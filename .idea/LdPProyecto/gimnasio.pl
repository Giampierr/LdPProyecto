%% ============================================================
%%  gimnasio.pl  -  Base de conocimiento del gimnasio (SWI-Prolog)
%%  Cargar con:  ?- [gimnasio].
%% ============================================================

:- use_module(library(aggregate)).
:- use_module(library(apply)).
:- use_module(library(lists)).

%% ------------------------------------------------------------
%% 1. HECHOS (equivalen a las tablas de gimnasia.db)
%% ------------------------------------------------------------

% socio(Id, Codigo, Nombre, Apellido, TipoDoc, NroDoc, Telefono, Email, FechaRegistro)
% TipoDoc: dni | carnet_extranjeria   (enum TipoDocumento)
socio(1, 's001', 'Ana',   'Torres',  dni,                '12345678',  '987654321', 'ana.torres@mail.com',  date(2026,1,10)).
socio(2, 's002', 'Luis',  'Rojas',   dni,                '87654321',  '912345678', 'luis.rojas@mail.com',  date(2026,2,15)).
socio(3, 's003', 'Marta', 'Quispe',  carnet_extranjeria, '123456789', '999888777', 'marta.q@mail.com',     date(2026,3,5)).
socio(4, 's004', 'Pedro', 'Salas',   dni,                '1234567',   '812345678', 'pedro-salas-mail.com', date(2026,4,20)).  % datos inválidos a propósito

% membresia(Id, IdSocio, FechaInicio, FechaFin, Precio, Estado)
% Estado: activo | inactivo   (enum EstadoSocio)
membresia(1, 1, date(2026,9,1),  date(2026,12,1),  150.00, activo).
membresia(2, 2, date(2026,8,1),  date(2026,10,10), 100.00, activo).
membresia(3, 3, date(2026,5,1),  date(2026,9,1),   120.00, inactivo).

% pago(Id, Fecha, Monto)   (en el modelo Scala el pago no referencia a la membresía)
pago(1, date(2026,9,1),  100.00).
pago(2, date(2026,9,15),  50.00).
pago(3, date(2026,8,1),   60.00).
pago(4, date(2026,5,2),  120.00).

% pago_membresia(IdPago, IdMembresia)  -- vínculo SUPUESTO; en la BD actual no existe
pago_membresia(1, 1).
pago_membresia(2, 1).
pago_membresia(3, 2).
pago_membresia(4, 3).

% administrador(Id, Usuario, Password, Nombre, Estado)
% Estado: activo | inactivo   (enum EstadoAdministracion)
administrador(1, 'admin',  'x1y2z3', 'Carla Mendoza', activo).
administrador(2, 'jperez', 'abc123', 'Jorge Pérez',   inactivo).

%% ------------------------------------------------------------
%% 2. FECHAS
%% ------------------------------------------------------------

% hoy(-Fecha): fecha actual como date(A,M,D)
hoy(date(Y,M,D)) :-
    get_time(T),
    stamp_date_time(T, date(Y,M,D,_,_,_,_,_,_), local).

% fecha_mas_dias(+Fecha0, +N, -Fecha1)
fecha_mas_dias(date(Y,M,D), N, date(Y1,M1,D1)) :-
    date_time_stamp(date(Y,M,D,0,0,0,0,-,-), S0),
    S1 is S0 + N*86400,
    stamp_date_time(S1, date(Y1,M1,D1,_,_,_,_,_,_), 'UTC').

% Nota: date(A,M,D) se ordena correctamente con @=<, @< (orden estándar).

%% ------------------------------------------------------------
%% 3. VALIDACIONES (equivalen a util/Validador.scala)
%% ------------------------------------------------------------

solo_digitos(Atom, N) :-
    atom_length(Atom, N),
    atom_chars(Atom, Cs),
    forall(member(C, Cs), char_type(C, digit)).

validar_dni(Dni)          :- solo_digitos(Dni, 8).
validar_carnet(Carnet)    :- solo_digitos(Carnet, 9).

validar_documento(dni, N)                :- validar_dni(N).
validar_documento(carnet_extranjeria, N) :- validar_carnet(N).

% Teléfono: 9 dígitos, empieza con 9
validar_telefono(Tel) :-
    solo_digitos(Tel, 9),
    sub_atom(Tel, 0, 1, _, '9').

% Correo: algo@dominio.ext  (una sola @, dominio con punto y partes no vacías)
validar_correo(Correo) :-
    atomic_list_concat([Local, Dominio], '@', Correo),
    Local \== '',
    atomic_list_concat(Partes, '.', Dominio),
    Partes = [_,_|_],
    \+ memberchk('', Partes).

% socio_valido(+IdSocio): cumple todas las validaciones del modelo Socio
socio_valido(Id) :-
    socio(Id, Cod, Nom, Ape, Tipo, Doc, Tel, Email, FReg),
    Cod \== '', Nom \== '', Ape \== '',
    validar_documento(Tipo, Doc),
    validar_telefono(Tel),
    validar_correo(Email),
    hoy(Hoy), FReg @=< Hoy.

% campos_invalidos(+IdSocio, -Lista): diagnóstico de qué campos fallan
campos_invalidos(Id, Fallos) :-
    socio(Id, _, _, _, Tipo, Doc, Tel, Email, _),
    findall(Campo,
            ( member(Campo-Test,
                     [ documento-validar_documento(Tipo, Doc),
                       telefono-validar_telefono(Tel),
                       correo-validar_correo(Email) ]),
              \+ call(Test) ),
            Fallos).

% membresia_valida(+IdMembresia): reglas del modelo Membresias
membresia_valida(Id) :-
    membresia(Id, _, Ini, Fin, Precio, _),
    hoy(Hoy),
    Ini @=< Hoy,          % inicio no futuro
    Ini @=< Fin,          % fin no anterior al inicio
    Precio > 0.

% pago_valido(+IdPago): reglas del modelo Pago
pago_valido(Id) :-
    pago(Id, Fecha, Monto),
    hoy(Hoy),
    Fecha @=< Hoy,
    Monto > 0.

%% ------------------------------------------------------------
%% 4. CONSULTAS SOBRE SOCIOS Y MEMBRESÍAS
%% ------------------------------------------------------------

nombre_completo(IdSocio, Nombre) :-
    socio(IdSocio, _, N, A, _, _, _, _, _),
    format(atom(Nombre), '~w ~w', [N, A]).

% membresia_vigente(+Hoy, ?IdMembresia): activa y dentro del rango de fechas
membresia_vigente(Hoy, IdM) :-
    membresia(IdM, _, Ini, Fin, _, activo),
    Ini @=< Hoy, Hoy @=< Fin.

% membresia_vencida(+Hoy, ?IdMembresia)
membresia_vencida(Hoy, IdM) :-
    membresia(IdM, _, _, Fin, _, _),
    Fin @< Hoy.

% socio_al_dia(?IdSocio): tiene al menos una membresía vigente hoy
socio_al_dia(IdS) :-
    hoy(Hoy),
    membresia(IdM, IdS, _, _, _, _),
    membresia_vigente(Hoy, IdM), !.

% socio_sin_membresia(?IdSocio)
socio_sin_membresia(IdS) :-
    socio(IdS, _, _, _, _, _, _, _, _),
    \+ membresia(_, IdS, _, _, _, _).

% por_vencer(+Dias, ?IdMembresia, ?IdSocio): vigentes que vencen en <= Dias
por_vencer(Dias, IdM, IdS) :-
    hoy(Hoy),
    fecha_mas_dias(Hoy, Dias, Limite),
    membresia(IdM, IdS, _, Fin, _, activo),
    Hoy @=< Fin, Fin @=< Limite.

% socios_por_estado_membresia(+Estado, -Nombres)
socios_por_estado_membresia(Estado, Nombres) :-
    findall(N, ( membresia(_, IdS, _, _, _, Estado), nombre_completo(IdS, N) ), L),
    sort(L, Nombres).

%% ------------------------------------------------------------
%% 5. PAGOS Y ESTADO DE CUENTA
%% ------------------------------------------------------------

% total_pagado_membresia(+IdM, -Total)
total_pagado_membresia(IdM, Total) :-
    membresia(IdM, _, _, _, _, _),
    aggregate_all(sum(M), ( pago_membresia(IdP, IdM), pago(IdP, _, M) ), Total).

% saldo_membresia(+IdM, -Saldo): precio - pagado (> 0 = deuda)
saldo_membresia(IdM, Saldo) :-
    membresia(IdM, _, _, _, Precio, _),
    total_pagado_membresia(IdM, Pagado),
    Saldo is Precio - Pagado.

% socio_con_deuda(?IdSocio, ?IdMembresia, ?Deuda)
socio_con_deuda(IdS, IdM, Deuda) :-
    membresia(IdM, IdS, _, _, _, _),
    saldo_membresia(IdM, Deuda),
    Deuda > 0.

% deuda_total_socio(+IdS, -Total)
deuda_total_socio(IdS, Total) :-
    socio(IdS, _, _, _, _, _, _, _, _),
    aggregate_all(sum(D), socio_con_deuda(IdS, _, D), Total).

% ingresos_totales(-Total) y en un rango de fechas
ingresos_totales(Total) :-
    aggregate_all(sum(M), pago(_, _, M), Total).

ingresos_entre(Desde, Hasta, Total) :-
    aggregate_all(sum(M), ( pago(_, F, M), Desde @=< F, F @=< Hasta ), Total).

% estado_cuenta(+IdSocio): imprime el estado de cuenta
estado_cuenta(IdS) :-
    nombre_completo(IdS, Nombre),
    format("Estado de cuenta de ~w~n", [Nombre]),
    forall( membresia(IdM, IdS, Ini, Fin, Precio, Est),
            ( total_pagado_membresia(IdM, Pag),
              Saldo is Precio - Pag,
              format("  Membresía ~w [~w -> ~w] estado=~w precio=~2f pagado=~2f saldo=~2f~n",
                     [IdM, Ini, Fin, Est, Precio, Pag, Saldo]) ) ).

%% ------------------------------------------------------------
%% 6. ADMINISTRADORES
%% ------------------------------------------------------------

admin_activo(Usuario) :- administrador(_, Usuario, _, _, activo).

% login(+Usuario, +Password): solo administradores activos
login(Usuario, Password) :-
    administrador(_, Usuario, Password, _, activo).

%% ------------------------------------------------------------
%% 7. EJEMPLOS DE CONSULTAS
%% ------------------------------------------------------------
%  ?- socio_valido(1).                  % true
%  ?- campos_invalidos(4, F).           % F = [documento, telefono, correo]
%  ?- socio_al_dia(S).                  % socios con membresía vigente
%  ?- por_vencer(15, M, S).             % vencen en los próximos 15 días
%  ?- socio_con_deuda(S, M, D).         % deudas pendientes
%  ?- deuda_total_socio(1, T).
%  ?- ingresos_totales(T).
%  ?- ingresos_entre(date(2026,9,1), date(2026,9,30), T).
%  ?- estado_cuenta(1).
%  ?- login('admin', 'x1y2z3').
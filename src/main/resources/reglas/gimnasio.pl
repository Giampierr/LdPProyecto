
% =====================================
% DATOS  DEL GIMNASIO
% =====================================

socio(001, "Carlos", vigente).
socio(002, "Maria", vencida).
socio(003, "Jose", vigente).
socio(004, "Ana", suspendida).
socio(005, "LUIS", vencida).

% =====================================
% REGLAS DE NEGOCIO
% =====================================

puede_ingresar(Id) :-
    socio(Id, _, vigente).

necesita_renovar(Id) :-
    socio(Id, _, vencida).

esta_suspendido(Id) :-
    socio(Id, _, suspendida).

% =====================================
% CONSULTAS PARA SCALA
% =====================================

resultado_ingreso(Id) :-
    (puede_ingresar(Id) -> writeln(true) ; writeln(false)).

resultado_renovacion(Id) :-
    (necesita_renovar(Id) -> writeln(true) ; writeln(false)).

resultado_suspension(Id) :-
    (esta_suspendido(Id) -> writeln(true) ; writeln(false)).

mostrar_socios :-
    forall(
        socio(Id, Nombre, Estado),
        format('~w|~w|~w~n', [Id, Nombre, Estado])
    ).

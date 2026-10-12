
CREATE TABLE IF NOT EXISTS ADMINISTRACION (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT NOT NULL UNIQUE CHECK (length(trim(usuario)) BETWEEN 4 AND 30),
	password TEXT NOT NULL,
	nombre TEXT NOT NULL CHECK (length(trim(nombre)) BETWEEN 2 AND 100),
	estado TEXT NOT NULL CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS SOCIOS (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo TEXT NOT NULL UNIQUE CHECK (length(trim(codigo)) BETWEEN 1 AND 20),
    nombre TEXT NOT NULL CHECK (length(trim(nombre)) BETWEEN 2 AND 60),
    apellido TEXT NOT NULL CHECK (length(trim(apellido)) BETWEEN 2 AND 80),
    tipoDocumento TEXT NOT NULL CHECK (tipoDocumento IN ('Dni', 'CarnetExtranjeria')),
    nroDocumento TEXT NOT NULL UNIQUE CHECK (length(trim(nroDocumento)) BETWEEN 8 AND 15),
    telefono TEXT NOT NULL CHECK (length(telefono) = 9 AND telefono NOT GLOB '*[^0-9]*' AND telefono GLOB '9*'),
    email TEXT NOT NULL COLLATE NOCASE UNIQUE CHECK (email NOT GLOB '* *' AND email GLOB '*@*.*' AND instr(email, '@') > 1),
    fechaRegistro TEXT NOT NULL DEFAULT (date('now')) CHECK (date(fechaRegistro) IS NOT NULL AND date(fechaRegistro) = fechaRegistro)
);

CREATE TRIGGER IF NOT EXISTS validar_fecha_socio_insert BEFORE INSERT ON SOCIOS FOR EACH ROW WHEN NEW.fechaRegistro > date('now')
BEGIN
    SELECT RAISE(ABORT, 'La fecha de registro no puede ser futura');
END;

CREATE TRIGGER IF NOT EXISTS validar_fecha_socio_update BEFORE UPDATE OF fechaRegistro ON SOCIOS FOR EACH ROW WHEN NEW.fechaRegistro > date('now')
BEGIN
    SELECT RAISE(ABORT, 'La fecha de registro no puede ser futura');
END;

CREATE TABLE IF NOT EXISTS MEMBRESIAS (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idSocio INTEGER NOT NULL,
    fechaInicio TEXT NOT NULL CHECK(date(fechaInicio) IS NOT NULL AND date(fechaInicio) = fechaInicio),
    fechaFin TEXT NOT NULL CHECK(date(fechaFin) IS NOT NULL AND date(fechaFin) = fechaFin AND fechaFin >= fechaInicio),
    precio NUMERIC NOT NULL CHECK(precio > 0),
    estado TEXT NOT NULL DEFAULT 'ACTIVA' CHECK(estado IN ('ACTIVA', 'VENCIDA', 'CANCELADA')),
    FOREIGN KEY (idSocio) REFERENCES SOCIOS(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS PAGOS (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha TEXT NOT NULL DEFAULT (datetime('now', 'localtime')) CHECK(datetime(fecha) IS NOT NULL),
    monto NUMERIC NOT NULL CHECK(monto > 0)
);

INSERT INTO ADMINISTRACION (usuario, password, nombre, estado) VALUES
('admin01', 'Admin#2026a', 'Carlos Ramirez', 'ACTIVO'),
('admin02', 'Maria@2026b', 'Maria Torres', 'ACTIVO'),
('admin03', 'Gym$1234c', 'Jose Mendoza', 'ACTIVO'),
('admin04', 'Clave*456d', 'Ana Castillo', 'ACTIVO'),
('admin05', 'Pass!789e', 'Luis Fernandez', 'ACTIVO'),
('admin06', 'Rosa#2026f', 'Rosa Vargas', 'ACTIVO'),
('admin07', 'MigueL@45g', 'Miguel Flores', 'ACTIVO'),
('admin08', 'Lucia$789h', 'Lucia Herrera', 'ACTIVO'),
('admin09', 'Pedro!2026i', 'Pedro Chavez', 'ACTIVO'),
('admin10', 'Carmen#54j', 'Carmen Rojas', 'ACTIVO'),
('admin11', 'Diego@987k', 'Diego Morales', 'ACTIVO'),
('admin12', 'Sofia*2026l', 'Sofia Castro', 'ACTIVO'),
('admin13', 'Jorge$321m', 'Jorge Medina', 'ACTIVO'),
('admin14', 'Elena!654n', 'Elena Ruiz', 'ACTIVO'),
('admin15', 'Andres#987o', 'Andres Silva', 'ACTIVO'),
('admin16', 'Patri@2026p', 'Patricia Leon', 'INACTIVO'),
('admin17', 'Ricar$456q', 'Ricardo Vega', 'INACTIVO'),
('admin18', 'Dani!789r', 'Daniela Soto', 'INACTIVO'),
('admin19', 'Ferna#123s', 'Fernando Paredes', 'INACTIVO'),
('admin20', 'Gabi@456t', 'Gabriela Reyes', 'INACTIVO'),
('admin21', 'Oscar$2026u', 'Oscar Navarro', 'ACTIVO'),
('admin22', 'Vale!789v', 'Valeria Campos', 'ACTIVO'),
('admin23', 'Manu#321w', 'Manuel Aguilar', 'ACTIVO'),
('admin24', 'Clau@654x', 'Claudia Salazar', 'ACTIVO'),
('admin25', 'Hector$987y', 'Hector Romero', 'ACTIVO'),
('admin26', 'Paola!2026z', 'Paola Guerrero', 'ACTIVO'),
('admin27', 'Alber#456A', 'Alberto Fuentes', 'ACTIVO'),
('admin28', 'Moni@789B', 'Monica Ponce', 'ACTIVO'),
('admin29', 'Eduar$321C', 'Eduardo Cabrera', 'ACTIVO'),
('admin30', 'Adri!654D', 'Adriana Espinoza', 'ACTIVO');

INSERT INTO SOCIOS (codigo, nombre, apellido, tipoDocumento,nroDocumento, telefono, email, fechaRegistro) VALUES
('SOC001', 'Carlos', 'Ramirez', 'Dni', '12345678', '987654321', 'carlos.ramirez@gmail.com', '2026-01-10'),
('SOC002', 'Maria', 'Torres', 'Dni', '23456781', '912345678', 'maria.torres@gmail.com', '2026-01-15'),
('SOC003', 'Jose', 'Mendoza', 'Dni', '34567812', '923456781', 'jose.mendoza@gmail.com', '2026-02-01'),
('SOC004', 'Ana', 'Castillo', 'Dni', '45678123', '934567812', 'ana.castillo@gmail.com', '2026-02-05'),
('SOC005', 'Luis', 'Fernandez', 'Dni', '56781234', '945678123', 'luis.fernandez@gmail.com', '2026-02-12'),
('SOC006', 'Rosa', 'Vargas', 'Dni', '67812345', '956781234', 'rosa.vargas@gmail.com', '2026-02-20'),
('SOC007', 'Miguel', 'Flores', 'Dni', '78123456', '967812345', 'miguel.flores@gmail.com', '2026-03-03'),
('SOC008', 'Lucia', 'Herrera', 'Dni', '81234567', '978123456', 'lucia.herrera@gmail.com', '2026-03-10'),
('SOC009', 'Pedro', 'Chavez', 'Dni', '89123456', '989123456', 'pedro.chavez@gmail.com', '2026-03-15'),
('SOC010', 'Carmen', 'Rojas', 'Dni', '91234567', '991234567', 'carmen.rojas@gmail.com', '2026-03-22'),
('SOC011', 'Diego', 'Morales', 'Dni', '10234567', '912345679', 'diego.morales@gmail.com', '2026-04-01'),
('SOC012', 'Sofia', 'Castro', 'Dni', '11234567', '923456782', 'sofia.castro@gmail.com', '2026-04-08'),
('SOC013', 'Jorge', 'Medina', 'Dni', '12234567', '934567813', 'jorge.medina@gmail.com', '2026-04-15'),
('SOC014', 'Elena', 'Ruiz', 'Dni', '13234567', '945678124', 'elena.ruiz@gmail.com', '2026-05-02'),
('SOC015', 'Andres', 'Silva', 'Dni', '14234567', '956781235', 'andres.silva@gmail.com', '2026-05-10'),
('SOC016', 'Patricia', 'Leon', 'CarnetExtranjeria', 'CE12345678', '967812346', 'patricia.leon@gmail.com', '2026-05-18'),
('SOC017', 'Ricardo', 'Vega', 'CarnetExtranjeria', 'CE23456789', '978123457', 'ricardo.vega@gmail.com', '2026-06-01'),
('SOC018', 'Daniela', 'Soto', 'Dni', '15234567', '989123457', 'daniela.soto@gmail.com', '2026-06-12'),
('SOC019', 'Fernando', 'Paredes', 'Dni', '16234567', '991234568', 'fernando.paredes@gmail.com', '2026-07-05'),
('SOC020', 'Gabriela', 'Reyes', 'Dni', '17234567', '912345680', 'gabriela.reyes@gmail.com', '2026-08-20');


INSERT INTO MEMBRESIAS (idSocio, fechaInicio, fechaFin, precio, estado) VALUES
(1, '2026-10-01', '2026-10-31', 100.00, 'ACTIVA'),
(2, '2026-10-05', '2026-11-04', 100.00, 'ACTIVA'),
(3, '2026-09-01', '2026-11-30', 270.00, 'ACTIVA'),
(4, '2026-07-01', '2026-09-30', 270.00, 'VENCIDA'),
(5, '2026-10-10', '2026-11-08', 100.00, 'ACTIVA'),
(6, '2026-01-01', '2026-12-31', 900.00, 'ACTIVA'),
(7, '2026-08-01', '2026-08-30', 100.00, 'VENCIDA'),
(8, '2026-10-12', '2027-01-09', 270.00, 'ACTIVA'),
(9, '2026-06-01', '2026-06-30', 100.00, 'VENCIDA'),
(10, '2026-09-15', '2026-10-14', 100.00, 'ACTIVA');


INSERT INTO PAGOS (fecha, monto) VALUES
('2026-10-01 09:15:00', 100.00),
('2026-10-05 10:30:00', 100.00),
('2026-09-01 11:00:00', 270.00),
('2026-07-01 14:20:00', 270.00),
('2026-10-10 08:45:00', 100.00),
('2026-01-01 16:00:00', 900.00),
('2026-08-01 12:10:00', 100.00),
('2026-10-12 09:00:00', 270.00),
('2026-06-01 17:30:00', 100.00),
('2026-09-15 13:45:00', 100.00);

UPDATE PAGOS
SET fecha = '2026-10-11 09:00:00'
WHERE id = 8;

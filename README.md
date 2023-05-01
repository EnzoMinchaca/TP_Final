# TP_Final

https://github.com/EnzoMinchaca/TP_Final/tree/main

Para crear las tablas:

CREATE TABLE alumnos(
    nombre VARCHAR(50),
    legajo INT,
    materias_aprobadas JSON,
    PRIMARY KEY(legajo)
);

CREATE TABLE materias(
    nombre VARCHAR(50),
    correlativas JSON,
    PRIMARY KEY(nombre)
);

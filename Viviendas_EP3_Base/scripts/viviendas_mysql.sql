-- =====================================================================
-- empresa administradora de viviendas - Base de datos (MySQL 8)
-- Crea la base de datos viviendas, sus tablas y los datos iniciales.
-- Ejecuta el script completo. No lo modifiques.
-- =====================================================================

DROP DATABASE IF EXISTS viviendas;
CREATE DATABASE viviendas CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE viviendas;

-- Catálogo de tipos
CREATE TABLE tipo_vivienda (
    id_tipo      INT          NOT NULL,
    descripcion  VARCHAR(30)  NOT NULL,
    CONSTRAINT pk_tipo_vivienda PRIMARY KEY (id_tipo),
    CONSTRAINT uq_tipo_vivienda_descripcion UNIQUE (descripcion)
);

-- Registros.
-- piso, gasto_comun_al_dia, estacionamiento se usan solo para Departamento;
-- patio se usa solo para Casa.
CREATE TABLE vivienda (
    id                    INT           NOT NULL AUTO_INCREMENT,
    id_tipo               INT           NOT NULL,
    codigo                VARCHAR(80)   NOT NULL,
    superficie            DECIMAL(10,2) NOT NULL,
    habitaciones          INT           NOT NULL,
    piso                  INT           NULL,
    gasto_comun_al_dia    BOOLEAN       NULL,
    estacionamiento       BOOLEAN       NULL,
    patio                 BOOLEAN       NULL,
    CONSTRAINT pk_vivienda PRIMARY KEY (id),
    CONSTRAINT fk_vivienda_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_vivienda (id_tipo),
    CONSTRAINT ck_vivienda_superficie CHECK (superficie BETWEEN 20 AND 500),
    CONSTRAINT ck_vivienda_habitaciones CHECK (habitaciones > 0),
    CONSTRAINT ck_vivienda_piso CHECK (piso > 0)
);

-- Datos iniciales
INSERT INTO tipo_vivienda (id_tipo, descripcion) VALUES (1, 'Departamento');
INSERT INTO tipo_vivienda (id_tipo, descripcion) VALUES (2, 'Casa');

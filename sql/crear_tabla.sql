-- SQLite: esquema inicial con las tres columnas.
-- La aplicación ya ejecuta esta creación al iniciar: no necesitas hacerlo a mano.
-- También puedes ejecutarlo en DB Browser for SQLite sobre personas.db.

CREATE TABLE IF NOT EXISTS personas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL CHECK(length(nombre) BETWEEN 1 AND 100),
    correo TEXT NOT NULL CHECK(length(correo) BETWEEN 1 AND 150)
);

SELECT id, nombre, correo FROM personas ORDER BY id;
DROP TABLE IF EXISTS Client, Manager;

CREATE TABLE IF NOT EXISTS Client (
    id SERIAL,
    name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Manager (
    no SERIAL,
    label VARCHAR(50),
    param1 VARCHAR(50)
);
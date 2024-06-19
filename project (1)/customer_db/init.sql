CREATE DATABASE customerdb;
\c customerdb;

CREATE TABLE IF NOT EXISTS customer (
   id serial PRIMARY KEY,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL
);

INSERT INTO customer(id, first_name, last_name)
VALUES 
   (1, 'Luisa', 'Colon'),
	(2, 'Ismail', 'Southern'),
	(3, 'Kory', 'Morley');
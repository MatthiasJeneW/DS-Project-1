CREATE DATABASE stationdb;
\c stationdb;

CREATE TABLE IF NOT EXISTS charge (
   id SERIAL PRIMARY KEY,
   kwh REAL NOT NULL,
   customer_id INTEGER NOT NULL
);

INSERT INTO charge(id, kwh, customer_id)
VALUES 
   (1, '48.2', 3),
   (2, '40.5', 3),
   (3, '32.9', 1),
   (4, '35.3', 3),
   (5, '12.6', 3),
   (6, '41.3', 1),
   (7, '24.4', 2),
   (8, '10.4', 3),
   (9, '20.5', 2),
   (10, '29', 3),
   (11, '43.5', 1),
   (12, '38.9', 2),
   (13, '18.6', 2),
   (14, '31.7', 1),
   (15, '32.1', 2),
   (16, '13.8', 1),
   (17, '19.8', 2),
   (18, '19.1', 1),
   (19, '45.4', 2),
   (20, '38.1', 2);
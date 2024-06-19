CREATE DATABASE stationdb;
\c stationdb;

CREATE TABLE IF NOT EXISTS charge (
   id SERIAL PRIMARY KEY,
   kwh REAL NOT NULL,
   customer_id INTEGER NOT NULL
);

INSERT INTO charge(id, kwh, customer_id)
VALUES 
   (1, '50.4', 2),
   (2, '10.8', 1),
   (3, '13.7', 2),
   (4, '10.6', 1),
   (5, '39.6', 2),
   (6, '18.2', 2),
   (7, '24.4', 2),
   (8, '31.3', 3),
   (9, '44.1', 2),
   (10, '45.7', 3),
   (11, '49.7', 1),
   (12, '25.3', 3),
   (13, '43.5', 2),
   (14, '21.9', 2),
   (15, '19.4', 3),
   (16, '22.7', 2),
   (17, '25.9', 2),
   (18, '14.2', 2),
   (19, '45.1', 3),
   (20, '42.8', 2);
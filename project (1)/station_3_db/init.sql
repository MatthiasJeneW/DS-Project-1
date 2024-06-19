CREATE DATABASE stationdb;
\c stationdb;

CREATE TABLE IF NOT EXISTS charge (
   id SERIAL PRIMARY KEY,
   kwh REAL NOT NULL,
   customer_id INTEGER NOT NULL
);

INSERT INTO charge(id, kwh, customer_id)
VALUES 
   (1, '25.9', 1),
   (2, '12.7', 2),
   (3, '13.5', 3),
   (4, '37.4', 1),
   (5, '25.3', 2),
   (6, '34.6', 2),
   (7, '10.7', 1),
   (8, '27.9', 2),
   (9, '22.4', 3),
   (10, '16.7', 2),
   (11, '13.6', 2),
   (12, '27.2', 1),
   (13, '20.5', 1),
   (14, '19.2', 3),
   (15, '27.6', 3),
   (16, '34.9', 3),
   (17, '33.5', 3),
   (18, '45.8', 1),
   (19, '38.1', 2),
   (20, '20.2', 2);
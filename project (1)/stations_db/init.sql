CREATE DATABASE stationdb;
\c stationdb;

CREATE TABLE IF NOT EXISTS station (
   id SERIAL PRIMARY KEY,
   db_url VARCHAR(255) NOT NULL,
   lat REAL NOT NULL,
   lng REAL NOT NULL
);

INSERT INTO station(id, db_url, lat, lng)
VALUES 
	(1, 'localhost:30011', '48.184192', '16.378604'),
   (2, 'localhost:30012', '48.186116', '16.377746'),
	(3, 'localhost:30013', '48.232940', '16.376786');
# Distributed Systems Semester Project
An example setup for a Distributed Systems project. It contains five databases (PostgreSQL) with example data and a messaging queue (RabbitMQ).

## Services
- Customer Database
	- Contains customer data (id, first name, last name)
	- URL: localhost:30001
- Stations Database
	- Contains station data (id, db_url, latitude, longitude)
	- URL: localhost:30002
- Individual Station Databases
	- Contains customer station data (id, kwh, customer_id)
	- URL Station 1: localhost:30011
	- URL Station 2: localhost:30012
	- URL Station 3: localhost:30013
- Queue
	- URL: localhost:30003
	- Web: localhost:30083

## Requirements
- [Docker](https://docs.docker.com/get-docker/)

## Start
```shell
docker-compose up
```

## RabbitMQ-Dashboard
- [RabbitMQ-Dashboard](http://localhost:30083)
- Username: guest
- Password: guest


## Documentations
- [RabbitMQ](https://www.rabbitmq.com/tutorials/tutorial-one-java.html)

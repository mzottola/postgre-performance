# Postgre Perf

## Description

The stack is composed of:
- a spring boot app
- a postgre database

A schema is created at each run: see postgre-performance/src/main/resources/schema.sql

The purpose of this application was to verify performance regarding nested array column.  
Instead of having 3 distinct tables to handle user rights, use an array column to know every user that have access to the concerned entity

In other words, instead of this model:
```
t_entity(entity_id) <----> t_join_table(entity_id, user_id) <----> t_users (user_id)
```
we would have this model:
```
t_entity(entity_id, users[])
```

## Test
The 2nd model is used here using 2 different ways:
- with hibernate
- with pure sql using entity manager

For each way:
- load 100_000 entities - for each entity there are either 100 or 120 users (even / odd)
- random select a user id: select every entity that the user has access to

I have the following results when launching using docker containers:
```sh
[main] c.t.p.PostgrePerformanceApplication      : loading 100000 entities...
[main] c.t.p.PostgrePerformanceApplication      : 100000 entities inserted in 13353 ms
[main] c.t.p.PostgrePerformanceApplication      : select in database entities linked to username_38 using JPA repository...
[main] c.t.p.PostgrePerformanceApplication      : 100000 entities retrieved in 3815 ms
[main] c.t.p.PostgrePerformanceApplication      : ----------------------------------
[main] c.t.p.PostgrePerformanceApplication      : USE PURE SQL
[main] c.t.p.PostgrePerformanceApplication      : loading 100000 entities...
[main] c.t.p.PostgrePerformanceApplication      : 100000 entities inserted in 6562 ms
[main] c.t.p.PostgrePerformanceApplication      : select in database entities linked to username_38 using Entity Manager...
[main] c.t.p.PostgrePerformanceApplication      : 100000 entities retrieved in 229 ms
```


## How to launch stack as containers
```sh
docker-compose build
docker-compose up
```

## How to launch only the database as a container
```sh
docker-compose -f docker-compose-dev.yml up
```

#!/bin/bash

docker-compose down

docker rmi aplicacao-web

docker rmi aplicacao-crud-empresas

docker rmi aplicacao-crud-usuarios

docker rmi mysql

docker-compose up -d
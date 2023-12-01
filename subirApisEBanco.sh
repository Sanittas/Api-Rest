#!/bin/bash

docker-compose up db -d

docker-compose up crud-usuarios -d

docker-compose up crud-empresas -d
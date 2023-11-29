#!/bin/bash

# Atualiza o repositório "Aplicacao"
git pull origin main

# Atualiza o repositório "CRUD-Usuario"
cd CRUD-Usuario
git pull origin main
git submodule update --remote

# Atualiza o repositório "sannitas-web"
cd ..
cd sannitas-web
git pull origin main
git submodule update --remote

# Atualiza o repositório "Servico-empresa"
cd ..
cd Servico-empresa
git pull origin main
git submodule update --remote

cd ..
git add .
git commit -m 'atualizacao de submodulos'
git push origin main

echo "Atualização concluída!"

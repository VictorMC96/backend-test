# Examen

Proyecto muestra backend

## Requisitos

Asegúrate de tener instalado lo siguiente 
-Docker (https://docs.docker.com/get-docker/)
-IDE(Intellij, eclipse , etc)

## Instrucciones de Uso

1. Clona este repositorio en tu máquina local:
   git clone https://github.com/VictorMC96/backend-test.git
2. Cambia la rama 
   git checkout FernandoMaldonadoGutierrez
3. Crea una instancia de docker para la base de datos o usa una base de datos previamente instalada
   docker run --detach --name vehiculosdb --env MARIADB_ROOT_PASSWORD=root-secret-password --env MARIADB_USER=example-user --env MARIADB_PASSWORD=user-password --env MARIADB_DATABASE=vehiculos-db -dp 3306:3306  mariadb:latest
4. Ejecuta el programa de Spring para crear las tablas que usaremos 
5. (Opcional) al no tener datos para poder ingresar horas de entrada y salida tendremos que tener datos previos si quieres ejecutar entradas y salidas primero ejecuta el siguiente query
   INSERT INTO vehiculo (placa, tipo) VALUES ('ABC123', 'OFICIAL');
   INSERT INTO vehiculo (placa, tipo) VALUES ('XYZ789', 'RESIDENTE');
6. Los metodos empleados son de tipo POST y pide como parametro placa=valor
   http://localhost:8080/vehiculos/entrada 
   http://localhost:8080/vehiculos/salida
   http://localhost:8080/vehiculos/alta/oficial
   http://localhost:8080/vehiculos/alta/residente

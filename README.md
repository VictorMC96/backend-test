# Uso

## IDE
abrir carpeta parking-neology y correr ParkingNeologyApplication con el boton RUN.

## Terminal
cd parking-neology
./mvnw spring-boot:run

## Diagrama de clases
![ Diagrama de clases](https://github.com/Argenis616/backend-test/blob/jorge/argenis/hernandez/chavez/img/claseNeology.png)

## diagrama de secuencia
![ Diagrama de secuencia](https://github.com/Argenis616/backend-test/blob/jorge/argenis/hernandez/chavez/img/sequenceNeology.png)

### Casos de uso

###### **Caso de uso "Registra entrada"**
POST: localhost:8080/parking/entry/{plate}
plate: No. de placa a registrar para su entrada

###### **Caso de uso "Registra salida"**
POST: localhost:8080/parking/exit/{plate}
plate: No. de placa a registrar para su salida

###### **Caso de uso "Da de alta vehículo oficial"**
POST: localhost:8080/parking/official/{plate}
plate: No. de placa a dar de alta como vehiculo oficial

###### **Caso de uso "Da de alta vehículo de residente"**
POST: localhost:8080/parking/resident/{plate}
plate: No. de placa a dar de alta como vehiculo residente

###### **Caso de uso "Comienza mes"**
GET: localhost:8080/parking/start-month


###### **Caso de uso "Pagos de residentes"**
GET: localhost:8080/parking/pay/{file-name}
file-name: Nombre del archivo a descargar.

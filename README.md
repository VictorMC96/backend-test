# Prueba Técnica - Neology
## Estacionamiento Backend API
Este proyecto es una solución a la prueba técnica para un puesto de desarrollador backend en Neology. Se trata de una API RESTful construida con Spring Boot que gestiona las operaciones de un sistema de estacionamiento.

## Requisitos del Sistema
Para compilar y ejecutar este proyecto, asegúrate de tener instalados los siguientes componentes:

* Java 17 o una versión superior.
* Maven para la gestión de dependencias.
* Un IDE como IntelliJ IDEA o Visual Studio Code.

## Configuración y Ejecución
Sigue estos pasos para poner en marcha la aplicación:

* Clonar el Repositorio
* Clona este repositorio en tu máquina local.
* Abrir en el IDE
* Abre la carpeta del proyecto en tu IDE. El IDE reconocerá el proyecto como un proyecto Maven y descargará automáticamente todas las dependencias necesarias.

### Ejecutar la Aplicación
* Ejecuta la clase principal EstacionamientoApplication.java desde tu IDE. La aplicación se iniciará en el puerto por defecto 8080.

### Uso de la API (Endpoints)
La API expone los siguientes endpoints para interactuar con el sistema de estacionamiento. Puedes utilizar un cliente HTTP como Postman, Insomnia o el cliente integrado de tu IDE.

#### Endpoints de Operación:
* POST /api/entrada
Registra la entrada de un vehículo.

Parámetros: placa (string).

Ejemplo: http://localhost:8080/api/entrada?placa=XYZ123

* POST /api/salida
Registra la salida de un vehículo y calcula el monto a pagar.

Parámetros: placa (string).

Ejemplo: http://localhost:8080/api/salida?placa=XYZ123

* GET /api/descargarInforme
Genera un informe con los pagos de los residentes y permite su descarga.

Parámetros: nombreArchivo (string).

Ejemplo: http://localhost:8080/api/descargarInforme?nombreArchivo=pagos_residentes.txt

#### Endpoints de Administración:
* POST /api/alta-oficial
Registra un vehículo como oficial.

Parámetros: placa (string).

* POST /api/alta-residente
Registra un vehículo como residente.

Parámetros: placa (string).

* POST /api/comienza-mes
Reinicia el registro de tiempo y las estancias de los vehículos para un nuevo mes de facturación.

Sin parámetros.

### Pruebas Unitarias
Para ejecutar las pruebas que validan la lógica de negocio de la aplicación, puedes usar la funcionalidad de tu IDE o el siguiente comando de Maven en la terminal del proyecto:

``mvn test``

# Documentación de la API
La API cuenta con documentación interactiva generada con Swagger (OpenAPI). Una vez que la aplicación esté en ejecución, puedes acceder a la siguiente URL en tu navegador para explorarla:

http://localhost:8080/swagger-ui.html
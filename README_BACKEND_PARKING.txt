🅿️ Parking Backend - Sistema de Control de Estacionamiento

Este proyecto es una API REST desarrollada en Java con Spring Boot que gestiona el control de acceso y estancia de vehículos en un estacionamiento. Se soportan vehículos oficiales, residentes y no residentes. Incluye funcionalidades como registrar entradas y salidas, generar reportes de pagos y resetear información mensual.

⚙️ Tecnologías
- Java 11
- Spring Boot 2.7.x
- Spring Data JPA
- MySQL
- Maven
- Hibernate
- Lombok

▶️ Cómo ejecutar

1. Crea la base de datos en MySQL (manualmente):
   CREATE DATABASE parking_db;

2. Configura la conexión en application.properties:
   spring.datasource.url=jdbc:mysql://localhost:3306/parking_db?useSSL=false
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=update

3. Ejecuta el backend:
   ./mvnw spring-boot:run
   o ejecuta ParkingApplication.java desde tu IDE.

4. Importa y prueba los endpoints en Postman o Insomnia.

📬 Endpoints disponibles

1. Registrar entrada de vehículo
   POST /api/stay/entry?plate=ABC123

2. Registrar salida de vehículo
   POST /api/stay/exit?plate=ABC123

3. Borrar todas las estancias (inicio de mes)
   DELETE /api/stay/clear

4. Alta vehículo oficial
   POST /api/vehicles/official?plate=XYZ111

5. Alta vehículo residente
   POST /api/vehicles/resident?plate=ZZZ999

6. Reiniciar datos mensuales
   POST /api/vehicles/reset

7. Generar reporte de pagos (JSON)
   GET /api/report/residents

8. Consultar estancias por placa
   GET /api/stay/byPlate?plate=ABC123

9. Listar todos los vehículos registrados
   GET /api/vehicles/all

🧪 Datos de prueba

Archivo data.sql preconfigurado con:
   INSERT INTO vehicle (vehicle_type, plate_number, accumulated_minutes)
   VALUES 
     ('OFFICIAL', 'ABC123', 0),
     ('RESIDENT', 'XYZ789', 40),
     ('NON_RESIDENT', 'TEST999', 0);

⚠️ Si ves un error de clave duplicada en el arranque, borra los datos existentes o elimina el contenido de data.sql.

📁 Estructura del proyecto

src/
├── controller/
│   └── StayController, VehicleController, ReportController
├── model/
│   └── Vehicle, Stay, OfficialVehicle, ResidentVehicle, NonResidentVehicle
├── repository/
│   └── VehicleRepository, StayRepository
├── service/
│   └── VehicleService, StayService, ReportService
└── ParkingApplication.java

🧾 Licencia

Este proyecto está bajo la licencia MIT. Libre uso para fines académicos y comerciales.
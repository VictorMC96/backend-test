
*******************************************************************
++++  Sección de sentencias SQL para creación de BDs y tablas  ****

CREATE DATABASE parking;
use parking;

CREATE TABLE vehicles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plate VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(255),
    accumulated_minutes INT DEFAULT 0
);

CREATE TABLE stays (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id BIGINT NOT NULL,
    entry_time DATETIME,
    exit_time DATETIME,
    duration_minutes INT,
    amount_paid DECIMAL(10,2),
    CONSTRAINT fk_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
        ON DELETE CASCADE
);

CREATE USER 'usrparking'@'localhost' IDENTIFIED BY 'pswparking';

GRANT ALL PRIVILEGES ON parking . * TO 'usrparking'@'localhost';












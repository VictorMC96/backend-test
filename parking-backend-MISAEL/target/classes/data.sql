
INSERT INTO vehicle (vehicle_type, plate_number, accumulated_minutes) VALUES
('OFFICIAL', 'ABC123', 0),
('RESIDENT', 'XYZ789', 40),
('NON_RESIDENT', 'TEST999', 0);

INSERT INTO stay (entry_time, exit_time, plate_number) VALUES
(NOW() - INTERVAL 90 MINUTE, NOW() - INTERVAL 60 MINUTE, 'XYZ789');

package com.ejercicio.estacionamiento;

import com.ejercicio.estacionamiento.exceptions.exceptions.PlacaNoEncontradaException;
import com.ejercicio.estacionamiento.model.Estancia;
import com.ejercicio.estacionamiento.model.Vehiculo;
import com.ejercicio.estacionamiento.service.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class EstacionamientoApplication implements CommandLineRunner {

    @Autowired
    private EstacionamientoService estacionamientoService;

    public static void main(String[] args) {
        SpringApplication.run(EstacionamientoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Registrar entrada");
            System.out.println("2. Registrar salida");
            System.out.println("3. Dar de alta vehículo oficial");
            System.out.println("4. Dar de alta vehículo residente");
            System.out.println("5. Comenzar mes");
            System.out.println("6. Generar informe de pagos de residentes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese la placa del vehículo: ");
                    String placaEntrada = scanner.nextLine();
                    Estancia entrada = estacionamientoService.registrarEntrada(placaEntrada);
                    System.out.println("Entrada registrada: " + entrada.getHoraEntrada().getTime());
                    System.out.println("Vehículo registrado como no residente. Se aplicará la tarifa correspondiente.");
                    break;
                case 2:
                    boolean salidaRegistrada = false;
                    while (!salidaRegistrada) {
                        try {
                            System.out.print("Ingrese la placa del vehículo: ");
                            String placaSalida = scanner.nextLine();
                            Estancia salida = estacionamientoService.registrarSalida(placaSalida);
                            System.out.println("Salida registrada: " + salida.getHoraSalida().getTime());
                            salidaRegistrada = true;
                        } catch (PlacaNoEncontradaException e) {
                            System.out.println(e.getMessage());
                            System.out.println("1. Volver a intentar");
                            System.out.println("2. Regresar al menú principal");
                            System.out.print("Seleccione una opción: ");
                            int subOpcion = scanner.nextInt();
                            scanner.nextLine();
                            if (subOpcion == 2) {
                                break;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.print("Ingrese la placa del vehículo oficial: ");
                    String placaOficial = scanner.nextLine();
                    Vehiculo oficial = estacionamientoService.registrarVehiculo(placaOficial, "oficial");
                    System.out.println("Vehículo oficial registrado: " + oficial);
                    break;
                case 4:
                    System.out.print("Ingrese la placa del vehículo residente: ");
                    String placaResidente = scanner.nextLine();
                    Vehiculo residente = estacionamientoService.registrarVehiculo(placaResidente, "residente");
                    System.out.println("Vehículo residente registrado: " + residente);
                    break;
                case 5:
                    estacionamientoService.comenzarMes();
                    System.out.println("Mes comenzado. Estancias y tiempos acumulados reseteados.");
                    break;
                case 6:
                    System.out.print("Ingrese el nombre del archivo para el informe: ");
                    String nombreArchivo = scanner.nextLine();
                    String informe = estacionamientoService.generarInformePagosResidentes(nombreArchivo);
                    if (informe.equals("No hay vehículos residentes registrados.")) {
                        System.out.println(informe);
                    } else {
                        System.out.println("Informe generado:\n" + informe);
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
}

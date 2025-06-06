package com.prueba.estacionamiento.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.estacionamiento.model.Estancia;
import com.prueba.estacionamiento.model.TarifaRequest;
import com.prueba.estacionamiento.model.TipoVehiculo;
import com.prueba.estacionamiento.service.EstanciaService;
import com.prueba.estacionamiento.service.PagoService;
import com.prueba.estacionamiento.service.TarifaService;
import com.prueba.estacionamiento.service.VehiculoService;

@RestController
@RequestMapping("/estancia")
public class EstanciaController {

	private final EstanciaService estanciaService;
	private final VehiculoService vehiculoService;
	private final TarifaService tarifaService;
	private final PagoService pagoService;

	public EstanciaController(EstanciaService estanciaService, VehiculoService vehiculoService,
			TarifaService tarifaService, PagoService pagoService) {
		this.estanciaService = estanciaService;
		this.vehiculoService = vehiculoService;
		this.tarifaService = tarifaService;
		this.pagoService = pagoService;
	}

	@PostMapping("/entrada")
	public ResponseEntity<?> registrarEntrada(@RequestBody Map<String, String> payload) {
		String placa = payload.get("placa");
		Estancia estancia = estanciaService.registrarEntrada(placa);
		return ResponseEntity
				.ok(Map.of("mensaje", "Entrada registrada", "horaEntrada", estancia.getEntrada().getTime()));
	}

	@PostMapping("/salida")
	public ResponseEntity<?> registrarSalida(@RequestBody Map<String, String> payload) {
		String placa = payload.get("placa");
		TipoVehiculo tipo = vehiculoService.obtenerTipoPorPlaca(placa);
		Estancia estancia = estanciaService.registrarSalida(placa);
		if (estancia == null)
			return ResponseEntity.notFound().build();

		long minutos = calcularMinutos(estancia.getEntrada(), estancia.getSalida());

		switch (tipo) {
		case OFICIAL:
			return ResponseEntity.ok(Map.of("mensaje", "Salida registrada para vehículo oficial", "minutos", minutos));

		case RESIDENTE:
			vehiculoService.acumularTiempo(placa, minutos);
			return ResponseEntity.ok(Map.of("mensaje", "Tiempo acumulado para residente", "minutos", minutos));

		case NO_RESIDENTE:
			BigDecimal monto = tarifaService
					.calcularTarifa(new TarifaRequest(placa, tipo, estancia.getEntrada(), estancia.getSalida()));
			pagoService.registrarPago(placa, tipo, monto);
			return ResponseEntity.ok(Map.of("mensaje", "Pago registrado", "monto", monto));

		default:
			return ResponseEntity.badRequest().body("Tipo de vehículo no válido");
		}
	}

	@PostMapping("/alta/oficial")
	public ResponseEntity<?> altaVehiculoOficial(@RequestBody Map<String, String> payload) {
		vehiculoService.registrarVehiculo(payload.get("placa"), TipoVehiculo.OFICIAL);
		return ResponseEntity.ok("Vehículo oficial registrado");
	}

	@PostMapping("/alta/residente")
	public ResponseEntity<?> altaVehiculoResidente(@RequestBody Map<String, String> payload) {
		vehiculoService.registrarVehiculo(payload.get("placa"), TipoVehiculo.RESIDENTE);
		return ResponseEntity.ok("Vehículo residente registrado");
	}

	@PostMapping("/comienza-mes")
	public ResponseEntity<?> comenzarMes() {
		estanciaService.eliminarEstanciasOficiales();
		vehiculoService.reiniciarTiempoResidentes();
		return ResponseEntity.ok("Nuevo mes iniciado. Estancias y tiempos reiniciados.");
	}

	@PostMapping("/pagos/residentes")
	public ResponseEntity<?> generarInforme(@RequestBody Map<String, String> payload) {
		String archivo = payload.get("archivo");
		pagoService.generarInformeResidentes(archivo);
		return ResponseEntity.ok("Informe generado: " + archivo);
	}

	private long calcularMinutos(Calendar entrada, Calendar salida) {
		return (salida.getTimeInMillis() - entrada.getTimeInMillis()) / (60 * 1000);
	}

}

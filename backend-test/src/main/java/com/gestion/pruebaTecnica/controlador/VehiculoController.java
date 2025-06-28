package com.gestion.pruebaTecnica.controlador;

import com.gestion.pruebaTecnica.entidades.TipoVehiculo;
import com.gestion.pruebaTecnica.entidades.Vehiculo;
import com.gestion.pruebaTecnica.servicio.TipoVehiculoService;
import com.gestion.pruebaTecnica.servicio.VehiculoService;
import com.gestion.pruebaTecnica.util.paginacion.PageRender;
import com.gestion.pruebaTecnica.util.reportes.VehiculoExporterPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class VehiculoController {

	@Autowired
	private VehiculoService vehiculoService;

	@Autowired
	private TipoVehiculoService tipoVehiculoService;

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm", Locale.US);
	public static SimpleDateFormat sdfResult = new SimpleDateFormat("HH:mm:ss", Locale.US);
	public static SimpleDateFormat sdfResultMinutos = new SimpleDateFormat("m", Locale.US);

	@GetMapping({"/","/listar",""})
	public String listarVehiculos(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Vehiculo> vehiculos = vehiculoService.findAll(pageRequest);
		PageRender<Vehiculo> pageRender = new PageRender<>("/listar", vehiculos);
		
		modelo.addAttribute("titulo","Listado de vehiculos");
		modelo.addAttribute("vehiculos",vehiculos);
		modelo.addAttribute("page", pageRender);
		
		return "listar";
	}

	@GetMapping("/form")
	public String mostrarFormularioDeRegistrarVehiculo(Map<String,Object> modelo) {
		Vehiculo vehiculo = new Vehiculo();
		modelo.put("vehiculo", vehiculo);
		modelo.put("titulo", "Registro de entrada vehiculo");
		return "form";
	}
	
	@PostMapping("/form")
	public String guardarVehiculo(@Valid Vehiculo vehiculo,BindingResult result,Model modelo,RedirectAttributes flash,SessionStatus status) throws ParseException {
		String mensaje="";
		String tVehiculo="";
		DecimalFormat formatter = new DecimalFormat("#0.00");

		if(result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de Vehiculo");
			return "form";
		}

		Vehiculo tv = vehiculoService.existe(vehiculo.getNumeroPlaca());
		if(tv != null){
			LocalDateTime fechaSalida = LocalDateTime.now();
			vehiculo.setFechaSalida(fechaSalida);
			Date todayDate = new Date();
			todayDate = asDate(fechaSalida);
			Date fEntrada  = asDate(vehiculo.getFechaEntrada());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fEnt = sdf.format(fEntrada);
			String todayDat = sdf.format(todayDate);
			Date difference = getDifferenceBetwenDates(sdf.parse(fEnt), sdf.parse(todayDat));
			int min = Integer.parseInt(sdfResultMinutos.format(difference));

			if(vehiculo.getTipoVehiculo()!= null){
				if(vehiculo.getTipoVehiculo().getVehiculoTipo().equals("Residente")){
					vehiculo.setImporte(Double.parseDouble(formatter.format(min * .05)));
				}else{
					vehiculo.setImporte(0.0);
				}
			}else{
				vehiculo.setImporte(min * .10);
			}

			vehiculo.setTiempo(min);

			if(tv.getNumeroPlaca().equals(vehiculo.getNumeroPlaca())){
				mensaje = "El Vehículo con placas " + vehiculo.getNumeroPlaca() + " ya sido registrado";
				status.setComplete();
				flash.addFlashAttribute("success", mensaje);
			}
		}else{
			DateTimeFormatter fecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime fechaEntrada = LocalDateTime.now();
			vehiculo.setFechaEntrada(fechaEntrada);
			 mensaje = (vehiculo.getIdVehiculo() != null) ? "El Vehículo ha sido editato con exito" : "Vehículo registrado con exito";
		}

		vehiculoService.save(vehiculo);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date getDifferenceBetwenDates(Date dateInicio, Date dateFinal) {
	long milliseconds = dateFinal.getTime() - dateInicio.getTime();
	int seconds = (int) (milliseconds / 1000) % 60;
	int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
	int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
	Calendar c = Calendar.getInstance();
				c.set(Calendar.SECOND, seconds);
				c.set(Calendar.MINUTE, minutes);
				c.set(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}

	@GetMapping("/salida")
	public String mostrarFormularioDeSalidaVehiculo(Map<String,Object> modelo) {
		Vehiculo vehiculo = new Vehiculo();
		modelo.put("vehiculo", vehiculo);
		modelo.put("titulo", "Registro de Salida vehiculo");
		return "salida";
	}

	@GetMapping("/salida/{id}")
	public String SalidaVehiculo(@PathVariable(value = "id") Long id,Map<String, Object> modelo,RedirectAttributes flash) {
		Vehiculo vehiculo = null;
		if(id > 0) {
			vehiculo = vehiculoService.findOne(id);
			if(vehiculo == null) {
				flash.addFlashAttribute("error", "El ID del vehiculo no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del vehiculo no puede ser cero");
			return "redirect:/listar";
		}
		DateTimeFormatter fecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime fechaSalida = LocalDateTime.now();

		vehiculo.setFechaSalida(fechaSalida);
		modelo.put("vehiculo",vehiculo);
		modelo.put("titulo", "Salida de vehiculo");
		return "form";
	}

	@GetMapping("/altaVehiculo")
	public String mostrarFormularioTipoVehiculo(Map<String,Object> modelo) {
		TipoVehiculo tipoVehiculo = new TipoVehiculo();
		modelo.put("tipoVehiculo", tipoVehiculo);
		modelo.put("titulo", "Registro alta tipo de vehiculo");
		return "altaVehiculo";
	}

	@PostMapping("/altaVehiculo")
	public String guardarTipoVehiculo(@Valid TipoVehiculo tipoVehiculo, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
		String mensaje;
		if(result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de tipo de Vehículo");
			return "altaVehiculo";
		}

		TipoVehiculo tv = tipoVehiculoService.existe(tipoVehiculo.getNumeroPlaca());
		if(tv != null){
			if(tv.getNumeroPlaca().equals(tipoVehiculo.getNumeroPlaca())){
				mensaje = "El Vehículo con placas " + tipoVehiculo.getNumeroPlaca() + " ya sido registrado";
				status.setComplete();
				flash.addFlashAttribute("success", mensaje);
			}
		}else {
			mensaje = (tipoVehiculo.getIdVehiculo() != null) ? "El Vehículo ha sido editato con exito" : "Tipo de vehículo registrado con exito";

			tipoVehiculoService.save(tipoVehiculo);
			status.setComplete();
			flash.addFlashAttribute("success", mensaje);
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/form/{id}")
	public String editarVehiculo(@PathVariable(value = "id") Long id,Map<String, Object> modelo,RedirectAttributes flash) {
		Vehiculo vehiculo = null;
		if(id > 0) {
			vehiculo = vehiculoService.findOne(id);
			if(vehiculo == null) {
				flash.addFlashAttribute("error", "El ID del vehiculo no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del vehiculo no puede ser cero");
			return "redirect:/listar";
		}
		
		modelo.put("vehiculo",vehiculo);
		modelo.put("titulo", "Edición de vehiculo");
		return "form";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarVehiculo(@PathVariable(value = "id") Long id,RedirectAttributes flash) {
		if(id > 0) {
			vehiculoService.delete(id);
			flash.addFlashAttribute("success", "Vehículo eliminado con exito");
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/exportarPDF")
	public void exportarListadoDeVehiculosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Vehículos" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Vehiculo> vehiculo = vehiculoService.findAll();
		
		VehiculoExporterPDF exporter = new VehiculoExporterPDF(vehiculo);
		exporter.exportar(response);
	}
}
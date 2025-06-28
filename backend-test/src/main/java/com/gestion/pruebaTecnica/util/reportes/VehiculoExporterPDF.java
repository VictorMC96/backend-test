package com.gestion.pruebaTecnica.util.reportes;

import com.gestion.pruebaTecnica.entidades.Vehiculo;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class VehiculoExporterPDF {

	private List<Vehiculo> listaVehiculos;

	public VehiculoExporterPDF(List<Vehiculo> listaVehiculos) {
		super();
		this.listaVehiculos = listaVehiculos;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("Numero de placa", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Tiempo estacionado (minutos)", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Cantidad a pagar", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Vehiculo vehiculo : listaVehiculos) {
			tabla.addCell(String.valueOf(vehiculo.getNumeroPlaca()));
			tabla.addCell(String.valueOf(vehiculo.getTiempo()));
			tabla.addCell(String.valueOf(vehiculo.getImporte()));
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.GRAY);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("Lista de pagos de vehículos estacionados", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(3);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 10f, 10f, 10f });
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}

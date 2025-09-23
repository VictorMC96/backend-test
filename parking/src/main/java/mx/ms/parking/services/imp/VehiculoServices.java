package mx.ms.parking.services.imp;

import mx.ms.parking.dao.IVehiculoDao;
import mx.ms.parking.entity.Vehiculo;
import mx.ms.parking.services.IVehiculoServices;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;

@Service
public class VehiculoServices implements IVehiculoServices {

    @Autowired
    private IVehiculoDao vehiculoDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    @Transactional
    public Vehiculo saveAndFlush(Vehiculo vehiculo) {
        return vehiculoDao.saveAndFlush(vehiculo);
    }

    @Override
    @Transactional(readOnly=true)
    public Vehiculo findByPlacas(String placas) {
        return vehiculoDao.findByPlaca(placas);
    }
    public void resetTiempoResidentes() {
        jdbcTemplate.execute("SELECT administration.reset_tiempo_residentes()");
    }
    @Override
    public void generarInformePagosResidentesExcel(String nombreArchivo) throws IOException {
        String sql = "SELECT * FROM administration.resumen_pagos_residentes()";

        try (org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            var sheet = workbook.createSheet("Pagos Residentes");

            // Estilo cabecera
            var headerStyle = workbook.createCellStyle();
            var font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Cabecera
            var header = sheet.createRow(0);
            String[] columnas = {"Núm. placa", "Tiempo estacionado (min.)", "Cantidad a pagar"};
            for (int i = 0; i < columnas.length; i++) {
                var cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Contenido
            final int[] filaIndex = {1};
            jdbcTemplate.query(sql, (rs) -> {
                while (rs.next()) {
                    var row = sheet.createRow(filaIndex[0]++);
                    row.createCell(0).setCellValue(rs.getString("placa"));
                    row.createCell(1).setCellValue(rs.getInt("tiempo_estacionado"));
                    row.createCell(2).setCellValue(rs.getDouble("cantidad_a_pagar"));
                }
            });

            // Ajuste columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (var fileOut = new java.io.FileOutputStream(nombreArchivo)) {
                workbook.write(fileOut);
            }
        }
    }
}

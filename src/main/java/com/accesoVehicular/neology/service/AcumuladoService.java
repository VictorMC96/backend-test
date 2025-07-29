package com.accesoVehicular.neology.service;


import com.accesoVehicular.neology.model.Acumulado;
import com.accesoVehicular.neology.model.Vehiculo;
import com.accesoVehicular.neology.repository.IAcumuladoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcumuladoService {

    private final IAcumuladoRepository acumuladoRepository;
    private final ReportService reportService;

    public AcumuladoService(IAcumuladoRepository acumuladoRepository, ReportService reportService) {
        this.acumuladoRepository = acumuladoRepository;
        this.reportService = reportService;
    }

    @Transactional
    public List<Acumulado> getAll() {
        return this.acumuladoRepository.findAll();
    }

    @Transactional
    public void registrarAcumulado(Vehiculo vehiculo, Long adicional) {
        Acumulado acumulado = this.acumuladoRepository.findByPlacaAndActivoTrue(vehiculo.getPlaca());
        if (acumulado == null) {
            acumulado = new Acumulado(vehiculo, adicional);
        } else {
            acumulado.setAcumulado((Long) acumulado.getAcumulado() + adicional);
        }
        this.acumuladoRepository.save(acumulado);
    }

    @Transactional
    public ResponseEntity<byte[]> obtenerPagosResidentes(String nombre_archivo) {
        List<Acumulado> acumulados = this.acumuladoRepository.findAllActive();

        if (acumulados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        byte[] reporte = this.reportService.generaReporte(acumulados);
        String fileName = nombre_archivo + ".txt";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        this.registrarReporte(acumulados);
        return new ResponseEntity<>(reporte, headers, HttpStatus.OK);
    }

    @Transactional
    public void registrarReporte(List<Acumulado> acumulados) {
        for( Acumulado acumulado : acumulados) {
            acumulado.setReportado(true);
        }
        this.acumuladoRepository.saveAllAndFlush(acumulados);
    }

    @Transactional
    public String resetAcumulados() throws Exception{
        List<Acumulado> acumulados = this.acumuladoRepository.findAllActive();
        if (acumulados.isEmpty()) {
            throw new Exception("No hay acumulados para reiniciar.");
        }

        List<Acumulado> noReportados = acumulados.stream().filter(acumulado -> acumulado.isReportado() != true).collect(Collectors.toList());
        if (!noReportados.isEmpty()) {
            throw new Exception("No se pueden reiniciar los acumulados que no han sido reportados.");
        }

        for (Acumulado acumulado : acumulados) {
            acumulado.setAcumulado(0L);
            acumulado.setReportado(false);
        }

        this.acumuladoRepository.saveAllAndFlush(acumulados);
        return "Acumulados reiniciados exitosamente.";
    }

}


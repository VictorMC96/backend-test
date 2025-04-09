package com.neology.assessment.java_backend.service;

import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.Estancia;
import com.neology.assessment.java_backend.exception.BusinessException;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

public interface EstanciaService {
    Estancia registarEntrada(VehiculoRequest vehiculoRequest) throws BusinessException;
    Estancia registrarSalida(VehiculoRequest vehiculoRequest) throws BusinessException;
    void iniciarMes();
    ByteArrayResource generarReporteResidentes() throws IOException;

}

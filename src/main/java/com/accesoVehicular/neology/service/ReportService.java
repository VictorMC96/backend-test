package com.accesoVehicular.neology.service;


import com.accesoVehicular.neology.model.Acumulado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    public byte[] generaReporte(List<Acumulado> acumulados) {
        StringBuilder sb = new StringBuilder();
        sb.append("Num placa\tTiempo estacionado\tCantidad a pagar\t");
        sb.append("\n");
        for (Acumulado registro : acumulados) {
            var porPagar = registro.getVehiculo().getTipoVehiculo().getTarifa() * registro.getAcumulado();
            sb.append(registro.getVehiculo().getPlaca() + "\t" +  registro.getAcumulado() + "\t" + porPagar);
            sb.append("\n");
        }
        return sb.toString().getBytes();
    }

}

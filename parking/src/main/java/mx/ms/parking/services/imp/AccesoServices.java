package mx.ms.parking.services.imp;

import mx.ms.parking.dao.IAccesoDao;
import mx.ms.parking.entity.Acceso;
import mx.ms.parking.services.IAccesoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
@Service
public class AccesoServices implements IAccesoServices {
    @Autowired
    private IAccesoDao accesoDao;

    @Override
    @Transactional
    public Acceso saveAndFlush(Acceso acceso) {
        return accesoDao.saveAndFlush(acceso);
    }

    @Override
    @Transactional(readOnly=true)
    public Acceso findByIdVehiculoAndFhEntrada(Long idVehiculo, Calendar fhEntrada) {
        return accesoDao.findByIdVehiculoAndFhEntrada(idVehiculo,fhEntrada);
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void eliminarAccesosOficiales() {
        jdbcTemplate.execute("SELECT administration.eliminar_accesos_oficiales()");
    }
}

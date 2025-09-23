package com.ar21.pruebatecnica.services;

import com.ar21.pruebatecnica.entities.EntradaSalida;
import com.ar21.pruebatecnica.entities.Vehiculo;
import com.ar21.pruebatecnica.exceptions.PlacaEncontradaException;
import com.ar21.pruebatecnica.exceptions.PlacaNoEncontradaException;
import com.ar21.pruebatecnica.models.EntradaSalidaModel;
import com.ar21.pruebatecnica.models.VehiculoModel;
import com.ar21.pruebatecnica.repositories.EntradaSalidaRepository;
import com.ar21.pruebatecnica.repositories.VehiculoRepository;
import com.ar21.pruebatecnica.utils.TipoMovimiento;
import com.ar21.pruebatecnica.utils.TipoVehiculo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VehiculoServiceImpl implements VehiculoService{

    private final VehiculoRepository vehiculoRepository;
    private final EntradaSalidaRepository entradaSalidaRepository;

public  VehiculoServiceImpl(VehiculoRepository vehiculoRepository, EntradaSalidaRepository entradaSalidaRepository){
    this.vehiculoRepository=vehiculoRepository;
    this.entradaSalidaRepository= entradaSalidaRepository;
    }

    @Override
    public VehiculoModel registrarVehiculo(VehiculoModel vehiculo) {

    Vehiculo v1= new Vehiculo();

     boolean  validar=  vehiculoRepository.existsByPlaca(vehiculo.getPlaca());

     if(!validar){
         v1.setPlaca(vehiculo.getPlaca());
         v1.setTipoVehiculo(vehiculo.getTipoVehiculo());
         v1.setFechaRegistro(LocalDateTime.now());
         vehiculoRepository.save(v1);
     }else {
            throw new PlacaEncontradaException("========= La placa se encuentra registrada ===========");
     }


       VehiculoModel response= new VehiculoModel();
                response.setTipoVehiculo(v1.getTipoVehiculo());
                response.setPlaca(v1.getPlaca());
                response.setFechaRegistro(v1.getFechaRegistro());
        return response;
    }

    @Override
    @Transactional
    public EntradaSalidaModel registrarEntrada(EntradaSalidaModel entradaRequest) {

        Optional<Vehiculo> buscarVehiculo= vehiculoRepository.findByPlaca (entradaRequest.getPlaca());

        EntradaSalidaModel entradaResponse= new EntradaSalidaModel();
        if(buscarVehiculo.isPresent()){
            entradaResponse.setFechaHoraEntrada(LocalDateTime.now());
            entradaResponse.setPlaca(entradaRequest.getPlaca());
            entradaResponse.setTipoMovimiento(entradaRequest.getTipoMovimiento());

            EntradaSalida entrada= new EntradaSalida();
            entrada.setVehiculo(buscarVehiculo.get());
            entrada.setFechaHoraEntrada(entradaResponse.getFechaHoraEntrada());
            entrada.setPlaca(entradaResponse.getPlaca());
            entrada.setTipoMovimiento(TipoMovimiento.ENTRADA.getValue());

            entradaSalidaRepository.save(entrada);
            return entradaResponse;

        }else {
            throw new PlacaNoEncontradaException("  =========== El vehiculo no se encuentra registrado !!! ======== ");
        }


    }
    @Override
    public EntradaSalidaModel registarSalida(String placa) {

        Optional <VehiculoModel> buscarVehiculo=   buscarVehiculoPorPlaca(placa);
        EntradaSalidaModel salidaModel= new EntradaSalidaModel();
        if(buscarVehiculo.isPresent()){

          Optional<EntradaSalida> buscandoVehiculoEnEntradas= entradaSalidaRepository.findByPlaca(placa);
            EntradaSalida salida= new EntradaSalida();

            if (buscandoVehiculoEnEntradas.isPresent()){

           if(buscandoVehiculoEnEntradas.get().getVehiculo().getTipoVehiculo().equals(TipoVehiculo.OFICIAL.getValue())){


               salidaModel.setFechaHoraEntrada(buscandoVehiculoEnEntradas.get().getFechaHoraEntrada());
               salidaModel.setFechaHoraSalida(LocalDateTime.now());
               salidaModel.setPlaca(buscandoVehiculoEnEntradas.get().getPlaca());
               salidaModel.setTipoMovimiento(TipoMovimiento.SALIDA.getValue());

               salida.setFechaHoraEntrada(salidaModel.getFechaHoraEntrada());
               salida.setPlaca(salidaModel.getPlaca());
               salida.setFechaHoraSalida(LocalDateTime.now());
               salida.setVehiculo(buscandoVehiculoEnEntradas.get().getVehiculo());
               salida.setTipoMovimiento(salidaModel.getTipoMovimiento());
               entradaSalidaRepository.save(salida);



           }
               else if(buscandoVehiculoEnEntradas.get().getVehiculo().getTipoVehiculo().equals(TipoVehiculo.RESIDENTE.getValue())){

                    salidaModel.setEstanciaTotalAcumulado(salidaModel.calcularTiempo(buscandoVehiculoEnEntradas.get().getFechaHoraEntrada(), LocalDateTime.now()));
                    salidaModel.setFechaHoraSalida(LocalDateTime.now());
                    salidaModel.setPlaca(buscandoVehiculoEnEntradas.get().getVehiculo().getPlaca());
                    salidaModel.setTipoMovimiento(TipoMovimiento.SALIDA.getValue());
                    salidaModel.setFechaHoraEntrada(buscandoVehiculoEnEntradas.get().getFechaHoraEntrada());


                    salida.setPlaca(salidaModel.getPlaca());
                    salida.setFechaHoraSalida(LocalDateTime.now());
                    salida.setFechaHoraEntrada(salidaModel.getFechaHoraEntrada());
                    salida.setVehiculo(buscandoVehiculoEnEntradas.get().getVehiculo());
                    salida.setTipoMovimiento(salidaModel.getTipoMovimiento());
                    entradaSalidaRepository.save(salida);


                }

               else {
                   salidaModel.setFechaHoraSalida(LocalDateTime.now());
                   salidaModel.setPlaca(buscandoVehiculoEnEntradas.get().getVehiculo().getPlaca());
                   salidaModel.setTipoMovimiento(TipoMovimiento.SALIDA.getValue());
                   salidaModel.setFechaHoraEntrada(buscandoVehiculoEnEntradas.get().getFechaHoraEntrada());

                   salidaModel.setImporteAPagar(TipoMovimiento.TOTAL_PAGAR.getValue());
                   salida.setPlaca(salidaModel.getPlaca());
                   salida.setFechaHoraSalida(LocalDateTime.now());
                   salida.setFechaHoraEntrada(salidaModel.getFechaHoraEntrada());
                   salida.setFechaHoraSalida(salidaModel.getFechaHoraSalida());
                   salida.setVehiculo(buscandoVehiculoEnEntradas.get().getVehiculo());
               entradaSalidaRepository.save(salida);


           }

          }

        }else {

            throw new PlacaNoEncontradaException("============ Placa no encontrada ==============");
        }
return salidaModel;
    }

    @Override
    public  Optional<VehiculoModel>  buscarVehiculoPorPlaca(String placa) {
        Optional<Vehiculo> vehiculo= vehiculoRepository.findByPlaca(placa);

        if(vehiculo.isPresent()){

            VehiculoModel vehiculoModel= new VehiculoModel();
            vehiculoModel.setPlaca(vehiculo.get().getPlaca());
            vehiculoModel.setTipoVehiculo(vehiculo.get().getTipoVehiculo());
            vehiculoModel.setFechaRegistro(vehiculo.get().getFechaRegistro());

            return Optional.of(vehiculoModel);
        }else {
            throw new PlacaNoEncontradaException("Error el usuario no existe!");
        }


    }




}

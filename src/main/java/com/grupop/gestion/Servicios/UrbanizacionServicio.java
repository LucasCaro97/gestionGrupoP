package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Urbanizacion;
import com.grupop.gestion.Repositorios.UrbanizacionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrbanizacionServicio {

    private final UrbanizacionRepo urbanizacionRepo;

    @Transactional
    public void crear(Urbanizacion dto){
        Urbanizacion urb = new Urbanizacion();
        urb.setDescripcion(dto.getDescripcion());
        urb.setNroPlano(dto.getNroPlano());
        urb.setNroPartida(dto.getNroPartida());
        urb.setSuperficieM2(dto.getSuperficieM2());
        urb.setUbicacion(dto.getUbicacion());
        urb.setCiudad(dto.getCiudad());
        urb.setCuenta(dto.getCuenta());
        urb.setLinkMapa(dto.getLinkMapa());
        urbanizacionRepo.save(urb);
    }

    @Transactional
    public void actualizar(Urbanizacion dto){
        Urbanizacion urb = urbanizacionRepo.findById(dto.getId()).get();
        urb.setDescripcion(dto.getDescripcion());
        urb.setNroPlano(dto.getNroPlano());
        urb.setNroPartida(dto.getNroPartida());
        urb.setSuperficieM2(dto.getSuperficieM2());
        urb.setUbicacion(dto.getUbicacion());
        urb.setCiudad(dto.getCiudad());
        urb.setCuenta(dto.getCuenta());
        urb.setLinkMapa(dto.getLinkMapa());
        urbanizacionRepo.save(urb);
    }


    @Transactional(readOnly = true)
    public List<Urbanizacion> obtenerTodos(){ return urbanizacionRepo.findAll();}



    @Transactional(readOnly = true)
    public Urbanizacion obtenerPorId(Long id){ return urbanizacionRepo.findById(id).get();}

    @Transactional
    public void eliminarPorId(Long id){ urbanizacionRepo.deleteById(id);}


    public CuentasContables obtenerCuenta(Urbanizacion urbanizacion) {
        return urbanizacionRepo.findCuentaContable(urbanizacion.getId());
    }
    @Transactional(readOnly = true)
    public String obtenerLinkMapa(Long idUrb) {
        return urbanizacionRepo.obtenerLinkMapa(idUrb);
    }
}

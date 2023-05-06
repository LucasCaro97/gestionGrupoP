package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Repositorios.EntidadBaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntidadBaseServicio {

    private final EntidadBaseRepo entidadBaseRepo;

    @Transactional
    public void crear(EntidadBase dto){
        EntidadBase ent = new EntidadBase();
        ent.setRazonSocial(dto.getRazonSocial());
        ent.setCuit(dto.getCuit());
        ent.setNombreFantasia(dto.getNombreFantasia());
        ent.setTipoIva(dto.getTipoIva());
        ent.setCliente(dto.getCliente());
        ent.setProveedor(dto.getProveedor());
        ent.setEmpleado(dto.getEmpleado());
        ent.setDomicilio(dto.getDomicilio());
        ent.setTelefono(dto.getTelefono());
        ent.setCorreo(dto.getCorreo());
        entidadBaseRepo.save(ent);
    }

    @Transactional
    public void actualizar(EntidadBase dto){
        EntidadBase ent = entidadBaseRepo.findById(dto.getId()).get();
        ent.setRazonSocial(dto.getRazonSocial());
        ent.setCuit(dto.getCuit());
        ent.setNombreFantasia(dto.getNombreFantasia());
        ent.setTipoIva(dto.getTipoIva());
        ent.setCliente(dto.getCliente());
        ent.setProveedor(dto.getProveedor());
        ent.setEmpleado(dto.getEmpleado());
        ent.setDomicilio(dto.getDomicilio());
        ent.setTelefono(dto.getTelefono());
        ent.setCorreo(dto.getCorreo());
        ent.setFechaModificacion(LocalDate.now());
        entidadBaseRepo.save(ent);
    }

    @Transactional(readOnly = true)
    public List<EntidadBase> obtenerTodos(){ return entidadBaseRepo.findAll(); }

    @Transactional(readOnly = true)
    public EntidadBase buscarPorId(Long id){ return entidadBaseRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ entidadBaseRepo.deleteById(id); }
}

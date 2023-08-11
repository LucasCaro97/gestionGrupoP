package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cheque;
import com.grupop.gestion.Repositorios.ChequeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChequeServicio {

    private final ChequeRepo chequeRepo;


    @Transactional
    public void crear (Cheque dto, String fechaEmision, String fechaPago, String fechaRecepcion){
        Cheque c = new Cheque();
        c.setFechaEmision(LocalDate.parse(fechaEmision));
        c.setSerie(dto.getSerie());
        c.setNroCheque(dto.getNroCheque());
        c.setFechaPago(LocalDate.parse(fechaPago));
        c.setBancoEmisor(dto.getBancoEmisor());
        c.setNroIdentificacion(dto.getNroIdentificacion());
        c.setTitularDeLaCuenta(dto.getTitularDeLaCuenta());
        c.setCiudad(dto.getCiudad());
        c.setProvincia(dto.getProvincia());

        c.setFechaRecepcion(LocalDate.parse(fechaRecepcion));
        c.setOperacionIngreso(dto.getOperacionIngreso());
        c.setEsTitular(dto.isEsTitular());
        c.setNombreEmisor(dto.getNombreEmisor());
        c.setImporte(dto.getImporte());
        chequeRepo.save(c);
    }

    @Transactional
    public void actualizar(Cheque dto, String fechaEmision, String fechaPago, String fechaRecepcion){
        Cheque c = chequeRepo.findById(dto.getId()).get();
        c.setFechaEmision(LocalDate.parse(fechaEmision));
        c.setSerie(dto.getSerie());
        c.setNroCheque(dto.getNroCheque());
        c.setFechaPago(LocalDate.parse(fechaPago));
        c.setBancoEmisor(dto.getBancoEmisor());
        c.setNroIdentificacion(dto.getNroIdentificacion());
        c.setTitularDeLaCuenta(dto.getTitularDeLaCuenta());
        c.setCiudad(dto.getCiudad());
        c.setProvincia(dto.getProvincia());

        c.setFechaRecepcion(LocalDate.parse(fechaRecepcion));
        c.setOperacionIngreso(dto.getOperacionIngreso());
        c.setEsTitular(dto.isEsTitular());
        c.setNombreEmisor(dto.getNombreEmisor());
        c.setImporte(dto.getImporte());
        chequeRepo.save(c);
    }

    @Transactional(readOnly = true)
    public Cheque obtenerPorId(Long id){    return chequeRepo.findById(id).get();   }

    @Transactional(readOnly = true)
    public List<Cheque> obtenerTodos(){ return chequeRepo.findAll(); }

    @Transactional
    public void eliminarPorId(Long id){
        chequeRepo.deleteById(id);
    }


}

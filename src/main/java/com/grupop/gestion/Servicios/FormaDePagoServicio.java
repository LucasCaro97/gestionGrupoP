package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Repositorios.FormaDePagoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaDePagoServicio {

    private final FormaDePagoRepo formaDePagoRepo;

    @Transactional
    public void crear(FormaDePago dto){
        FormaDePago formaDePago = new FormaDePago();
        formaDePago.setDescripcion(dto.getDescripcion());
        formaDePagoRepo.save(formaDePago);
    }

    @Transactional
    public void actualizar(FormaDePago dto){
        FormaDePago formaDePago = formaDePagoRepo.findById(dto.getId()).get();
        formaDePago.setDescripcion(dto.getDescripcion());
        formaDePagoRepo.save(formaDePago);
    }

    @Transactional(readOnly = true)
    public List<FormaDePago> obtenerTodos(){
        return formaDePagoRepo.findAll();
    }
    @Transactional(readOnly = true)
    public FormaDePago obtenerPorId(Long id) {      return formaDePagoRepo.findById(id).get() ; }
    @Transactional
    public void elimianrPorId(Long id){     formaDePagoRepo.deleteById(id); }
}

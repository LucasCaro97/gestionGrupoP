package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.TipoPago;
import com.grupop.gestion.Repositorios.FormaDePagoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaDePagoServicio {

    private final FormaDePagoRepo formaDePagoRepo;


    public void crear(FormaDePago dto){
        FormaDePago fdp = new FormaDePago();
        fdp.setDescripcion(dto.getDescripcion());
        fdp.setTipoPago(dto.getTipoPago());
        fdp.setTipoOperacion(dto.getTipoOperacion());
        fdp.setAbreviatura(dto.getAbreviatura());
        fdp.setCuenta(dto.getCuenta());
        fdp.setMoneda(dto.getMoneda());
        fdp.setBancoTarjeta(dto.getBancoTarjeta());
        formaDePagoRepo.save(fdp);
    }

    public void actualizar(FormaDePago dto){
        FormaDePago fdp = formaDePagoRepo.findById(dto.getId()).get();
        fdp.setDescripcion(dto.getDescripcion());
        fdp.setTipoPago(dto.getTipoPago());
        fdp.setTipoOperacion(dto.getTipoOperacion());
        fdp.setAbreviatura(dto.getAbreviatura());
        fdp.setCuenta(dto.getCuenta());
        fdp.setMoneda(dto.getMoneda());
        fdp.setBancoTarjeta(dto.getBancoTarjeta());
        formaDePagoRepo.save(fdp);
    }


    public List<FormaDePago> obtenerTodos(){ return formaDePagoRepo.findAll(); }

    public FormaDePago obtenerPorId(Long id){ return formaDePagoRepo.findById(id).get(); }

    public void eliminarPorId(Long id){ formaDePagoRepo.deleteById(id);}

    public List<FormaDePago> obtenerTodosPorOperacion(Long idOperacion) { return formaDePagoRepo.obtenerTodosPorOperacion(idOperacion);    }
}

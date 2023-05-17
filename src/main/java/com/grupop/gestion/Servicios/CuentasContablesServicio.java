package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Repositorios.CuentasContablesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentasContablesServicio {

    private final CuentasContablesRepo cuentasContablesRepo;

    @Transactional
    public void crear(CuentasContables dto){
        CuentasContables cta = new CuentasContables();
        cta.setDescripcion(dto.getDescripcion());
        cta.setClasificacionCta(dto.getClasificacionCta());
        cta.setCtaTotalizadora(dto.getCtaTotalizadora());
        cta.setMoneda(dto.getMoneda());
        cta.setCodigo(dto.getCodigo());
        cta.setImpuestos(dto.getImpuestos());
        cuentasContablesRepo.save(cta);
        System.out.println(cta.getImpuestos());
    }

    @Transactional
    public void actualizar(CuentasContables dto){
        CuentasContables cta = cuentasContablesRepo.findById(dto.getId()).get();
        cta.setDescripcion(dto.getDescripcion());
        cta.setClasificacionCta(dto.getClasificacionCta());
        cta.setCtaTotalizadora(dto.getCtaTotalizadora());
        cta.setMoneda(dto.getMoneda());
        cta.setCodigo(dto.getCodigo());
        cta.setImpuestos(dto.getImpuestos());
        cuentasContablesRepo.save(cta);
        System.out.println(cta.getImpuestos());
    }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerTodos(){ return cuentasContablesRepo.findAll(); }
    @Transactional(readOnly = true)
    public CuentasContables obtenerPorid(Long id) { return cuentasContablesRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id) { cuentasContablesRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public List<CuentasContables> obtenerCuentas1111(){
        return cuentasContablesRepo.obtenerCuentas1111();
    }
}

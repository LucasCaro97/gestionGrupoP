package com.grupop.gestion.Servicios;

import com.fasterxml.jackson.core.io.schubfach.DoubleToDecimal;
import com.grupop.gestion.Entidades.CuentasTotalizadoras;
import com.grupop.gestion.Repositorios.CuentasTotalizadorasRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentasTotalizadorasServicio {

    private final CuentasTotalizadorasRepo cuentasTotalizadorasRepo;

    @Transactional
    public void crear(CuentasTotalizadoras dto){
        CuentasTotalizadoras cta = new CuentasTotalizadoras();
        cta.setDescripcion(dto.getDescripcion());
        cta.setTipoTotalizadora(dto.getTipoTotalizadora());
        cta.setNivel(dto.getNivel());
        cta.setPadre(dto.getPadre());
//        cta.setNroCta(insertarNroCt());
        cuentasTotalizadorasRepo.save(cta);
        generarNomenclatura();
    }


    @Transactional
    public void actualizar(CuentasTotalizadoras dto){
        CuentasTotalizadoras cta = cuentasTotalizadorasRepo.findById(dto.getId()).get();
        cta.setDescripcion(dto.getDescripcion());
        cta.setTipoTotalizadora(dto.getTipoTotalizadora());
//        cta.setPadre(dto.getPadre());
//        cta.setNomenclatura(dto.getNomenclatura()); // CREAR METODO QUE CREE EL NIVEL DE JERARQUIAS COMPLETO
        cuentasTotalizadorasRepo.save(cta);
    }

    @Transactional(readOnly = true)
    public List<CuentasTotalizadoras> obtenerTodos(){ return cuentasTotalizadorasRepo.findAll(); }

    @Transactional(readOnly = true)
    public CuentasTotalizadoras obtenerPorId(Long id){ return  cuentasTotalizadorasRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ cuentasTotalizadorasRepo.deleteById(id);}

    public void generarNomenclatura(){
        CuentasTotalizadoras cta = cuentasTotalizadorasRepo.obtenerUltimoRegistro();
        String nomenclaturaPadre = cuentasTotalizadorasRepo.obtenerNomenclaturaPadre(cta.getPadre());

        if(nomenclaturaPadre==null){
            cta.setNomenclatura(cta.getId().toString());
        }else{
            cta.setNomenclatura( nomenclaturaPadre+"."+ cta.getId());
        }
        cuentasTotalizadorasRepo.save(cta);
    }

}


package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.IndiceCAC;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Repositorios.IndiceCacRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndiceCacServicio {

    private final IndiceCacRepo indiceCacRepo;
    private final VentaServicio ventaServicio;

    @Transactional
    public void crear(IndiceCAC dto){
        IndiceCAC c = new IndiceCAC();
        c.setMes(dto.getMes());
        System.out.println("Mes: " + dto.getMes());
        c.setAnio(dto.getAnio());
        System.out.println("Anio: " + dto.getAnio());
        c.setIndice(dto.getIndice());
        System.out.println("Indice: " + dto.getIndice());
        indiceCacRepo.save(c);
    }

    @Transactional
    public void actualizar(IndiceCAC dto){
        IndiceCAC c = indiceCacRepo.findById(dto.getId()).get();
        c.setMes(dto.getMes());
        c.setAnio(dto.getAnio());
        c.setIndice(dto.getIndice());
        indiceCacRepo.save(c);
    }

    @Transactional(readOnly = true)
    public IndiceCAC obtenerPorId(Long id){
        return indiceCacRepo.findById(id).get();
    }
    @Transactional(readOnly = true)
    public List<IndiceCAC> obtenerTodos(){
        return indiceCacRepo.findAll();
    }
    @Transactional
    public void eliminarPorId(Long id){ indiceCacRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public BigDecimal obtenerIndiceBase(Integer mes, Integer anio) {
        return indiceCacRepo.obtenerIndiceBase(mes,anio);
    }
}

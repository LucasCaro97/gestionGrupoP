package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.PlanPago;
import com.grupop.gestion.Repositorios.PlanPagoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanPagoServicio {

    private final PlanPagoRepo planPagoRepo;

    @Transactional
    public void crear(PlanPago dto){
        PlanPago p = new PlanPago();
        p.setDescripcion(dto.getDescripcion());
        p.setTasaInteresTotal(dto.getTasaInteresTotal());
        p.setCantCuota(dto.getCantCuota());
        p.setGastosAdministrativos(dto.getGastosAdministrativos());
        p.setPorcentajeGastos(dto.getPorcentajeGastos());
        p.setDiasDeGracia(dto.getDiasDeGracia());
        p.setInteresPunitorio(dto.getInteresPunitorio());
        p.setImporteMinimo(dto.getImporteMinimo());
        p.setImporteMaximo(dto.getImporteMaximo());
        p.setVenceLosDias(dto.getVenceLosDias());
        p.setTablaCac(dto.getTablaCac());
        planPagoRepo.save(p);

    }

    @Transactional
    public void actualizar(PlanPago dto){
        PlanPago p = planPagoRepo.findById(dto.getId()).get();
        p.setDescripcion(dto.getDescripcion());
        p.setTasaInteresTotal(dto.getTasaInteresTotal());
        p.setCantCuota(dto.getCantCuota());
        p.setGastosAdministrativos(dto.getGastosAdministrativos());
        p.setPorcentajeGastos(dto.getPorcentajeGastos());
        p.setDiasDeGracia(dto.getDiasDeGracia());
        p.setInteresPunitorio(dto.getInteresPunitorio());
        p.setImporteMinimo(dto.getImporteMinimo());
        p.setImporteMaximo(dto.getImporteMaximo());
        p.setVenceLosDias(dto.getVenceLosDias());
        p.setTablaCac(dto.getTablaCac());
        planPagoRepo.save(p);
    }

    @Transactional(readOnly = true)
    public List<PlanPago> obtenerTodos(){ return planPagoRepo.findAll(); }

    @Transactional(readOnly = true)
    public PlanPago obtenerPorId(Long id){
        return planPagoRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ planPagoRepo.deleteById(id);}


}


package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.ClasificacionCta;
import com.grupop.gestion.Repositorios.ClasificacionCtaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClasifiacionCtaServicio {

    private final ClasificacionCtaRepo clasificacionCtaRepo;

    @Transactional
    public void crear(ClasificacionCta dto){
        ClasificacionCta cta = new ClasificacionCta();
        cta.setDescripcion(dto.getDescripcion());
        clasificacionCtaRepo.save(cta);
    }

    @Transactional
    public void actualizar(ClasificacionCta dto){
        ClasificacionCta cta = clasificacionCtaRepo.findById(dto.getId()).get();
        cta.setDescripcion(dto.getDescripcion());
        clasificacionCtaRepo.save(cta);
    }

    @Transactional(readOnly = true)
    public List<ClasificacionCta> obtenerTodos(){ return clasificacionCtaRepo.findAll();    }

    @Transactional(readOnly = true)
    public ClasificacionCta obtenerPorId(Long id) { return clasificacionCtaRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id) { clasificacionCtaRepo.deleteById(id); }

}


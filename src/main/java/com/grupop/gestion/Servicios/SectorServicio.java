package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Entidades.Sector;
import com.grupop.gestion.Repositorios.PuestoRepo;
import com.grupop.gestion.Repositorios.SectorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorServicio {
    private final SectorRepo sectorRepo;
    @Transactional
    public void crear(Sector dto){
        Sector sector = new Sector();
        sector.setDescripcion(dto.getDescripcion());
        sectorRepo.save(sector);
    }

    @Transactional
    public void actualizar(Sector dto){
        Sector sector = sectorRepo.findById(dto.getId()).get();
        sector.setDescripcion(dto.getDescripcion());
        sectorRepo.save(sector);
    }
    @Transactional(readOnly = true)
    public List<Sector> obtenerTodos(){
        return sectorRepo.findAll();
    }

    @Transactional
    public void eliminarPorId(Long id){ sectorRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public Sector buscarPorId(Long id){ return sectorRepo.findById(id).get(); }

}

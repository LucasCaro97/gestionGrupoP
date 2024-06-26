package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.ManzanaAltDTO;
import com.grupop.gestion.Entidades.Manzana;
import com.grupop.gestion.Repositorios.ManzanaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManzanaServicio {

    private final ManzanaRepo manzanaRepo;
    private final UrbanizacionServicio urbanizacionServicio;


    @Transactional
    public void crear(Manzana dto){
        Manzana manzana = new Manzana();
        manzana.setDescripcion(dto.getDescripcion());
        manzana.setUrbanizacion(dto.getUrbanizacion());
        manzana.setUltimoNroLote(0);
        manzanaRepo.save(manzana);
    }

    @Transactional
    public void crear(ManzanaAltDTO dto){
        Manzana manzana = new Manzana();
        manzana.setDescripcion(dto.getDescripcion());
        manzana.setUrbanizacion(urbanizacionServicio.obtenerPorId(dto.getIdUrbanizacion()));
        manzana.setUltimoNroLote(0);
        manzanaRepo.save(manzana);
    }

    @Transactional
    public void actualizar(Manzana dto){
        Manzana manzana = manzanaRepo.findById(dto.getId()).get();
        manzana.setDescripcion(dto.getDescripcion());
        manzana.setUrbanizacion(dto.getUrbanizacion());
        manzanaRepo.save(manzana);
    }

    @Transactional(readOnly = true)
    public List<Manzana> obtenerTodos(){
        return manzanaRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Manzana> obtenerTodos(Long manzana){
        if(manzana!=null){
            return manzanaRepo.searchByUrbanizacion(manzana);
        }else{
            return manzanaRepo.findAll(); }
        }


    @Transactional(readOnly = true  )
    public Manzana obtenerPorId(Long id){ return manzanaRepo.findById(id).get();}

    @Transactional
    public void eliminarPorId(Long id){ manzanaRepo.deleteById(id);}


    public List<Manzana> obtenerPorUrbanizacion(Long id) { return manzanaRepo.obtenerPorUrb(id);}

    public Integer asignarUltimoNroLote(Long idManzana){
        Manzana m = manzanaRepo.findById(idManzana).get();
        Integer nroLote = m.getUltimoNroLote() + 1;
        m.setUltimoNroLote(nroLote);
        manzanaRepo.save(m);
        return  nroLote;
    }

}

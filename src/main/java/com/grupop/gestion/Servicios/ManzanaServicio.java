package com.grupop.gestion.Servicios;

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


    @Transactional
    public void crear(Manzana dto){
        Manzana manzana = new Manzana();
        manzana.setDescripcion(dto.getDescripcion());
        manzanaRepo.save(manzana);
    }

    @Transactional
    public void actualizar(Manzana dto){
        Manzana manzana = manzanaRepo.findById(dto.getId()).get();
        manzana.setDescripcion(dto.getDescripcion());
        manzanaRepo.save(manzana);
    }

    @Transactional(readOnly = true)
    public List<Manzana> obtenerTodos(Long id){ return manzanaRepo.findAll(); }

    @Transactional(readOnly = true)
    public Manzana obtenerPorId(Long id){ return manzanaRepo.findById(id).get();}

    @Transactional
    public void eliminarPorId(Long id){ manzanaRepo.deleteById(id);}
}

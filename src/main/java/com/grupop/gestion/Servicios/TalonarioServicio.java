package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Talonario;
import com.grupop.gestion.Repositorios.TalonarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TalonarioServicio {

    private final TalonarioRepo talonarioRepo;

    @Transactional
    public void crear (Talonario dto){
        Talonario tl = new Talonario();
        tl.setNroTalonario(dto.getNroTalonario());
        tl.setDescripcion(dto.getDescripcion());
        tl.setUltimoNro(0l);
        talonarioRepo.save(tl);
    }

    @Transactional
    public void actualizar (Talonario dto){
        Talonario tl = talonarioRepo.findById(dto.getId()).get();
        tl.setNroTalonario(dto.getNroTalonario());
        tl.setDescripcion(dto.getDescripcion());
        talonarioRepo.save(tl);
    }

    @Transactional(readOnly = true)
    public List<Talonario> obtenerTodos(){ return talonarioRepo.findAll(); }

    @Transactional(readOnly = true)
    public Talonario obtenerPorId(Long id){ return talonarioRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ talonarioRepo.deleteById(id);}

}

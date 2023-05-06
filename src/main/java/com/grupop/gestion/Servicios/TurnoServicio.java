package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Entidades.Turno;
import com.grupop.gestion.Repositorios.PuestoRepo;
import com.grupop.gestion.Repositorios.TurnoRepo;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoServicio {

    private final TurnoRepo turnoRepo;

    @Transactional
    public void crear(Turno dto){
        Turno turno = new Turno();
        turno.setDescripcion(dto.getDescripcion());
        turno.setHsEntrada(dto.getHsEntrada());
        turno.setHsSalida(dto.getHsSalida());
        turno.setHsEntradaReceso(dto.getHsEntradaReceso());
        turno.setHsSalidaReceso(dto.getHsSalidaReceso());
        turnoRepo.save(turno);
    }

    @Transactional
    public void actualizar(Turno dto){
        Turno turno = turnoRepo.findById(dto.getId()).get();
        turno.setDescripcion(dto.getDescripcion());
        turno.setHsEntrada(dto.getHsEntrada());
        turno.setHsSalida(dto.getHsSalida());
        turno.setHsEntradaReceso(dto.getHsEntradaReceso());
        turno.setHsSalidaReceso(dto.getHsSalidaReceso());
        turnoRepo.save(turno);
    }
    @Transactional(readOnly = true)
    public List<Turno> obtenerTodos(){
        return turnoRepo.findAll();
    }

    @Transactional
    public void eliminarPorId(Long id){ turnoRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public Turno buscarPorId(Long id){ return turnoRepo.findById(id).get(); }

}
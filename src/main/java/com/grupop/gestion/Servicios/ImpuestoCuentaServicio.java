package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.ImpuestoCuenta;
import com.grupop.gestion.Repositorios.ImpuestoCuentaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImpuestoCuentaServicio {

    private final ImpuestoCuentaRepo impuestoCuentaRepo;

    @Transactional
    public void crear(ImpuestoCuenta dto){
        ImpuestoCuenta imp = new ImpuestoCuenta();
        imp.setDescripcion(dto.getDescripcion());
        imp.setPorcentaje(dto.getPorcentaje());
        impuestoCuentaRepo.save(imp);
    }

    @Transactional
    public void actualizar(ImpuestoCuenta dto){
        ImpuestoCuenta imp = impuestoCuentaRepo.findById(dto.getId()).get();
        imp.setDescripcion(dto.getDescripcion());
        imp.setPorcentaje(dto.getPorcentaje());
        impuestoCuentaRepo.save(imp);
    }

    @Transactional(readOnly = true)
    public List<ImpuestoCuenta> obtenerTodos(){ return impuestoCuentaRepo.findAll(); }

    @Transactional(readOnly = true)
    public ImpuestoCuenta obtenerPorId(Long id){ return impuestoCuentaRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ impuestoCuentaRepo.deleteById(id);}

}

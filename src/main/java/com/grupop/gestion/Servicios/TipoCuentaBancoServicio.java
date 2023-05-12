package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoCuentaBanco;
import com.grupop.gestion.Repositorios.TipoCuentaBancoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCuentaBancoServicio {

    private final TipoCuentaBancoRepo tipoCuentaBancoRepo;

    @Transactional
    public void crear(TipoCuentaBanco dto){
        TipoCuentaBanco tipoCuentaBanco = new TipoCuentaBanco();
        tipoCuentaBanco.setDescripcion(dto.getDescripcion());
        tipoCuentaBancoRepo.save(dto);
    }
    @Transactional
    public void actualizar(TipoCuentaBanco dto){
        TipoCuentaBanco tipoCuentaBanco = tipoCuentaBancoRepo.findById(dto.getId()).get();
        tipoCuentaBanco.setDescripcion(dto.getDescripcion());
        tipoCuentaBancoRepo.save(tipoCuentaBanco);
    }

    @Transactional(readOnly = true)
    public List<TipoCuentaBanco> obtenerTodos(){ return tipoCuentaBancoRepo.findAll();    }

    @Transactional(readOnly = true)
    public TipoCuentaBanco obtenerPorId(Long id){ return tipoCuentaBancoRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ tipoCuentaBancoRepo.deleteById(id); }
}

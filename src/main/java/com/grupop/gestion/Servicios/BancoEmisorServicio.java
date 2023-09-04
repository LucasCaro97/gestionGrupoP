package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.BancoEmisor;
import com.grupop.gestion.Repositorios.BancoEmisorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BancoEmisorServicio {

    private final BancoEmisorRepo bancoEmisorRepo;

    @Transactional
    public void crear(BancoEmisor dto){
        BancoEmisor b = new BancoEmisor();
        b.setDescripcion(dto.getDescripcion());
        bancoEmisorRepo.save(b);
    }

    @Transactional
    public void actualizar(BancoEmisor dto){
        BancoEmisor b = bancoEmisorRepo.findById(dto.getId()).get();
        b.setDescripcion(dto.getDescripcion());
        bancoEmisorRepo.save(b);
    }

    @Transactional(readOnly = true)
    public List<BancoEmisor> obtenerTodos(){
        return bancoEmisorRepo.findAll();
    }

    @Transactional(readOnly = true)
    public BancoEmisor obtenerPorId(Long id){ return bancoEmisorRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ bancoEmisorRepo.deleteById(id); }


}

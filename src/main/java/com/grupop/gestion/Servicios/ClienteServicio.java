package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Repositorios.ClienteRepo;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServicio {

        private final ClienteRepo clienteRepo;

        @Transactional
        public void crear(){
                Cliente cliente = new Cliente();
                clienteRepo.save(cliente);
        }

        @Transactional
        public void eliminarPorId(Long id){ clienteRepo.findById(id);   }

        @Transactional(readOnly = true)
        public Long buscarUltimoId(){ return clienteRepo.findLastId(); }

        @Transactional(readOnly = true)
        public Cliente buscarPorId(Long id){ return clienteRepo.findById(id).get();}

}

package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Repositorios.ClienteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServicio {

        private final ClienteRepo clienteRepo;

        @Transactional
        public void crear(){
                Cliente cli = new Cliente();
                clienteRepo.save(cli);
        }

        @Transactional
        public void eliminarPorId(Long id){ clienteRepo.findById(id);   }
}

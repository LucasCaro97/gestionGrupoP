package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Repositorios.ClienteRepo;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServicio {

        private final ClienteRepo clienteRepo;

        @Transactional
        public void crear(){
                Cliente cliente = new Cliente();
                cliente.setSaldoAFavor(new BigDecimal(0));
                clienteRepo.save(cliente);
        }

        @Transactional
        public void eliminarPorId(Long id){ clienteRepo.findById(id);   }

        @Transactional(readOnly = true)
        public Long buscarUltimoId(){ return clienteRepo.findLastId(); }

        @Transactional(readOnly = true)
        public Cliente buscarPorId(Long id){ return clienteRepo.findById(id).get();}

        @Transactional(readOnly = true)
        public String obtenerNombre(Long id) { return  clienteRepo.obtenerNombre(id); }

        @Transactional(readOnly = true)
        public List<Cliente> obtenerTodos(){ return clienteRepo.findAll(); }

        @Transactional
        public void actualizarSaldoAFavor(Long idCliente, BigDecimal importe) {
                Cliente c = clienteRepo.findById(idCliente).get();
                c.setSaldoAFavor(c.getSaldoAFavor().add(importe));
                clienteRepo.save(c);
        }

        @Transactional
        public void descontarSaldoAFavor(Long idCliente, BigDecimal importe) {
                Cliente c = clienteRepo.findById(idCliente).get();
                c.setSaldoAFavor(c.getSaldoAFavor().subtract(importe));
                clienteRepo.save(c);
        }
}

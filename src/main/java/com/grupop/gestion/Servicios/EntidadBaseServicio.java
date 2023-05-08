package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.EntidadBaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Source;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntidadBaseServicio {

    private final EntidadBaseRepo entidadBaseRepo;
    private final ClienteServicio clienteServicio;
    private final ProveedorServicio proveedorServicio;
    private final EmpleadoServicio empleadoServicio;
    private final VendedorServicio vendedorServicio;

    @Transactional
    public void crear(EntidadBase dto) {
        EntidadBase ent = new EntidadBase();
        ent.setRazonSocial(dto.getRazonSocial());
        ent.setCuit(dto.getCuit());
        ent.setNombreFantasia(dto.getNombreFantasia());
        ent.setTipoIva(dto.getTipoIva());
        ent.setDomicilio(dto.getDomicilio());
        ent.setTelefono(dto.getTelefono());
        ent.setCorreo(dto.getCorreo());
        entidadBaseRepo.save(ent);
    }

    @Transactional
    public void actualizar(EntidadBase dto) {
        EntidadBase ent = entidadBaseRepo.findById(dto.getId()).get();
        ent.setRazonSocial(dto.getRazonSocial());
        ent.setCuit(dto.getCuit());
        ent.setNombreFantasia(dto.getNombreFantasia());
        ent.setTipoIva(dto.getTipoIva());
        ent.setDomicilio(dto.getDomicilio());
        ent.setTelefono(dto.getTelefono());
        ent.setCorreo(dto.getCorreo());
        ent.setFechaModificacion(LocalDate.now());
        entidadBaseRepo.save(ent);
    }

    @Transactional(readOnly = true)
    public List<EntidadBase> obtenerTodos() {
        return entidadBaseRepo.findAll();
    }

    @Transactional(readOnly = true)
    public EntidadBase buscarPorId(Long id) {
        return entidadBaseRepo.findById(id).get();
    }

    @Transactional
    public void eliminarPorId(Long id) {
        entidadBaseRepo.deleteById(id);
    }


    @Transactional
    public void setCliente(Long id) {
        EntidadBase ent = entidadBaseRepo.findById(id).get();
        if (ent.getCliente() == null) {
            clienteServicio.crear();
            Long idCliente = clienteServicio.buscarUltimoId();
            System.out.println("Ultimo id " + idCliente);
            Cliente cte = clienteServicio.buscarPorId(idCliente);
            System.out.println("Encontre el cte: " + cte);

            ent.setCliente(cte);
            entidadBaseRepo.save(ent);
        } else {
            System.out.println("La entidad ya es un cliente");
        }

    }

    @Transactional
    public void setProv(Long id) {
        EntidadBase ent = entidadBaseRepo.findById(id).get();

        if (ent.getProveedor() == null) {
            proveedorServicio.crear();
            Long idProv = proveedorServicio.buscarUltimoId();
            System.out.println("Ultimo id " + idProv);
            Proveedor prov = proveedorServicio.buscarPorId(idProv);
            System.out.println("Encontre el prov: " + prov);

            ent.setProveedor(prov);
            entidadBaseRepo.save(ent);
        } else {
            System.out.println("La entidad ya es un proveedor");
        }


    }

    @Transactional
    public void setEmp(Long id) {
        EntidadBase ent = entidadBaseRepo.findById(id).get();
        if (ent.getEmpleado() == null) {
            empleadoServicio.crear();
            Long idEmp = empleadoServicio.buscarUltimoId();
            System.out.println("Ultimo id " + idEmp);
            Empleado emp = empleadoServicio.buscarPorId(idEmp);
            System.out.println("Encontre el emp: " + emp);

            ent.setEmpleado(emp);
            entidadBaseRepo.save(ent);
        }

    }

    @Transactional
    public void setVend(Long id) {
        EntidadBase ent = entidadBaseRepo.findById(id).get();
        if (ent.getVendedor() == null) {
            vendedorServicio.crear();
            Long idEmp = vendedorServicio.buscarUltimoId();
            System.out.println("Ultimo id " + idEmp);
            Vendedor vendedor = vendedorServicio.buscarPorId(idEmp);
            System.out.println("Encontre el emp: " + vendedor);

            ent.setVendedor(vendedor);
            entidadBaseRepo.save(ent);
        }
    }
}

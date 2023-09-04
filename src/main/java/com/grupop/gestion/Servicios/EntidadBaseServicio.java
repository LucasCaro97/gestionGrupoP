package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.EntidadBaseRepo;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.xml.transform.Source;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            Cliente cte = clienteServicio.buscarPorId(idCliente);

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
            Proveedor prov = proveedorServicio.buscarPorId(idProv);

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
            Empleado emp = empleadoServicio.buscarPorId(idEmp);

            ent.setEmpleado(emp);
            entidadBaseRepo.save(ent);
        }else {
            System.out.println("La entidad ya es un empleado");
        }

    }

    @Transactional
    public void setVend(Long id) {
        EntidadBase ent = entidadBaseRepo.findById(id).get();
        if (ent.getVendedor() == null) {
            vendedorServicio.crear();
            Long idEmp = vendedorServicio.buscarUltimoId();
            Vendedor vendedor = vendedorServicio.buscarPorId(idEmp);

            ent.setVendedor(vendedor);
            entidadBaseRepo.save(ent);
        }else {
            System.out.println("La entidad ya es un vendedor");
        }
    }

    @Transactional(readOnly = true)
    public List<EntidadBase> obtenerClientes() { return entidadBaseRepo.findClients(); }
    @Transactional(readOnly = true)
    public List<EntidadBase> obtenerProveedores(){ return entidadBaseRepo.findSuppliers(); }
    @Transactional(readOnly = true)
    public List<EntidadBase> obtenerEmpleados(){ return entidadBaseRepo.findEmployees(); }
    @Transactional(readOnly = true)
    public List<EntidadBase> obtenerVendedores() { return entidadBaseRepo.findSellers(); }

    @Transactional(readOnly = true)
    public String obtenerCuitCliente(Long id){ return entidadBaseRepo.buscarCuitCliente(id); }

    @Transactional(readOnly = true)
    public String obtenerIvaCliente (Long id) { return entidadBaseRepo.buscarIvaCliente(id); }

    @Transactional(readOnly = true)
    public EntidadBase obtenerNombrePorFkCliente(Long id){ return entidadBaseRepo.obtenerNombreFkClienteId(id); }

    @Transactional(readOnly = true)
    public EntidadBase obtenerNombrePorFkVendedor(Long id) { return entidadBaseRepo.obtenerNombreFkVendedorId(id);    }

    public EntidadBase obtenerNombrePorFkProveedor(Long id) {
        return entidadBaseRepo.obtenerNombreFkProveedorId(id);
    }

    public void exportInvoice() {

        try{
            //CARGO EL ARCHIVO .JASPER DESDE EL CLASSPATH
            InputStream jasperStream = EntidadBase.class.getResourceAsStream("/static/reportes/ReporteEntidades.jasper");
            //CARGO LOS DATOS A MOSTRAR EN UNA LISTA


            List<EntidadBase> listaEntidades = entidadBaseRepo.findAll();
            EntidadBase dummyEntidad = new EntidadBase();
            dummyEntidad.setId(null);
            dummyEntidad.setRazonSocial(null);
            dummyEntidad.setCuit(null);
            dummyEntidad.setFechaAlta(null);
            dummyEntidad.setNombreFantasia(null);
            dummyEntidad.setTipoIva(null);
            dummyEntidad.setCliente(null);
            dummyEntidad.setProveedor(null);
            dummyEntidad.setEmpleado(null);
            dummyEntidad.setVendedor(null);
            dummyEntidad.setDomicilio(null);
            dummyEntidad.setTelefono(null);
            dummyEntidad.setCorreo(null);
            dummyEntidad.setFechaModificacion(null);

            listaEntidades.add(0,dummyEntidad);

            for (EntidadBase ent: listaEntidades) {
                System.out.println(ent);
            }

            //CONVIERTO LA LISTA EN UNA FUENTE DE DATOS JRBEANCOLLECTIONDATASOURCE
            JRDataSource dataSource = new JRBeanCollectionDataSource(listaEntidades);


            // Cargar la imagen desde la carpeta /static/img
            InputStream logoStream = EntidadBase.class.getResourceAsStream("/static/img/logo_grupop.png");
            //PASO LOS PARAMETROS
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoStream);
            parametros.put("dsEntidades", dataSource);

            String nombreArchivo = generarNombreArchivo();
            String outputPath = "C://Users//Admin//desarollo_web//gestion_local//gestionBorrador//src//main//resources//static//informes//" + nombreArchivo;

            //GENERO EL REPORTE
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);
            //EXPORTO EL REPORTE A PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            System.out.println("Reporte generado exitosamente");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static String generarNombreArchivo(){
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHoraFormateada = fechaHoraActual.format(formateador);
        return "reporte" + fechaHoraFormateada + ".pdf";
    }
}

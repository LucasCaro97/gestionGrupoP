package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Entidades.TipoProducto;
import com.grupop.gestion.Repositorios.ProductoRepo;
import com.grupop.gestion.Repositorios.TipoProductoRepo;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServicio {

    private final ProductoRepo productoRepo;
    private final TipoProductoServicio tipoProductoServicio;

    @Transactional
    public void crear(Producto dto){
        Producto prod = new Producto();
        prod.setDescripcion(dto.getDescripcion());
        prod.setTipoProducto(dto.getTipoProducto());
        prod.setCuentasContables(dto.getCuentasContables());
        prod.setImpuestos(dto.getImpuestos());
        prod.setEstado(true);
        productoRepo.save(prod);
    }

    @Transactional
    public void actualizar(Producto dto){
        Producto prod = productoRepo.findById(dto.getId()).get();
        prod.setDescripcion(dto.getDescripcion());
        prod.setTipoProducto(dto.getTipoProducto());
        prod.setCuentasContables(dto.getCuentasContables());
        prod.setImpuestos(dto.getImpuestos());
        productoRepo.save(prod);
    }

    @Transactional      //FALTA SETEAR CUENTA CONTABLE
    public void crearProductoLote(Lote dto) throws Exception {

        boolean valida = productoRepo.existsByLote(dto);

        if(!valida){
            Producto prod = new Producto();
            prod.setDescripcion(dto.getDescripcion());
            prod.setTipoProducto(tipoProductoServicio.obtenerIdTipoLote("Lote"));
            prod.setLote(dto);
            prod.setCuentasContables(dto.getUrbanizacion().getCuenta());
            prod.setEstado(true);
            productoRepo.save(prod);
        }else{
            throw new Exception("El lote ya cuenta con un producto vinculado");
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos(){
        return productoRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Producto> obtenerTodos(String descripcion, Long idTipoProd, Long idCuenta, int page, int size){

        if(descripcion==null || descripcion == ""){
            if(idTipoProd!=null && idCuenta!= null){
                return productoRepo.searchByTipoProdAndCuenta(idTipoProd, idCuenta, PageRequest.of(page,size));
            }else if (idTipoProd!=null && idCuenta==null){
                return productoRepo.searchByTipoProd(idTipoProd, PageRequest.of(page,size));
            }else if(idTipoProd==null && idCuenta!=null){
                return productoRepo.searchByCuentaPageable(idCuenta, PageRequest.of(page,size));
            }else if (idTipoProd==null && idCuenta == null){
                return productoRepo.findAllPageable(PageRequest.of(page,size));
            }
        }
        else{
            if(idTipoProd!=null && idCuenta!= null){
                return productoRepo.searchByTipoProdAndCuentaAndDesc(idTipoProd, idCuenta, descripcion, PageRequest.of(page,size));
            }else if (idTipoProd!=null && idCuenta==null){
                return productoRepo.searchByTipoProdAndDescripcion(idTipoProd,descripcion, PageRequest.of(page,size));
            }else if(idTipoProd==null && idCuenta!=null){
                return productoRepo.searchByCuentaAndDescPageable(idCuenta, descripcion, PageRequest.of(page,size));
            }else if(idTipoProd==null && idCuenta == null){
                return productoRepo.searchByDescripcionPageable(descripcion, PageRequest.of(page,size));
            }
            else{
                return productoRepo.findAllPageable(PageRequest.of(page,size));
            }
        }
        return null;
    }

    public List<Producto> obtenerActivos(Boolean estado){ return productoRepo.searchByEstado(estado); }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id){ return productoRepo.findById(id).get();}

    @Transactional
    public void eliminarPorId(Long id){
        Producto p = productoRepo.findById(id).get();
        productoRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerPorDescripcionAndCuenta(String descripcion, Long idCuenta) {
        return productoRepo.searchByCuentaAndDesc(idCuenta,descripcion);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerPorDescripcion (String descripcion) {
        return productoRepo.searchByDescripcion(descripcion);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerPorCuenta(Long idCuenta) {
        return productoRepo.searchByCuenta(idCuenta);
    }

    @Transactional(readOnly = true)
    public Producto buscarPorDesc(String descProd) {    return productoRepo.searchProductoByDescripcion(descProd); }

    @Transactional
    public void actualizarEstadoPorId(Long idProd, Integer estado) {
        Producto prod = productoRepo.findById(idProd).get();
        if(estado == 1){
            prod.setEstado(true);
        }else{
            prod.setEstado(false);
        }
        productoRepo.save(prod);
    }


}

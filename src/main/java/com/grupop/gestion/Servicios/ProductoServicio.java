package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Entidades.TipoProducto;
import com.grupop.gestion.Repositorios.ProductoRepo;
import com.grupop.gestion.Repositorios.TipoProductoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        prod.setImpuestos(dto.getImpuestos());
        productoRepo.save(prod);
    }

    @Transactional
    public void crearProductoLote(Lote dto){
        Producto prod = new Producto();
        prod.setDescripcion(dto.getDescripcion());
        prod.setTipoProducto(tipoProductoServicio.obtenerIdTipoLote("Lote"));
        productoRepo.save(prod);
    }

}

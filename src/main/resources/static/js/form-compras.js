
$(document).ready(function () {
var url = $(location).attr('pathname');

var fechaActual = document.getElementById("fechaAlta").value;

if (fechaActual.length){
    console.log("Hay contenido");
}
else{
    var fecha = new Date();
    document.getElementById("fechaAlta").value = fecha.toJSON().slice(0,10);
}

const endPoint3= "/talonario/obtenerNroComprobante/";


if($(btnAlta).text() == "Crear"){
    $("#divDetalle").hide();
}else{
    $("#divDetalle").show();
}

$("#talonario").change(function(){
  var valorTalon = $("#talonario").val();

  if( $("#talonario").val() != "" ){
        $.get(endPoint3 + $("#talonario").val(), function(dato){
          $("#nroComprobante").val(dato);
        });
  }else{
        alert("Debe elegir un talonario valido");
         $("#nroComprobante").val("");
  }

});

//CARGO TIPO PRODUCTO
$.get("/tipoProducto/obtenerTodos", function(datos,status){
    $.each(datos, function(key,value){
        $("#tipoProd").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
    });
});

//CARGO CUENTA
$.get("/cuentas/obtenerTodos", function(datos,status){
    $.each(datos, function(key,value){
        $("#cuenta").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
    });
});

//FUNCIONES TAB PRODUCTOS

$("#filtro").click(function(){
    var url;
    $("#tablaProducto tbody").remove();
    $("#tablaProducto").append("<tbody></tbody>");
    var descripcion = $("#descripcion").val();
    var cuenta = $("#cuenta").val();

    if(descripcion!="" && cuenta != ""){
        url = "/producto/obtenerPorDescripcionAndCuenta/"+ descripcion + "/" + cuenta;
    }
    if(descripcion=="" && cuenta != ""){
        url = "/producto/obtenerPorCuenta/"+ cuenta;
    }
    if(descripcion!="" && cuenta == ""){
        url = "/producto/obtenerPorDescripcion/"+ descripcion;
    }
    if(descripcion=="" && cuenta == ""){
        alert("Para filtrar debe ingresar una descripcion o seleccionar una cuenta");
    }

    $.get(url, function(datos,status){
        $.each(datos, function(key,value){
            $("#tablaProducto tbody").append("<tr> + <td> "+ value.id +" </td> + <td>  "+ value.descripcion +"  </td>+ <td>  "+ value.cuentasContables.descripcion +"  </td> + <td>  <div + class='form-check text-center' + >   <input class='form-check-input row-item check' type='checkbox'>   </div> </td> + </tr>");
        });
    });
});

$("#limpiar").click(function(){
    $("#tablaProducto tbody").remove();
    $("#tablaProducto").append("<tbody></tbody>");
    $.get("/producto/obtenerProductosTodos", function(datos,status){
        $.each(datos, function(key,value){
            $("#tablaProducto tbody").append("<tr> + <td> "+ value.id +" </td> + <td>  "+ value.descripcion +"  </td>+ <td>  "+ value.cuentasContables.descripcion +"  </td> + <td>  <div + class='form-check text-center' + >   <input class='form-check-input row-item check' type='checkbox'>   </div> </td> + </tr>");
        });
    });
});

$("#addItem").click(function(){
    $("#tablaProducto tr").each(function(index, element){
        var checkbox = $(element).find(".check");
        if (checkbox.is(":checked")) {
            var filaEditable = $("<tr></tr>");
            var celda1 = "<td>" + $(element).children().eq(1).text() + "</td>"
            var celda2 = "<td  class='celdaOculta' >" + $(element).children().eq(0).text() + "</td>"
            var celda3 = "<td contenteditable='true' class='editable'>1.0</td>"
            var celda4 = "<td contenteditable='true' class='editable'>0.00</td>"
            var celda5 = "<td contenteditable='true' class='editable'>0.00</td>"
            var celda6 = "<td contenteditable='true' class='editable'>0.00</td>"
            var celda7 = "<td contenteditable='true' class='editable'>0.00</td>"
            var celda8 = "<td class='celdaMoneda'>0.00</td>"
            var celda9 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4,celda5,celda6,celda7,celda8,celda9);
             $("#tablaDetalle tbody").append(filaEditable);
        }
    });

});

$("#confirmDelete").click(function(){
        var filasMarcadas=[];
        var totalVenta = $("#totalVenta").val();

        //RECORRER TODAS LAS FILAS DE LA TABLA Y CREO UN ARRAY DE LAS FILAS MARCADAS PARA LUEGO ELIMINARLAS DE LA TABLA
        $("#tablaDetalle tbody tr").each(function(){
            var checkbox = $(this).find(".row-item");

            //VERIFICAR SI EL CHECKBOX ESTA MARCADO
            if(checkbox.prop("checked")){
                var filaMarcada = $(this).index();
                filasMarcadas.push(filaMarcada);
            }

        });

        //ELIMINO LAS FILAS MARCADAS DE LA TABLA Y DE LA BASE DE DATOS
        for(var i = 0; i < filasMarcadas.length; i++){
        var indiceFila = filasMarcadas[i];
        var filaMarcada = $("#tablaDetalle tbody tr").eq(indiceFila);


        var totalLinea = filaMarcada.find("td:eq(7)").text();
        totalVenta -= totalLinea;
        var idProducto = filaMarcada.find("td:eq(1)").text();
        var idVenta = $("#id").val();


        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/comprasDetalle/bajaDetalle/";
        var nuevaUrl = urlObj.href;
        //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
        fetch(nuevaUrl+ idVenta + "/" + idProducto, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })
}


            //console.log("Total luego de eliminar lineas: " + totalVenta)
            //GUARDO EL TOTAL DE LA COMPRA EN LA TABLA COMPRA
            fetch("/comprasDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
                    method : "POST",
                    headers:{
                    "Content-Type" : "application/json"
                    }
                })

var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/compras/form/"+ $("#id").val();
}
//setTimeout(redireccionar, tiempoEspera);
});

//FUNCIONES TAB IMPUTACION
$("#filtroImp").click(function(){
    var url;
    $("#tablaProductoImp tbody").remove();
    $("#tablaProductoImp").append("<tbody></tbody>");
    var descripcion = $("#descripcionImp").val();

        url = "/cuentas/obtenerPorDescripcion/"+ descripcion;

    $.get(url, function(datos,status){
        $.each(datos, function(key,value){
            $("#tablaProductoImp tbody").append("<tr> + <td> "+ value.id +" </td> + <td>  "+ value.descripcion +"  </td>+ <td>  "+ value.ctaTotalizadora.descripcion +"  </td> + <td>  <div + class='form-check text-center' + >   <input class='form-check-input row-item checkImp' type='checkbox'>   </div> </td> + </tr>");
        });
    });
});

$("#limpiarImp").click(function(){
    $("#tablaProductoImp tbody").remove();
    $("#tablaProductoImp").append("<tbody></tbody>");
    $.get("/cuentas/obtenerTodos", function(datos,status){
        $.each(datos, function(key,value){
            $("#tablaProductoImp tbody").append("<tr> + <td> "+ value.id +" </td> + <td>  "+ value.descripcion +"  </td>+ <td>  "+ value.ctaTotalizadora.descripcion +"  </td> + <td>  <div + class='form-check text-center' + >   <input class='form-check-input row-item check' type='checkbox'>   </div> </td> + </tr>");
        });
    });
});

$("#addItemImp").click(function(){
    $("#tablaProductoImp tr").each(function(index, element){
        var checkbox = $(element).find(".checkImp");
        if (checkbox.is(":checked")) {
            var filaEditable = $("<tr></tr>");
            var celda1 = "<td>" + $(element).children().eq(1).text() + "</td>"
            var celda2 = "<td  class='celdaOculta' >" + $(element).children().eq(0).text() + "</td>"
            var celda3 = "<td contenteditable='true' class='editable'>0.0</td>"
            var celda4 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4);
             $("#tablaDetalleImp tbody").append(filaEditable);

        }
    });

});

$("#confirmDeleteImp").click(function(){
        var filasMarcadas=[];
        var totalVenta = $("#totalVenta").val();

        //RECORRER TODAS LAS FILAS DE LA TABLA Y CREO UN ARRAY DE LAS FILAS MARCADAS PARA LUEGO ELIMINARLAS DE LA TABLA
        $("#tablaDetalleImp tbody tr").each(function(){
            var checkbox = $(this).find(".row-item");

            //VERIFICAR SI EL CHECKBOX ESTA MARCADO
            if(checkbox.prop("checked")){
                var filaMarcada = $(this).index();
                filasMarcadas.push(filaMarcada);
            }

        });

        //ELIMINO LAS FILAS MARCADAS DE LA TABLA Y DE LA BASE DE DATOS
        for(var i = 0; i < filasMarcadas.length; i++){
            var indiceFila = filasMarcadas[i];
            var filaMarcada = $("#tablaDetalleImp tbody tr").eq(indiceFila);


            var totalLinea = filaMarcada.find("td:eq(2)").text();
            totalVenta -= totalLinea;
            var idProducto = filaMarcada.find("td:eq(1)").text();
            var idVenta = $("#id").val();


            var url = window.location.href;
            var urlObj = new URL(url);
            urlObj.pathname = "/comprasDetalleImputacion/bajaDetalle/";
            var nuevaUrl = urlObj.href;
            //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
            fetch(nuevaUrl+ idVenta + "/" + idProducto, {
                    method : "POST",
                    headers:{
                    "Content-Type" : "application/json"
                    }
                })
        }


            //console.log("Total luego de eliminar lineas: " + totalVenta)
            //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
            fetch("/comprasDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
                    method : "POST",
                    headers:{
                    "Content-Type" : "application/json"
                    }
                })



var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/compras/form/"+ $("#id").val();
}
setTimeout(redireccionar, tiempoEspera);
});

/*
    BOTON GUARDAR QUE GUARDA / INSERTA LINEAS DE DETALLE DE PRODUCTOS E IMPUTACIONES
    EN 1 SOLO PASO Y ACTUALIZA EL TOTAL HACIENDO RECUENTO DE LAS 2 TABLAS
*/
$(".guardarDetalle").click(function(){
crearItemsDetalle();
});


$.get("/compras/obtenerTotalPorId/" + $("#id").val(), function(datos, status){
           $("#totalCompra").val(datos);
           console.log("Total compra: " + datos);
});



function crearItemsDetalle(){
    let total = 0;

    function confirmSave(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/comprasDetalle/altaDetalle/";
        var nuevaUrl = urlObj.href;


        //RECORRO TABLA DETALLE PRODUCTO
        $("#tablaDetalle tbody tr").each(function(){
            let idCompra = $("#id").val();
            let descProd = $(this).children().eq(0).text();
            let idProd = $(this).children().eq(1).text();
            let cantidad = parseFloat($(this).children().eq(2).text());
            let precioU =  parseFloat($(this).children().eq(3).text());
            let precioF = parseFloat($(this).children().eq(4).text());
            let totalLinea = (cantidad*precioF);
            total += totalLinea;

            //GENERO LOS DETALLES DE LA COMPRA EN LA BASE DE DATOS
            fetch(nuevaUrl+ idCompra + "/" + idProd + "/" + cantidad + "/" + precioU + "/" + precioF, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            });
        });
    };


    function confirmSaveImp(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/compraDetalleImputacion/altaDetalle/";
        var nuevaUrl = urlObj.href;

        //RECORRO TABLA DETALLE IMPUTACION
        $("#tablaDetalleImp tbody tr").each(function(){
            let idVenta = $("#id").val();
            let descCta = $(this).children().eq(0).text();
            let idCta = $(this).children().eq(1).text();
            let importe = parseFloat($(this).children().eq(2).text());


            total = total + importe;

            //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
            fetch(nuevaUrl+ idVenta + "/" + idCta + "/" + importe, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })
        });
    };


    confirmSave();
    confirmSaveImp();



    //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
    fetch("/compras/actualizarTotalCompra/" + $("#id").val() + "/" + total, {
            method : "POST",
            headers:{
            "Content-Type" : "application/json"
            }
    })

    var tiempoEspera = 500;
    function redireccionar() {
      window.location.href= "/compras/form/"+ $("#id").val();
    }
    setTimeout(redireccionar, tiempoEspera);
}


/* NO PERMITIR CAMBIOS CUANDO LA VENTA POSEA UN PAGO GENERADO
$('input').prop('readonly', true);
$('select').prop('disabled', true);
$('textarea').prop('readonly', true);
$('#add').hide();
$('#deleteRow').hide();
$('#guardarItems').hide();
$('#volverAtras ').hide();
*/
});



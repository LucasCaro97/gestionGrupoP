
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

const endPoint1= "/entidadBase/obtenerIva/";
const endPoint2= "/entidadBase/obtenerCuit/";
const endPoint3= "/talonario/obtenerNroComprobante/";


if($(btnAlta).text() == "Crear"){
    $("#divDetalle").hide();
}else{
    $("#divDetalle").show();
}

if( (url.localeCompare("/ventas/form")) != 0){
      $.get(endPoint1 + $('select[id=cliente]').val(), function(dato){

      $("#IVA").val(dato);
      $("#idIva").val(dato);
    });

    $.get(endPoint2 + $('select[id=cliente]').val(), function(dato){

      $("#cuit").val(dato);
    });
}

$("#cliente").change(function(){

      $.get(endPoint1 + $('select[id=cliente]').val(), function(dato){
        $("#IVA").val(dato);
        $("#idIva").val(dato);
      });

    $.get(endPoint2 + $('select[id=cliente]').val(), function(dato){
      $("#cuit").val(dato);
    });

});


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
            var celda4 = "<td contenteditable='true' class='celdaMoneda editable'>0.00</td>"
            var celda5 = "<td class='celdaMoneda'>0.00</td>"
            var celda6 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4,celda5,celda6);
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


        var totalLinea = filaMarcada.find("td:eq(4)").text();
        totalVenta -= totalLinea;
        var idProducto = filaMarcada.find("td:eq(1)").text();
        var idVenta = $("#id").val();


        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/ventaDetalle/bajaDetalle/";
        var nuevaUrl = urlObj.href;
        //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
        fetch(nuevaUrl+ idVenta + "/" + idProducto, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })
            .then(response => response.json())
            .then(data => {
            ///console.log("Se registro la linea con exito" + data);
            })
            .catch(error => {
            //MENSAJE DE ERROR
            })
        }


            //console.log("Total luego de eliminar lineas: " + totalVenta)
            //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
            fetch("/ventaDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
                    method : "POST",
                    headers:{
                    "Content-Type" : "application/json"
                    }
                })
                .then(response => response.json())
                .then(data => {
                //console.log("Se registro la linea con exito" + data);
                })
                .catch(error => {
                //console.log("guarde total = " + totalVenta);
                })


var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/ventas/form/"+ $("#id").val();
}
setTimeout(redireccionar, tiempoEspera);
});

$("#confirmSave").click(function(){
alert("Se van a marcar los lotes de esta venta con ESTADO: VENDIDO  --> FALTA IMPLEMENTAR <--")
var url = window.location.href;
var urlObj = new URL(url);
urlObj.pathname = "/ventaDetalle/altaDetalle/";
var nuevaUrl = urlObj.href;

let totalVenta = 0;

$("#tablaDetalle tbody tr").each(function(){
    let idVenta = $("#id").val();
    let descProd = $(this).children().eq(0).text();
    let idProd = $(this).children().eq(1).text();
    let cantidad = parseFloat($(this).children().eq(2).text());
    let precioU =  parseFloat($(this).children().eq(3).text());


    let totalLinea = (cantidad * precioU);
    totalVenta += totalLinea;

    //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
    fetch(nuevaUrl+ idVenta + "/" + idProd + "/" + cantidad + "/" + precioU, {
        method : "POST",
        headers:{
        "Content-Type" : "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
    ///console.log("Se registro la linea con exito" + data);
    })
    .catch(error => {
    //console.log("Guarde items")
    })

});

//GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
fetch("/ventaDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
        method : "POST",
        headers:{
        "Content-Type" : "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
    //console.log("Se registro la linea con exito" + data);
    })
    .catch(error => {
    //console.log("guarde total = " + totalVenta);
    })

var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/ventas/form/"+ $("#id").val();
}
setTimeout(redireccionar, tiempoEspera);


});


$('.editable').on('input', function() { //DETECTO QUE SE ALTERO UNA CELDA
var fila = $(this).closest('tr'); //obtengo la fila donde se realizo la alteracion

let totalVenta = 0;
let cantidad = $(fila).children().eq(2).text(); // CANTIDAD
let precioUnitario = $(fila).children().eq(3).text(); // PRECIO U
$(fila).children().eq(4).text(cantidad*precioUnitario);

    $("#tablaDetalle tbody tr").each(function(){
        var valorCelda = $(this).find("td:eq(4)").text();
        totalVenta += parseFloat(valorCelda);
    });

$("#totalVenta").val(totalVenta);

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
            var celda4 = "<td contenteditable='true' class='editable'>0.0</td>"
            var celda5 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4,celda5);
             $("#tablaDetalleImp tbody").append(filaEditable);

        }
    });

});

//TRABAJAR EN ESTO 13/06/23
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


        var totalLinea = filaMarcada.find("td:eq(4)").text();
        totalVenta -= totalLinea;
        var idProducto = filaMarcada.find("td:eq(1)").text();
        var idVenta = $("#id").val();


        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/ventaDetalle/bajaDetalle/";
        var nuevaUrl = urlObj.href;
        //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
        fetch(nuevaUrl+ idVenta + "/" + idProducto, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })
            .then(response => response.json())
            .then(data => {
            ///console.log("Se registro la linea con exito" + data);
            })
            .catch(error => {
            //MENSAJE DE ERROR
            })
        }


            //console.log("Total luego de eliminar lineas: " + totalVenta)
            //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
            fetch("/ventaDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
                    method : "POST",
                    headers:{
                    "Content-Type" : "application/json"
                    }
                })
                .then(response => response.json())
                .then(data => {
                //console.log("Se registro la linea con exito" + data);
                })
                .catch(error => {
                //console.log("guarde total = " + totalVenta);
                })


var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/ventas/form/"+ $("#id").val();
}
setTimeout(redireccionar, tiempoEspera);
});

//TRABAJAR EN ESTO 13/06/23
$("#confirmSave").click(function(){
alert("Se van a marcar los lotes de esta venta con ESTADO: VENDIDO  --> FALTA IMPLEMENTAR <--")
var url = window.location.href;
var urlObj = new URL(url);
urlObj.pathname = "/ventaDetalle/altaDetalle/";
var nuevaUrl = urlObj.href;

let totalVenta = 0;

$("#tablaDetalle tbody tr").each(function(){
    let idVenta = $("#id").val();
    let descProd = $(this).children().eq(0).text();
    let idProd = $(this).children().eq(1).text();
    let cantidad = parseFloat($(this).children().eq(2).text());
    let precioU =  parseFloat($(this).children().eq(3).text());


    let totalLinea = (cantidad * precioU);
    totalVenta += totalLinea;

    //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
    fetch(nuevaUrl+ idVenta + "/" + idProd + "/" + cantidad + "/" + precioU, {
        method : "POST",
        headers:{
        "Content-Type" : "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
    ///console.log("Se registro la linea con exito" + data);
    })
    .catch(error => {
    //console.log("Guarde items")
    })

});

//GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
fetch("/ventaDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
        method : "POST",
        headers:{
        "Content-Type" : "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
    //console.log("Se registro la linea con exito" + data);
    })
    .catch(error => {
    //console.log("guarde total = " + totalVenta);
    })

var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/ventas/form/"+ $("#id").val();
}
setTimeout(redireccionar, tiempoEspera);


});

$.get("/ventas/obtenerTotalPorId/" + $("#id").val(), function(datos, status){
           $("#totalVenta").val(datos);
           //console.log(datos);
});


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



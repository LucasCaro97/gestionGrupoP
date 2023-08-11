
$(document).ready(function () {
var url = $(location).attr('pathname');

$("#volverAtras").click(function() {
    window.history.go(-1);
})

if ($("#btnAlta").text() == 'Crear'){
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
//$.get("/cuentas/obtenerTodos", function(datos,status){
//    $.each(datos, function(key,value){
//        $("#cuenta").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
//    });
//});

//FUNCIONES TAB COMPRAS CTA CTE

$("#addItem").click(function(){
    $("#tablaProducto tr").each(function(index, element){
        var checkbox = $(element).find(".check");
        if (checkbox.is(":checked")) {
            var filaEditable = $("<tr></tr>");
            var celda1 = "<td class='celdaOculta'></td>"
            var celda2 = "<td>" + $(element).children().eq(0).text() + "</td>"      //nro cpa
            var celda3 = "<td>" + $(element).children().eq(1).text() + "</td>"      //detalle
            var celda4 = "<td>" + $(element).children().eq(2).text() + "</td>"     // importe
            var celda5 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4,celda5);
             $("#tablaDetalle tbody").append(filaEditable);
        }
    });

});

$("#confirmDelete").click(function(){
eliminarItemsDetalleProd()
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
            var celda1 = "<td class='celdaOculta'>0</td>"
            var celda2 = "<td class='celdaOculta'>" + $(element).children().eq(0).text() + "</td>"
            var celda3 = "<td>" + $(element).children().eq(1).text() + "</td>"
            var celda4 = "<td contenteditable='true' class='editable'>0.0</td>"
            var celda5 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4,celda5);
            $("#tablaDetalleImp tbody").append(filaEditable);

        }
    });

});

$("#confirmDeleteImp").click(function(){
eliminarItemsDetalleImp()
});

/*
    BOTON GUARDAR QUE GUARDA / INSERTA LINEAS DE DETALLE DE PRODUCTOS E IMPUTACIONES
    EN 1 SOLO PASO Y ACTUALIZA EL TOTAL HACIENDO RECUENTO DE LAS 2 TABLAS
*/
$(".guardarDetalle").click(function(){
crearItemsDetalle();
});


$.get("/pago/obtenerTotalPorId/" + $("#id").val(), function(datos, status){
           let datosFormatted = parseFloat(datos).toLocaleString("en-US");
           $("#totalPago").val(datosFormatted);
           validarFormaDePago($("#id").val(), 4, datos )
});

    $(".verDetallePago").click(function() {
    let url = "http://localhost:8080/detalleDePago/getForm/" + $("#id").val() + "/4"
    window.open(url,"_blank");
    })

async function crearItemsDetalle(){
    var tiempoEspera = 500;
    function redireccionar() {
                //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
                fetch("/pago/actualizarTotalPago/" + $("#id").val(), {
                        method : "POST",
                        headers:{
                        "Content-Type" : "application/json"
                        }
                })

          window.location.href= "/pago/form/"+ $("#id").val();
        }
    function confirmSave(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/pagoDetalle/altaDetalle/";
        var nuevaUrl = urlObj.href;


        //RECORRO TABLA DETALLE PRODUCTO
        $("#tablaDetalle tbody tr").each(function(){
            let idPago = $("#id").val();
            let idCompra = $(this).children().eq(1).text()
            let importe = $(this).children().eq(3).text().replace(/\,/g, '');


            //GENERO LOS DETALLES DE LA COMPRA EN LA BASE DE DATOS
            fetch(nuevaUrl+ idPago + "/" + idCompra + "/" + importe, {
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
        urlObj.pathname = "/pagoDetalleImp/altaDetalle/";
        var nuevaUrl = urlObj.href;

        //RECORRO TABLA DETALLE IMPUTACION
        $("#tablaDetalleImp tbody tr").each(function(){
            let idPago = $("#id").val();
            let idCuenta = $(this).children().eq(1).text();
            let descCuenta = $(this).children().eq(2).text();
            let importe = parseFloat($(this).children().eq(3).text().replace(/\,/g, ''));


            //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
            fetch(nuevaUrl+ idPago + "/" + idCuenta + "/" + importe, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })
        });
    };

    await confirmSave();
    await confirmSaveImp();
    setTimeout(redireccionar, tiempoEspera);
}

async function eliminarItemsDetalleProd(){

    function eliminarFilas(){
        var filasMarcadas=[];
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

            var importe = filaMarcada.find("td:eq(2)").text();
            var idDetalleCtaCte = filaMarcada.find("td:eq(0)").text();


            var url = window.location.href;
            var urlObj = new URL(url);
            urlObj.pathname = "/pagoDetalle/bajaDetalle/";
            var nuevaUrl = urlObj.href;
            //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
            fetch(nuevaUrl+ idDetalleCtaCte, {
                method : "POST",
                headers:{
                    "Content-Type" : "application/json"
                }
            })
        }
    }
    var tiempoEspera = 500;
    function redireccionar() {
                    //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
                    fetch("/pago/actualizarTotalPago/" + $("#id").val(), {
                            method : "POST",
                            headers:{
                            "Content-Type" : "application/json"
                            }
                    })

      window.location.href= "/pago/form/"+ $("#id").val();
    }

    await eliminarFilas()
    setTimeout(redireccionar, tiempoEspera);

}


async function eliminarItemsDetalleImp(){

    function eliminarFilas(){
        var filasMarcadas=[];

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

                var idCuenta = filaMarcada.find("td:eq(1)").text();
                var idPago = $("#id").val();

                var url = window.location.href;
                var urlObj = new URL(url);
                urlObj.pathname = "/pagoDetalleImp/bajaDetalle/";
                var nuevaUrl = urlObj.href;
                //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
                fetch(nuevaUrl + idPago + "/" + idCuenta, {
                    method : "POST",
                    headers:{
                        "Content-Type" : "application/json"
                    }
                })
            }
    }
    var tiempoEspera = 500;
    function redireccionar() {

    //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
                fetch("/pago/actualizarTotalPago/" + $("#id").val(), {
                        method : "POST",
                        headers:{
                        "Content-Type" : "application/json"
                        }
                })

window.location.href= "/pago/form/"+ $("#id").val();
}

    await eliminarFilas()
    setTimeout(redireccionar, tiempoEspera);

}

});


function validarFormaDePago(idOperacion, idTipoOperacion, totalOperacion){
        $.get("/detalleDePago/obtenerTotal/" + idOperacion + "/" + idTipoOperacion, function(dato,status){
            const totalDetallePago = dato;

            console.log("TOTAL DP: " + typeof totalDetallePago + " = " + totalDetallePago)
            console.log("TOTAL OP: " + typeof totalOperacion + " = " + totalOperacion)

            if(totalDetallePago === totalOperacion){
            $(".verDetallePago").css("background-color", "#48D383")
            }else{
            $(".verDetallePago").css("background-color", "#D34848")
            }

        })
}


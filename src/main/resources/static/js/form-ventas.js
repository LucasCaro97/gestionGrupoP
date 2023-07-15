
$(document).ready(function () {
validarEstado($("#id").val());

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

//TRADUCIR CLIENTESID A NOMBRE
traducirCliente($("#cliente"));

//TRADUCIR VENDEDORID A NOMBRE
traducirVendedor($("#selectVendedor"));

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
    eliminarItemsDetalleProd()
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
            var celda4 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4);
             $("#tablaDetalleImp tbody").append(filaEditable);

        }
    });

});

//TRABAJAR EN ESTO 13/06/23
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

//agregar condicion para que solamente traiga el total cuando la venta exista en la bd
$.get("/ventas/obtenerTotalPorId/" + $("#id").val())
    .done(function(datos, status){
           $("#totalVenta").val(datos);
    })
    .fail(function(jqXHR, textStatus, errorThrown){
    console.log("No se puede traer el total porque la aun no esta en la base de datos");
    })



async function crearItemsDetalle(){
    let total = 0;
    function confirmSave(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/ventaDetalle/altaDetalle/";
        var nuevaUrl = urlObj.href;


        //RECORRO TABLA DETALLE PRODUCTO
        $("#tablaDetalle tbody tr").each(function(){
            let idVenta = $("#id").val();
            let descProd = $(this).children().eq(0).text();
            let idProd = $(this).children().eq(1).text();
            let cantidad = parseFloat( $(this).children().eq(2).text() );
            let precioU =  parseFloat( $(this).children().eq(3).text().replace(/\,/g, '') );


            let totalLinea = (cantidad*precioU);
            total += totalLinea;

            //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
            fetch(nuevaUrl+ idVenta + "/" + idProd + "/" + cantidad + "/" + precioU, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })

            fetch("/lote/setEstadoVendido"+ "/" + idProd, {
                            method : "POST",
                            headers:{
                            "Content-Type" : "application/json"
                            }
            })

        });
    };
    function confirmSaveImp(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/ventaDetalleImputacion/altaDetalle/";
        var nuevaUrl = urlObj.href;

        //RECORRO TABLA DETALLE IMPUTACION
        $("#tablaDetalleImp tbody tr").each(function(){
            let idVenta = $("#id").val();
            let descCta = $(this).children().eq(0).text();
            let idCta = $(this).children().eq(1).text();
            let importe = parseFloat($(this).children().eq(2).text().replace(/\,/g, ''));


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
    var tiempoEspera = 500;
    function redireccionar() {
            //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
            fetch("/ventas/actualizarTotalVenta/" + $("#id").val(), {
                    method : "POST",
                    headers:{
                    "Content-Type" : "application/json"
                    }
            })
          window.location.href= "/ventas/form/"+ $("#id").val();
        }

    await confirmSave();
    await confirmSaveImp();
    setTimeout(redireccionar, tiempoEspera);
}

//Redirije a la vista de credio cuando la forma de pago es credito
$("#btnAlta").click(function(){
    let texto = $("#btnAlta").text();
    let contenidoFP = $("#formaDePago").val();
    if(texto == "Guardar"){
        if(contenidoFP == 3){
        alert("Sera redirigido a la vista para generar el credito de la venta")
            window.location.href= "credito/form/new/" + $("#id").val();
        }
    }

});


$("#porcentajeComisionGeneral").change(function(){
let result = ($("#baseImponible").val() * $("#porcentajeComisionGeneral").val()) / 100;
$("#comisionGeneral").val(result);
})

$("#baseImponible").change(function(){
let result = ($("#baseImponible").val() * $("#porcentajeComisionGeneral").val()) / 100;
$("#comisionGeneral").val(result);
})


$("#addCom").click(function(){
generarComision();
})

traducirVendedorTabla();


//FIN DOCUMENT READY
});

function traducirCliente(selectCliente){
    let opciones = selectCliente.find('option:not(:first)')

    opciones.each(function(index, option){
        let razonSocial;
        let valor = $(option).val();

        $.get("/entidadBase/obtenerNombrePorFkCliente/" + valor, function(dato,status){
            razonSocial = dato.razonSocial;
            $(option).text(razonSocial);
        })
    })
}

function validarEstado(idVenta){

fetch('/ventas/validarEstado/'+idVenta)
  .then(response => response.text())
  .then(data => {
    const ventaCerrada = (data === "true");
    if(ventaCerrada===true){
        console.log("la venta esta cerrada");
        comandosEncabezado = $("#comandos #btnAlta");
        comandosDetalle = $(".comandosDet");
        botonEliminarCom = $(".eliminarCom");
        comandosEncabezado.css('display', 'none');
        comandosDetalle.css('visibility', 'hidden');
        botonEliminarCom.css('display', 'none');

        $('input').prop('readonly', true);
        $('input').css('background-color', 'var(--bs-secondary-bg)');
        $('select').prop('disabled', true);
        $('textarea').prop('readonly', true);
        $('textarea').css('background-color', 'var(--bs-secondary-bg)');
    }
    })
  .catch(error => {
    console.error('Error al obtener el valor booleano:', error);
  });

}

function traducirVendedor(selectVendedor){
    let opciones = selectVendedor.find('option:not(:first)')

    opciones.each(function(index, option){
        let razonSocial;
        let valor = $(option).val();

        $.get("/entidadBase/obtenerNombrePorFkVendedor/" + valor, function(dato,status){
            razonSocial = dato.razonSocial;
            $(option).text(razonSocial);
        })
    })
}

function generarComision(){
    //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
    fetch("/comision/generarComision/"+ $("#selectVendedor").val() + "/" + $("#id").val() + "/" + $("#baseImponible").val() + "/" + $("#porcentajeComisionGeneral").val(), {
        method : "POST",
        headers:{
            "Content-Type" : "application/json"
        }
    })
    var tiempoEspera = 500;
    function redireccionar() {
        window.location.href= "/ventas/form/"+ $("#id").val();
    }
    setTimeout(redireccionar, tiempoEspera);

}

function traducirVendedorTabla(){

     $("#tablaDetalleCom tbody tr").each(function(){
            var idVendedor = $(this).find('td:eq(0)');
            console.log("/entidadBase/obtenerNombrePorFkVendedor/"+idVendedor.text())

            $.get("/entidadBase/obtenerNombrePorFkVendedor/" + idVendedor.text(), function(dato,status){
                idVendedor.text(dato.razonSocial);
            })
        });
}

async function eliminarItemsDetalleProd(){
    function eliminarFilas(){
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
                //SETEO EL LOTE COMO DISPONIBLE NUEVAMENTE
                fetch("/lote/setEstadoDisponible"+ "/" + idProducto, {
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
                fetch("/ventas/actualizarTotalVenta/" + $("#id").val(), {
                    method : "POST",
                    headers:{
                        "Content-Type" : "application/json"
                    }
                })

        window.location.href= "/ventas/form/"+ $("#id").val();
    }

    await eliminarFilas()
    setTimeout(redireccionar, tiempoEspera);
}

async function eliminarItemsDetalleImp(){
    function eliminarFilas(){
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

            var idProducto = filaMarcada.find("td:eq(1)").text();
            var idVenta = $("#id").val();

            var url = window.location.href;
            var urlObj = new URL(url);
            urlObj.pathname = "/ventaDetalleImputacion/bajaDetalle/";
            var nuevaUrl = urlObj.href;
            //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
            fetch(nuevaUrl+ idVenta + "/" + idProducto, {
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
            fetch("/ventas/actualizarTotalVenta/" + $("#id").val(), {
                method : "POST",
                headers:{
                    "Content-Type" : "application/json"
                }
            })

    window.location.href= "/ventas/form/"+ $("#id").val();
}

    await eliminarFilas()
    setTimeout(redireccionar, tiempoEspera);
}


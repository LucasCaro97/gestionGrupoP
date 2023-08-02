
$(document).ready(function () {
validarEstado($("#id").val())

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
    function obtenerImpuestosPorCuenta(idCuenta){
        const url = "/cuentas/obtenerListaDeImpuestosPorCuenta/" + idCuenta.text();

    return new Promise((resolve, reject) => {
        fetch(url)
          .then((response) => {
            if (!response.ok) {
              // Si la respuesta no es exitosa, rechazamos la promesa con un error
              reject("Error en la solicitud: " + response.status);
            } else {
              // Convierte la respuesta en formato JSON y resuelve la promesa con los datos obtenidos
              resolve(response.json());
            }
          })
          .catch((error) => {
            // Rechaza la promesa si ocurre un error durante la solicitud
            reject("Error al procesar la solicitud: " + error.message);
          });
      });
    }
    function cargarListaDeImpuestos(){
        const selectImpuestos = $("<select name='tax' multiple></select>")
        return selectImpuestos;
    }

    $("#tablaProductoImp tr").each(async function(index, element){
        var idCuenta = $(element).find("td:first-child")
        var checkbox = $(element).find(".checkImp");
        if (checkbox.is(":checked")) {
            try{
                const listaDeImpuestos = await obtenerImpuestosPorCuenta(idCuenta)
                const selectImpuestos = cargarListaDeImpuestos()

                if (listaDeImpuestos.length === 0) {
                  // La lista de impuestos está vacía, puedes hacer algo aquí si es necesario
                } else {
                  // La lista de impuestos no está vacía, puedes agregar las opciones al select
                  listaDeImpuestos.forEach((impuesto) => {
                    const opcion = $("<option></option>").attr("value", impuesto.id).text(impuesto.descripcion);
                    selectImpuestos.append(opcion);
                  });
                }

            var filaEditable = $("<tr></tr>");
            var celda1 = "<td>" + $(element).children().eq(1).text() + "</td>"
            var celda2 = "<td  class='celdaOculta' >" + $(element).children().eq(0).text() + "</td>"
            var celda3 = "<td contenteditable='true' class='editable'>0.0</td>"
            var celda4 = "<td></td>"
            celda4 = celda4.replace("</td>", selectImpuestos.prop("outerHTML") + "</td>");
            var celda5 = "<td>0.00</td>"
            var celda6 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"

            filaEditable.append(celda1,celda2,celda3,celda4,celda5,celda6);
             $("#tablaDetalleImp tbody").append(filaEditable);

            } catch(error){
                console.log(error)
            }
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


$.get("/compras/obtenerTotalPorId/" + $("#id").val(), function(datos, status){
           let datosFormatted = parseFloat(datos).toLocaleString("en-US");
           $("#totalCompra").val(datosFormatted);
           validarFormaDePago($("#id").val(), 2, datos )
});

$(".verDetallePago").click(function() {
let url = "http://localhost:8080/detalleDePago/getForm/" + $("#id").val() + "/2"
window.open(url,"_blank");
})



async function crearItemsDetalle(){
    let total = 0;
    let totalLinea = 0;

    var tiempoEspera = 800;
    function redireccionar() {
                //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
                fetch("/compras/actualizarTotalCompra/" + $("#id").val(), {
                        method : "POST",
                        headers:{
                        "Content-Type" : "application/json"
                        }
                })

          window.location.href= "/compras/form/"+ $("#id").val();
        }
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
            let cantidad = parseFloat($(this).children().eq(2).text().replace(/\,/g, ''));
            let precioU =  parseFloat($(this).children().eq(3).text().replace(/\,/g, ''));
            let precioF = parseFloat($(this).children().eq(4).text().replace(/\,/g, ''));

            //GENERO LOS DETALLES DE LA COMPRA EN LA BASE DE DATOS
            fetch(nuevaUrl+ idCompra + "/" + idProd + "/" + cantidad + "/" + precioU + "/" + precioF, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            });
        });
    };
    async function confirmSaveImp(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/compraDetalleImputacion/altaDetalle";
        var nuevaUrl = urlObj.href;

        //RECORRO TABLA DETALLE IMPUTACION
        $("#tablaDetalleImp tbody tr").each(async function(){
            let idCompra = $("#id").val();
            let descCta = $(this).children().eq(0).text();
            let idCta = $(this).children().eq(1).text();
            let importe = parseFloat($(this).children().eq(2).text().replace(/\,/g, ''));
            const impuestosSeleccionados = [];
                $(this).find("select[name='tax'] option:selected").each(function() {
                  const impuesto = {
                    id: $(this).val(),
                    descripcion: $(this).text(),
                  };
                  impuestosSeleccionados.push(impuesto);
                });


            const compraDetalleImputacionRequest = {
                compraId: idCompra,
                cuentaContableId: idCta,
                importeBase: importe,
                impuestosIds: impuestosSeleccionados.map(impuesto => impuesto.id), // Suponiendo que impuestosSeleccionados es una lista de objetos Impuestos con un campo "id"
                importeTotal: 0,
              }

            fetch(nuevaUrl, {
                method: "POST",
                headers: {
                    "Content-Type" : "application/json",
                },
                body: JSON.stringify(compraDetalleImputacionRequest),
            })

            })
    }

    await confirmSave();
    await confirmSaveImp();

    setTimeout(redireccionar, tiempoEspera);
        }


async function eliminarItemsDetalleProd(){

    function eliminarFilas(){
        var filasMarcadas=[];
        var totalVenta = $("#totalCompra").val();

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
    }
    var tiempoEspera = 500;
    function redireccionar() {
                    //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
                    fetch("/compras/actualizarTotalCompra/" + $("#id").val(), {
                            method : "POST",
                            headers:{
                            "Content-Type" : "application/json"
                            }
                    })

      window.location.href= "/compras/form/"+ $("#id").val();
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
                urlObj.pathname = "/compraDetalleImputacion/bajaDetalle/";
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
                fetch("/compras/actualizarTotalCompra/" + $("#id").val(), {
                        method : "POST",
                        headers:{
                        "Content-Type" : "application/json"
                        }
                })

window.location.href= "/compras/form/"+ $("#id").val();
}

    await eliminarFilas()
    setTimeout(redireccionar, tiempoEspera);

}

});


function validarEstado(idCompra){

fetch('/compras/validarEstado/'+ idCompra)
  .then(response => response.text())
  .then(data => {
    const compraCerrada = (data === "true");
    if(compraCerrada===true){
        console.log("la compra esta cerrada");
        comandosEncabezado = $("#comandos #btnAlta");
        comandosDetalle = $(".comandosDet");
//        botonEliminarCom = $(".eliminarCom");
//        comandosEncabezado.css('display', 'none');
        comandosDetalle.css('visibility', 'hidden');
//        botonEliminarCom.css('display', 'none');

        $('input').prop('readonly', true);
        $('input').css('background-color', 'var(--bs-secondary-bg)');
        $('select').prop('disabled', true);
//        $('textarea').prop('readonly', true);
//        $('textarea').css('background-color', 'var(--bs-secondary-bg)');
    }
    })
  .catch(error => {
    console.error('Error al obtener el valor booleano:', error);
  });

}

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



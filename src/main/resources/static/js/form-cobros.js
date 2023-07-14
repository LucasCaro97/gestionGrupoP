
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

if( (url.localeCompare("/cobros/form")) != 0){
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


//FUNCIONES TAB PRODUCTOS
//AGREGO ITEMS AL DETALLE DE COBROS ( CUOTAS ) - CAMBIAR ITEMS QUE SE BAJAN
$("#addItem").click(function(){
    $("#tablaProducto tr").each(function(index, element){
        var checkbox = $(element).find(".check");
        if (checkbox.is(":checked")) {
            var filaEditable = $("<tr></tr>");
            var celda1 = "<td>" + $(element).children().eq(1).text() + "</td>"
            var celda2 = "<td>" + $(element).children().eq(2).text() + "</td>"
            var celda3 = "<td>" + $(element).children().eq(3).text() + "</td>"
            var celda4 = "<td>" + $(element).children().eq(11).text() + "</td>"
            var celda5 = "<td>" + $(element).children().eq(4).text() + "</td>"
            var celda6 = "<td>" + $(element).children().eq(10).text() + "</td>"
            var celda7 = "<td>" + $(element).children().eq(7).text() + "</td>"
            var celda8 = "<td>" + $(element).children().eq(8).text() + "</td>"
            var celda9 = "<td contenteditable='true' class='editable'>" + 0 + "</td>"
            var celda10 = "<td contenteditable='true' class='editable'>" + 0 + "</td>"
            var celda11 = "<td> <div class='form-check text-center'> <input class='form-check-input row-item' type='checkbox'>  </div> </td>"
            filaEditable.append(celda1,celda2,celda3,celda4,celda5,celda6,celda7,celda8,celda9,celda10, celda11);
             $("#tablaDetalle tbody").append(filaEditable);
        }
    });

});

$("#confirmDelete").click(function(){
        var filasMarcadas=[];
//        var totalVenta = $("#totalVenta").val();

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
//        totalVenta -= totalLinea;
        var idCred = filaMarcada.find("td:eq(1)").text();
        var nroCuota = filaMarcada.find("td:eq(2)").text();
        var idCobro = $("#id").val();


        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/cobroDetalleCuotas/delete/";
        var nuevaUrl = urlObj.href;
        //ELIMINO LAS LINEAS DE DETALLE SELECCIONADAS DE LA BD
        fetch(nuevaUrl+ idCred + "/" + nroCuota + "/" + idCobro, {
                method : "POST",
                headers:{
                "Content-Type" : "application/json"
                }
            })

    }
var tiempoEspera = 500;
function redireccionar() {
  window.location.href= "/cobros/form/"+ $("#id").val();
}
setTimeout(redireccionar, tiempoEspera);
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


            //console.log("Total luego de eliminar lineas: " + totalVenta)
            //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
            fetch("/ventaDetalle/actualizarTotalVenta/" + $("#id").val() + "/" + totalVenta, {
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
});

/*
    BOTON GUARDAR QUE GUARDA / INSERTA LINEAS DE DETALLE DE PRODUCTOS E IMPUTACIONES
    EN 1 SOLO PASO Y ACTUALIZA EL TOTAL HACIENDO RECUENTO DE LAS 2 TABLAS
*/
$(".guardarDetalle").click(function(){
crearItemsDetalle();
});

//Calcula los dias vencidos que lleva cada cuota
//Calcula el ajusteCac en caso de corresponder
$("#tablaProducto tbody tr").each(function(){
    let textoPrimerVencimiento = $('#tablaProducto tbody tr:first td:eq(11)').text();
    let fechaPrimerVencimiento = new Date(textoPrimerVencimiento);
    let mesPrimerVenc = fechaPrimerVencimiento.getMonth() + 1;
    let anioPrimerVenc = fechaPrimerVencimiento.getFullYear();


calcularCacAndPunitorio($(this).children().eq(2),  $(this).children().eq(4), new Date(textoPrimerVencimiento), $(this).children().eq(8), $(this).children().eq(11).text() , $(this).children().eq(12),
                $(this).children().eq(7), $(this).children().eq(4), $(this).children().eq(8), $(this).children().eq(6), $(this).children().eq(10) )


})



//agregar condicion para que solamente traiga el total cuando la venta exista en la bd
//$.get("/ventas/obtenerTotalPorId/" + $("#id").val())
//    .done(function(datos, status){
//           $("#totalVenta").val(datos);
//    })
//    .fail(function(jqXHR, textStatus, errorThrown){
//    console.log("No se puede traer el total porque la aun no esta en la base de datos");
//    })


//ALTA DE ITEMS DETALLE A LA BD
function crearItemsDetalle(){
    let total = 0;

    function confirmSave(){
        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/cobroDetalleCuotas/alta/";
        var nuevaUrl = urlObj.href;

        console.log("URL: " + url)
        console.log("URL OBJ: " + urlObj)
        console.log(nuevaUrl)

//        //RECORRO TABLA DETALLE PRODUCTO
        $("#tablaDetalle tbody tr").each(function(){
            let idVenta = $(this).children().eq(0).text();
            let idCred = $(this).children().eq(1).text();
            let nroCuota = $(this).children().eq(2).text();
            let fechaVenc = $(this).children().eq(3).text();
            let cuotaBase = $(this).children().eq(4).text();
            let ajuste = $(this).children().eq(6).text();
            let punitorio = $(this).children().eq(7).text();
            let importeBonif = $(this).children().eq(8).text();
            let importeFinal = $(this).children().eq(9).text();


//            let totalLinea = (cantidad*precioU);
//            total += totalLinea;

            //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
//          fetch(nuevaUrl+ idVenta + "/" + idCred + "/" + nroCuota + "/" + fechaVenc+ "/" + cuotaBase + "/" + ajuste + "/" + punitorio+ "/" + importeBonif + "/" + importeFinal, {
            fetch(nuevaUrl+ idVenta + "/" + idCred + "/" + nroCuota + "/" + fechaVenc + "/" + cuotaBase + "/" + ajuste + "/" + punitorio + "/" + importeBonif + "/" + importeFinal + "/" + $("#id").val() , {
                            method : "POST",
                            headers:{
                            "Content-Type" : "application/json"
                            }
            })
        });
    };

//    function confirmSaveImp(){
//        var url = window.location.href;
//        var urlObj = new URL(url);
//        urlObj.pathname = "/ventaDetalleImputacion/altaDetalle/";
//        var nuevaUrl = urlObj.href;
//
//        //RECORRO TABLA DETALLE IMPUTACION
//        $("#tablaDetalleImp tbody tr").each(function(){
//            let idVenta = $("#id").val();
//            let descCta = $(this).children().eq(0).text();
//            let idCta = $(this).children().eq(1).text();
//            let importe = parseFloat($(this).children().eq(2).text());
//
//
//            total = total + importe;
//
//            //GENERO LOS DETALLES DE LA VENTA EN LA BASE DE DATOS
//            fetch(nuevaUrl+ idVenta + "/" + idCta + "/" + importe, {
//                method : "POST",
//                headers:{
//                "Content-Type" : "application/json"
//                }
//            })
//        });
//    };
//
    confirmSave();
//    confirmSaveImp();
//
//
//
//    //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
//    fetch("/ventas/actualizarTotalVenta/" + $("#id").val() + "/" + total, {
//            method : "POST",
//            headers:{
//            "Content-Type" : "application/json"
//            }
//    })
//
    var tiempoEspera = 500;
    function redireccionar() {
      window.location.href= "/cobros/form/"+ $("#id").val();
    }
    setTimeout(redireccionar, tiempoEspera);
}


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

function calcularDiasVencidos(fechaVenc, celdaDiasVenc, celdaInteresPun, celdaCuotaBase, celdaAjuste, celdaPorcIntPun){
let fechaActual = new Date();
var fechaPago = new Date (fechaVenc);
var diferenciaMs = fechaActual - fechaPago;
var diasAtraso = Math.floor(diferenciaMs / (1000 * 60 * 60 * 24));
    if(diasAtraso > 0){
        celdaDiasVenc.text(diasAtraso);
        let cuotaBase = parseFloat(celdaCuotaBase.text());
        let ajuste = 82612.10;
        let porcPun = parseFloat(celdaPorcIntPun.text());

        let interesPun = (cuotaBase + ajuste) * porcPun * diasAtraso / 100
        let interesPunRound = interesPun.toFixed(2);
        celdaInteresPun.text(interesPunRound)
    }
}

async function calcularAjusteIndiceCac(celdaCredito, celdaCuotaBase, fechaPrimerVencimiento, celdaImporteAjuste){
    let idCredito = celdaCredito.text();
    let cuotaBase = celdaCuotaBase.text()

    mesPrimerVenc = fechaPrimerVencimiento.getMonth()
    anioPrimerVenc= fechaPrimerVencimiento.getFullYear()
    diaPrimerVenc = fechaPrimerVencimiento.getDate()

    let indiceBase = 0;
    let indiceActual = 0


    let fechaIndiceBase = obtenerMesIndiceBase(anioPrimerVenc, mesPrimerVenc, diaPrimerVenc)
    let fechaIndiceActual = obtenerMesIndiceActual()

    try{
    const dato = await $.get("/credito/obtenerPorId/" + idCredito);
    if(dato.planPago.tablaCac){

        const indiceBaseResponse = await $.get("/indiceCac/obtenerIndice/" + ( fechaIndiceBase.getMonth() + 1 ) + "/" + fechaIndiceBase.getFullYear());
        indiceBase = indiceBaseResponse;

        const indiceActualResponse = await $.get("/indiceCac/obtenerIndice/" + ( fechaIndiceActual.getMonth() +1 ) + "/" + fechaIndiceActual.getFullYear());
        indiceActual = indiceActualResponse;

        let resultado =   cuotaBase * ( indiceActual / indiceBase );
        let importeAjuste = resultado - cuotaBase;
        let importeAjusteRound = importeAjuste.toFixed(2);
        celdaImporteAjuste.text(importeAjusteRound)
    }

    } catch(error){
        console.log("Error al obtener los datos: ", error)
    }

}

function calcularTotal(celdaCuotaBase, celdaInteresPun, celdaImporteAjuste, celdaTotal){
    let cuotaBase = parseFloat(celdaCuotaBase.text())
    let interesPunitorio = parseFloat(celdaInteresPun.text())
    let ajuste = parseFloat(celdaImporteAjuste.text())
    let resultado = cuotaBase + interesPunitorio + ajuste
    let resultadoRound = resultado.toFixed(2)
    celdaTotal.text(resultadoRound)
}

function obtenerMesIndiceBase(year, mes, dia){
        var ultimoDia = new Date(year, mes, 0);

        for(let i = 0; i < 2 ; i++){

            if (mes === 0) {
                mes = 11;
                year = year - 1;
            } else {
                mes = mes - 1;
            }
            if (dia > ultimoDia.getDate()) {
                dia = ultimoDia.getDate();
            }
                var fechaRet = new Date(year, mes, dia);
            }

        return fechaRet;
}

function obtenerMesIndiceActual(){

        let fechaIndiceActual = new Date()
        let year = fechaIndiceActual.getFullYear()
        let mes = fechaIndiceActual.getMonth()
        let dia = fechaIndiceActual.getDate()


        var ultimoDia = new Date(year, mes, 0);

        for(let i = 0; i < 2 ; i++){

            if (mes === 0) {
                mes = 11;
                year = year - 1;
            } else {
                mes = mes - 1;
            }
            if (dia > ultimoDia.getDate()) {
                dia = ultimoDia.getDate();
            }
                var fechaRet = new Date(year, mes, dia);
            }

        return fechaRet;


}

async function calcularCacAndPunitorio(celdaCredito, celdaCuotaBase, fechaPrimerVencimiento, celdaImporteAjuste , fechaVenc, celdaDiasVenc, celdaInteresPun, celdaCuotaBase, celdaAjuste, celdaPorcIntPun, celdaTotal){
    await calcularAjusteIndiceCac(celdaCredito, celdaCuotaBase, fechaPrimerVencimiento, celdaImporteAjuste)
    await calcularDiasVencidos(fechaVenc, celdaDiasVenc, celdaInteresPun, celdaCuotaBase, celdaAjuste, celdaPorcIntPun)
    calcularTotal(celdaCuotaBase, celdaInteresPun , celdaAjuste, celdaTotal )
}

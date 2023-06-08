
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

        //RECORRER TODAS LAS FILAS DE LA TABLA
        $("#tablaDetalle tbody tr").each(function(){
            var checkbox = $(this).find(".row-item");

            //VERIFICAR SI EL CHECKBOX ESTA MARCADO
            if(checkbox.prop("checked")){
                var filaMarcada = $(this).index();
                filasMarcadas.push(filaMarcada);
            }

        });

        for(var i = 0; i < filasMarcadas.length; i++){
        var indiceFila = filasMarcadas[i];
        var filaMarcada = $("#tablaDetalle tbody tr").eq(indiceFila);

        var idProducto = filaMarcada.find("td:eq(1)").text();
        var idVenta = $("#id").val();

        var url = window.location.href;
        var urlObj = new URL(url);
        urlObj.pathname = "/ventaDetalle/bajaDetalle/";
        var nuevaUrl = urlObj.href;
        //console.log(nuevaUrl+ idVenta + "/" + idProducto);
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
        window.location.href= "/ventas/form/"+ $("#id").val();
});


$("#moneda").change(function(){
    console.log("Cambiando formato - " + $("#moneda").val());
    var celdasMoneda = document.getElementsByClassName("celdaMoneda");
    for(var i=0; i < celdasMoneda.length; i++){
        var celda = celdasMoneda[i];
        var valor = parseFloat(celda.textContent);
        if($("#moneda").val() == 1){
            var formatoMoneda = valor.toLocaleString("es-ES", { style: "currency", currency: "ARS"});
            }else{
            var formatoMoneda = valor.toLocaleString("es-ES", { style: "currency", currency: "USD"});
            }

        celda.textContent = formatoMoneda;
    }
});

$("#confirmSave").click(function(){
alert("Se van a marcar los lotes de esta venta con ESTADO: VENDIDO  --> FALTA IMPLEMENTAR <--")
var url = window.location.href;
var urlObj = new URL(url);
urlObj.pathname = "/ventaDetalle/altaDetalle/";
var nuevaUrl = urlObj.href;
console.log(nuevaUrl);

$("#tablaDetalle tbody tr").each(function(){
    let idVenta = $("#id").val();
    let descProd = $(this).children().eq(0).text();
    let idProd = $(this).children().eq(1).text();
    let cantidad = $(this).children().eq(2).text();
    let precioU = $(this).children().eq(3).text();

    console.log(nuevaUrl + idVenta + "/" + idProd + "/" + cantidad + "/" + precioU);
    //window.location.href = nuevaUrl + idVenta + "/" + idProd + "/" + cantidad + "/" + precioU;

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
    //MENSAJE DE ERROR
    })
    window.location.href= "/ventas/form/"+ $("#id").val();

});
});


$('.editable').on('input', function() { //DETECTO QUE SE ALTERO UNA CELDA
var fila = $(this).closest('tr'); //obtengo la fila donde se realizo la alteracion

let cantidad = $(fila).children().eq(2).text(); // CANTIDAD
let precioUnitario = $(fila).children().eq(3).text(); // PRECIO U
$(fila).children().eq(4).text(cantidad*precioUnitario);
console.log("nuevo precio: " + cantidad * precioUnitario);

//var valores = fila.find('td').map(function() { //meto todos los valores en un array llamado valores
//      return $(this).text();
//    }).get();
//console.log(valores); // imprimo mi array


//var celdaEspecifica = fila.find('td:nth-child(2)'); // Altera la segunda celda de la fila
//celdaEspecifica.text('Nuevo valor');


});




});


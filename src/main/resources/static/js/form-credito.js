$(document).ready(function () {



var fechaActual = document.getElementById("fechaAlta").value;

if (fechaActual.length){
    console.log("Hay contenido");
}
else{
    var fecha = new Date();
    document.getElementById("fechaAlta").value = fecha.toJSON().slice(0,10);
}

const endPoint3= "/talonario/obtenerNroComprobante/";

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

if($("#cliente").val() > 0){
traducirCliente($("#cliente"));
}

$("#clienteNew").change(function(){
console.log("Actualizar lista operaciones");
});

$("#planPago").change(function(){

$.get("/planPago/obtenerPlanPago/" + $("#planPago").val(), function(datos,status){
    $("#cantCuotas").val(datos.cantCuota);
    $("#porcentajeInteres").val(datos.tasaInteresTotal);
})


});

$("#clienteNew").change(function(){

$.get("/ventas/obtenerVentasSinCreditoPorCliente/" + $("#clienteNew").val(), function(datos,status){
        $("#idOperacionNew").empty();
        $("#idOperacionNew").append($("<option value=''> Seleccione operacion </option>"));
        $.each(datos, function (key, value) {
            $("#idOperacionNew").append($("<option value='"+value.id+"'> "+ value.nroComprobante + " - " + value.fechaComprobante + " - $" +  value.total +" </option>"));
        })
    })
})

$("#idOperacionNew").change(function(){
    console.log($("#idOperacionNew").val())
    $.get("/ventas/obtenerTotalPorId/"+ $("#idOperacionNew").val(), function(dato,status){
        $("#capitalNew").val(dato)
    })
})

});


function traducirCliente(selectCliente){

$.get("/entidadBase/obtenerNombrePorFkCliente/" + selectCliente.val(), function(datos,status){
    //console.log(datos);
    selectCliente.empty();
    selectCliente.append($("<option value='"+datos.id+"'> "+ datos.razonSocial +" </option>"));
         })
}



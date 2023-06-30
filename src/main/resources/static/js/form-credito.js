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


});

function traducirCliente(selectCliente){

$.get("/entidadBase/obtenerNombrePorFkCliente/" + selectCliente.val(), function(datos,status){
    //console.log(datos);
    selectCliente.empty();
    selectCliente.append($("<option value='"+datos.id+"'> "+ datos.razonSocial +" </option>"));
         })
}




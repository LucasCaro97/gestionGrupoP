$(document).ready(function () {

$("#tipoOperacion").change(function(){
    $("#formaPago option").remove();
    $("#formaPago").append('<option value="">Forma de Pago</option>');

    $.get("/formaDePago/obtenerPorOperacion/" + $("#tipoOperacion").val() , function(datos, status){

           $.each(datos, function (key, value) {
                    $("#formaPago").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
                });
        });
})

});
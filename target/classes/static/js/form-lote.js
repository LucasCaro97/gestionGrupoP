$(document).ready(function () {

const $select = $("#urbanizacion");
const endPoint = "/urbanizacion/obtenerManzanasPorId/";

$select.change(function() {
        $("#manzana option").remove();
        $("#manzana").append('<option value="">Seleccione una manzana</option>');
        //console.log(endPoint + $select.val());



    $.get(endPoint + $select.val(), function(datos, status){

       $.each(datos, function (key, value) {
                //alert(value.descripcion)
                $("#manzana").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
            });

      //alert("Data: " + data + "\nStatus: " + status);
    });



     });

});
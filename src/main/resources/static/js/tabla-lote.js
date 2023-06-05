$(document).ready(function () {

const $select = $("#urbanizacion");
const endPoint = "/urbanizacion/obtenerManzanasPorId/";

    $select.change(function() {
            $("#manzana option").remove();
            $("#manzana").append('<option value="">Seleccione una manzana</option>');

        $.get(endPoint + $select.val(), function(datos, status){

           $.each(datos, function (key, value) {
                    $("#manzana").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
           });
        });
    });


    $(".tbody tr").each(function(){
        if($(this).children().eq(11).text() == "true"){
            $(this).children().eq(11).text("Si");
        }else{
            $(this).children().eq(11).text("No");
        }
        //console.log("Iteracion" + $(this).children().eq(11).text());

    });

});
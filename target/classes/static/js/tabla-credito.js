$(document).ready(function () {


    $(".tablebody tr").each(function(){
        var celda = $(this).children().eq(1);
        let cliente = celda.text();


        $.get("cliente/obtenerNombre/" + cliente, function(dato, status){
            celda.text(dato);
        });




    });

});


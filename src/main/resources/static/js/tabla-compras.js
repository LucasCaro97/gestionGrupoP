$(document).ready(function () {

    $(".tablebody tr").each(function(){
        var idCliente = $(this).find('td:eq(1)');

        $.get("/entidadBase/obtenerNombrePorFkProveedor/" + idCliente.text(), function(dato,status){
            idCliente.text(dato.razonSocial);
        })


    });

});
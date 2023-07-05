$(document).ready(function () {

    $(".tablebody tr").each(function(){
        var idCliente = $(this).find('td:eq(1)');
        console.log("/entidadBase/obtenerNombrePorFkCliente/"+idCliente.text())

        $.get("/entidadBase/obtenerNombrePorFkCliente/" + idCliente.text(), function(dato,status){
            idCliente.text(dato.razonSocial);
        })


    });

});
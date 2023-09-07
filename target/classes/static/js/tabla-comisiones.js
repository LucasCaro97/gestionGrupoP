$(document).ready(function () {

    $(".tablebody tr").each(function(){
        var idVendedor = $(this).find('td:eq(0)');
        console.log("/entidadBase/obtenerNombrePorFkVendedor/"+idVendedor.text())

        $.get("/entidadBase/obtenerNombrePorFkVendedor/" + idVendedor.text(), function(dato,status){
            idVendedor.text(dato.razonSocial);
        })
    });

});
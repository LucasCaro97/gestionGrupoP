$(document).ready(function () {

//$(".tbody tr").each(function() {
//    calcularDiasVencidos($(this).children().eq(4),$(this).children().eq(9), $(this).children().eq(1))
//})

})




function calcularDiasVencidos(fechaVenc, celdaDiasVenc, celdaCliente){
let fechaActual = new Date();
var fechaVencimiento = new Date (fechaVenc.text());
var diferenciaMs = fechaActual - fechaVencimiento;
var diasAtraso = Math.floor(diferenciaMs / (1000 * 60 * 60 * 24));

    if(diasAtraso > 0){
        celdaDiasVenc.text(diasAtraso);
    }

    $.get("/entidadBase/obtenerNombrePorFkCliente/" + celdaCliente.text(), function(dato,status){
        celdaCliente.text(dato.razonSocial);
    })

}
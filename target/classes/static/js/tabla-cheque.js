$(document).ready(function () {

$.get("/cheque/obtenerTotalDisponible" , function(datos, status){
           $("#totalCheques").val(datos);
});

})

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


//$("#exportarExcel").click(function(){
//
//        var url = window.location.href;
//        var urlObj = new URL(url);
//        urlObj.pathname = "/reporting/generarExcel";
//        var nuevaUrl = urlObj.href;
//
//        let tipoOp = () => $("#tipoOperacion").val() != "" ? $("#tipoOperacion").val() : 0;
//        let fechaDesde = () => $("#fechaDesde").val() != "" ? $("#fechaDesde").val() : 0;
//        let fechaHasta = () => $("#fechaHasta").val() != "" ? $("#fechaHasta").val() : 0;
//        let sectorId = () => $("#sector").val() != "" ? $("#sector").val() : 0;
//        let talonDesde = () => $("#talonarioDesde").val() != "" ? $("#talonarioDesde").val() : 0;
//        let talonHasta = () => $("#talonarioHasta").val() != "" ? $("#talonarioHasta").val() : 0;
//        let excluirTalon = $("#flexCheckDefault").prop("checked")
//        let formaPagoId = () => $("#formaPago").val() != "" ? $("#formaPago").val() : 0;
//
//
//
////        console.log(nuevaUrl + "/" + tipoOp() + "/" + fechaDesde() + "/" + fechaHasta() + "/" + sectorId() + "/" + talonDesde() + "/" + talonHasta() + "/" + excluirTalon + "/" + formaPagoId())
////        if(parseInt(tipoOp,10) !== 0) fetch(nuevaUrl + "/" + tipoOp() + "/" + fechaDesde() + "/" + fechaHasta() + "/" + sectorId() + "/" + talonDesde() + "/" + talonHasta() + "/" + excluirTalon + "/" + formaPagoId())
////        else    alert("Ingresar tipo operacion")
//
//})

});
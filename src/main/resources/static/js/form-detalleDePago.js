$(document).ready(function () {

$("#addFormaDePago").click( async function(){
   var tiempoEspera = 500;
   function redireccionar() {
    window.location.href= "/detalleDePago/getForm/"+ $("#Operacion").val() + "/" + $("#tipoOperacion").val() ;

   }

   var url = window.location.href;
   var urlObj = new URL(url);
   urlObj.pathname = "/formaDePagoDetalleSubdetalle/alta/";
   var nuevaUrl = urlObj.href;

    let idOperacion = $("#Operacion").val()
    let idTipoOperacion = $("#tipoOperacion").val()
    let importe = $("#monto").val()
    let idFormaDePago = $("#formaDePago").val()

   await fetch(nuevaUrl + idOperacion + "/" + idTipoOperacion + "/" + importe + "/" + idFormaDePago, {
                   method : "POST",
                   headers:{
                       "Content-Type" : "application/json"
                   }
               })

   setTimeout(redireccionar, tiempoEspera);

})

verificarSaldo();

$("#addFormaDePago").click(function() {
    if($("#formaDePago").val() == 3){
        let idOperacion = $("#Operacion").val()
        let importe = $("#monto").val()
        let url = "/credito/form/new/" + idOperacion
        window.open(url,"_blank");
    }
})

$("#tablaDetalle tr").each(function(){
    if($(this).children().eq(0).text() === '6-CREDITO'){
        validarExistenciaCredito($("#Operacion").val(), $(this))
    }
})


})

function verificarSaldo(){
    let totalOperacion = $("#totalOperacion").val()
    let total = 0;
    $("#tablaDetalle tbody tr").each(function(){
        console.log("Calculando total fila")
        let monto = parseFloat($(this).children().eq(1).text().replace(/\,/g, ''));
        total += monto;
    })

   $("#saldo").val(totalOperacion - total);
    cambiarColorSaldo($("#saldo").val())
}

function cambiarColorSaldo(saldo){
    saldoNum = parseFloat(saldo);
    if ( saldoNum === 0) {
    $("#saldo").css("background-color", "#48D383")
   }else{
    $("#saldo").css("background-color", "#D34848")
   }
}

function validarExistenciaCredito(idOperacion , fila){
    $.get("/credito/validarExistencia/" + idOperacion, function(dato,status){
                if(dato == 0){
                    fila.css("background-color", "#D34848")
                }
            })
}







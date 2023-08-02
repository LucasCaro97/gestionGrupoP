$(document).ready(function () {
validarEstado($("#id").val())


var fechaActual = document.getElementById("fechaAlta").value;

if (fechaActual.length){
    console.log("Hay contenido");
}
else{
    var fecha = new Date();
    document.getElementById("fechaAlta").value = fecha.toJSON().slice(0,10);
}

const endPoint3= "/talonario/obtenerNroComprobante/";

$("#talonario").change(function(){
  var valorTalon = $("#talonario").val();

  if( $("#talonario").val() != "" ){
        $.get(endPoint3 + $("#talonario").val(), function(dato){
          $("#nroComprobante").val(dato);
        });
  }else{
        alert("Debe elegir un talonario valido");
         $("#nroComprobante").val("");
  }

});

if($("#cliente").val() > 0){
traducirCliente($("#cliente"));
}

$("#clienteNew").change(function(){
console.log("Actualizar lista operaciones");
});

$("#planPago").change(function(){

$.get("/planPago/obtenerPlanPago/" + $("#planPago").val(), function(datos,status){
    $("#cantCuotas").val(datos.cantCuota);
    $("#porcentajeInteres").val(datos.tasaInteresTotal);
    $("#vencimiento").val(datos.venceLosDias);
})


});

$("#clienteNew").change(function(){

$.get("/ventas/obtenerVentasSinCreditoPorCliente/" + $("#clienteNew").val(), function(datos,status){
        $("#idOperacionNew").empty();
        $("#idOperacionNew").append($("<option value=''> Seleccione operacion </option>"));
        $.each(datos, function (key, value) {
            $("#idOperacionNew").append($("<option value='"+value.id+"'> "+ value.nroComprobante + " - " + value.fechaComprobante + " - $" +  value.total +" </option>"));
        })
    })
})

$("#idOperacionNew").change(function(){
    console.log($("#idOperacionNew").val())
    $.get("/ventas/obtenerTotalPorId/"+ $("#idOperacionNew").val(), function(dato,status){
        $("#capitalNew").val(dato)
    })
})

$(".btnRegenerarCuotas").click(function() {
    crearArrayListCuotas()
})

obtenerCapialCredito($("#idOperacion").val(), 1)
}); //FIN DEL DOCUMENT READY


function traducirCliente(selectCliente){

$.get("/entidadBase/obtenerNombrePorFkCliente/" + selectCliente.val(), function(datos,status){
    //console.log(datos);
    selectCliente.empty();
    selectCliente.append($("<option value='"+datos.id+"'> "+ datos.razonSocial +" </option>"));
         })
}

async function crearArrayListCuotas(){
    function crearArrayList(){
        const arrayList = []

        const tableRows = document.querySelectorAll(".tablaCreditoDetalle tbody tr");
        tableRows.forEach( (row) => {
            const nroCuota = parseInt(row.querySelector("td:nth-child(1)").textContent);
            const monto = parseFloat(row.querySelector("td:nth-child(2)").textContent.replace(",", "")).toFixed(2);
            const vencimiento = row.querySelector("td:nth-child(3)").textContent;
            arrayList.push({ nroCuota, monto, vencimiento });
        } )
        return arrayList;
    }
    function enviarDatosAlServidor(arrayList){
        const url = '/credito/regenerarCuotas/' + $("#id").val();
        fetch(url, {
            method : "POST",
            headers:{
                "Content-Type" : "application/json"
            },
            body: JSON.stringify(arrayList)
        })

    }
    var tiempoEspera = 800;
    function redireccionar() {
        window.location.href= "/credito/form/"+ $("#id").val();
    }

    const dynamicArrayList = await crearArrayList();
    await enviarDatosAlServidor(dynamicArrayList);
    setTimeout(redireccionar, tiempoEspera);
}

function validarEstado(idPago){

fetch('/credito/validarEstado/'+ idPago)
  .then(response => response.text())
  .then(data => {
    const creditoCerrado = (data === "true");
    if(creditoCerrado===true){
        console.log("El credito ya posee un cobro");
        comandosEncabezado = $("#comandos #btnAlta");
        comandosDetalle = $(".comandosDet");
        btnRegenerarCuotas = $(".btnRegenerarCuotas");
        btnRegenerarCuotas.css('display', 'none');
        comandosDetalle.css('visibility', 'hidden');
//        botonEliminarCom.css('display', 'none');

        $('input').prop('readonly', true);
        $('input').css('background-color', 'var(--bs-secondary-bg)');
        $('select').prop('disabled', true);
//        $('textarea').prop('readonly', true);
//        $('textarea').css('background-color', 'var(--bs-secondary-bg)');
    }
    })
  .catch(error => {
    console.error('Error al obtener el valor booleano:', error);
  });

}

function obtenerCapialCredito(idOperacion, idTipoOperacion){
    $.get("/detalleDePago/obtenerCapitalCredito/" + idOperacion + "/" + idTipoOperacion, function(datos,status){
            $("#capitalNew").val(datos)
        })
}


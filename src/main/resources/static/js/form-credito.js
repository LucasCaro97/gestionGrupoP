$(document).ready(function () {
validarEstado($("#id").val())

//formatearInputs($("#interesesTotales"),$("#gastosAdministrativos"), $("#capital"), $("#capitalNew"), $("#totalCredito") )

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
    let idCliente = selectCliente.val()
$.get("/entidadBase/obtenerNombrePorFkCliente/" + idCliente, function(datos,status){
    selectCliente.empty();
    selectCliente.append($("<option value='"+idCliente+"'> "+ datos.razonSocial +" </option>"));
         })
}

async function crearArrayListCuotas(){
    function crearArrayList(){
        const arrayList = []

        const tableRows = document.querySelectorAll(".tablaCreditoDetalle tbody tr");
        tableRows.forEach( (row) => {
            const nroCuota = parseInt(row.querySelector("td:nth-child(1)").textContent);
            const monto = parseFloat(row.querySelector("td:nth-child(4)").textContent.replace(",", "")).toFixed(2);
            const vencimiento = row.querySelector("td:nth-child(6)").textContent;
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
            $("#capital").val(datos)
        })
}

function formatearInputs(celda1, celda2, celda3, celda4, celda5){

    let valor1Formated = new Intl.NumberFormat('en-US').format(celda1.val());
    let valor2Formated = new Intl.NumberFormat('en-US').format(celda2.val());
    let valor3Formated = new Intl.NumberFormat('en-US').format(celda3.val());
    let valor4Formated = new Intl.NumberFormat('en-US').format(celda4.val());
    let valor5Formated = new Intl.NumberFormat('en-US').format(celda5.val());

    celda1.val(valor1Formated);
    celda2.val(valor2Formated);
    celda3.val(valor3Formated);
    celda4.val(valor4Formated);
    celda5.val(valor5Formated);

}


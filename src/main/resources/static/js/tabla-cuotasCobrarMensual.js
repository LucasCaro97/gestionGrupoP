$(document).ready(function () {


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

function redirigirWhatsApp(enlace) {
        // Obtén el valor de data-nombre de la fila donde se hizo clic en el botón
        var fila = enlace.closest('tr');
        var idCreditoDetalle = fila.querySelector('td:nth-child(1)').textContent;
        var nombreCliente = fila.querySelector('td:nth-child(2)').textContent;
        var vencimiento = fila.querySelector('td:nth-child(5)').textContent;
        var totalCuota = fila.querySelector('td:nth-child(13)').textContent;

        var partesFecha = vencimiento.split('-');
        var mes = partesFecha[1];
        var año = partesFecha[2];
        var mesYAnio = mes + '/' + año;

        $.get('/credito/obtenerTelefonoCliente/' + idCreditoDetalle, function(numeroTelefono) {
        // Construye el mensaje con el número de teléfono obtenido
//        var mensaje = 'Hola\n¿Cómo estás?'; // Puedes personalizar el mensaje

        var mensaje = 'Estimado cliente:\n' +
        'Nos comunicamos desde el área de cobranzas de GRUPO PIÑHERO S.A.S. con el fin de informarle que durante el periodo ' + mesYAnio + ' el valor de su cuota será el siguiente:\n' +
        '*$' + totalCuota + '* \n' +
        'Asimismo, le recordamos que le resultará conveniente realizar el pago antes del día ' + vencimiento + '.' +
        ' Puede hacerlo presencialmente en nuestras oficinas ubicadas en Av. San Martin n° 1546 Km. 9 Eldorado, Misiones o bien a través de los siguientes medios de pago.' +
        ' Solicitamos que envíe el comprobante al número 3751-635956.'+
        ' Adjuntamos también la página oficial del índice CAC https://www.cifrasonline.com.ar/indice-cac/'+ '\n'+
        'Quedamos a su disposición, muchas gracias por elegirnos.'


        // Construye la URL de WhatsApp con el número de teléfono y el mensaje
        var urlWhatsApp = 'https://api.whatsapp.com/send?phone=549' + numeroTelefono + '&text=' + encodeURIComponent(mensaje);

        // Abre la URL en una nueva ventana o pestaña del navegador
        window.open(urlWhatsApp);
    });
    }


function enviarMensajeWhatsApp(boton){


    var fila = boton.closest('tr');

    var celdas = fila.getElementsByTagName('td');
    var idCreditoDet = celdas[0].textContent;
    var cliente = celdas[1].textContent;
    var credito = celdas[2].textContent;
    var nroCuota = celdas[3].textContent;
    var vencimiento = celdas[4].textContent;
    var capital = celdas[5].textContent;
    var gastoAdm = celdas[6].textContent;
    var monto = celdas[7].textContent;
    var saldo = celdas[8].textContent;
    var diasAtraso = celdas[9].textContent;
    var ajusteCac = celdas[10].textContent;
    var interes = celdas[11].textContent;
    var total = celdas[12].textContent;

    console.log(cliente)

}

function generarMensaje(){


}
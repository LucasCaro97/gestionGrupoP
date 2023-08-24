$(document).ready(function () {

formatearNumeros($("#totalCompras"),$("#totalVentas"), $("#totalPagos"), $("#totalCobros"))


})

function formatearNumeros(inputCompra, inputVenta, inputPago, inputCobro){
        inputCompraFormated = new Intl.NumberFormat('en-US').format(inputCompra.val());
        inputVentaFormated = new Intl.NumberFormat('en-US').format(inputVenta.val());
        inputPagoFormated = new Intl.NumberFormat('en-US').format(inputPago.val());
        inputCobroFormated = new Intl.NumberFormat('en-US').format(inputCobro.val());

        inputCompra.val(inputCompraFormated)
        inputVenta.val(inputVentaFormated)
        inputPago.val(inputPagoFormated)
        inputCobro.val(inputCobroFormated)
}


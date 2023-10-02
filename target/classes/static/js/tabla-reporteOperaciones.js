$(document).ready(function () {

$("#exportarExcel").click(function(){
    var url = window.location.href;
    var urlObj = new URL(url);
    urlObj.pathname = "/reporting/generarExcelOperaciones";
    var nuevaUrl = urlObj.href;

    const arrayList = []
    const rows = Array.from( $("#tablaOperaciones tbody tr") );

    rows.forEach( (row) => {

    let id = $(row).children().eq(0).text();
    let tipoOperacion = $(row).children().eq(1).text();
    let razonSocial = $(row).children().eq(2).text();
    let fechaOperacion = $(row).children().eq(3).text();
    let talonario = $(row).children().eq(4).text();
    let nroComprobante = $(row).children().eq(5).text();
    let sector = $(row).children().eq(6).text();
    let formaPago = $(row).children().eq(7).text();
    let total = parseFloat( $(row).children().eq(8).text() ) ;
    let observacion = "";

    arrayList.push({id,tipoOperacion,razonSocial,fechaOperacion,talonario,nroComprobante,sector,formaPago,total, observacion})
    })


    console.log(nuevaUrl)
    console.log(arrayList)

     $.ajax({
                url: "/reporting/generarExcelOperaciones",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(arrayList), // Envía tu lista de objetos aquí
                success: function(data) {
                    var blob = new Blob([data], { type: "application/octet-stream" });
                    var url = window.URL.createObjectURL(blob);
                    var a = document.createElement("a");
                    a.href = url;
                    a.download = "operaciones.xlsx";
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                },
                error: function() {
                    // Manejar errores si es necesario
                    console.log("error")
                }
            });


})


//$(document).ready(function() {
//    $("#exportarExcel").click(function() {
//        $.ajax({
//            url: "/reporting/generarExcelOperaciones",
//            type: "POST",
//            contentType: "application/json",
//            data: JSON.stringify(arrayList), // Envía tu lista de objetos aquí
//            success: function(data) {
//                var blob = new Blob([data], { type: "application/octet-stream" });
//                var url = window.URL.createObjectURL(blob);
//                var a = document.createElement("a");
//                a.href = url;
//                a.download = "operaciones.xlsx";
//                document.body.appendChild(a);
//                a.click();
//                window.URL.revokeObjectURL(url);
//            },
//            error: function() {
//                // Manejar errores si es necesario
//                console.log("error")
//            }
//        });
//    });
//});











});
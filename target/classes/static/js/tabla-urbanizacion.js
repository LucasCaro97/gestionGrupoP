$("#verMapa").click(async function(){
    let fila = $(this).closest("tr");
    let idUrb = fila.find("td:first").text();
    let urlDato;
        await $.get("/urbanizacion/obtenerLinkMapa/" + idUrb, function(dato,status){
        urlMapa = dato;
        })
    window.open(urlMapa, '_blank')
})
console.log("AAAAAAAAAAAAAAAAA")

$("tbody tr").each(async function(){
        console.log("Iterando fila: " + $(this).children().eq(0).text());
        let stock = 0;
        let disponibles = 0;
        let vendidos = 0;

        await $.get("/lote/obtenerStockPorUrb/" + $(this).children().eq(0).text(), function(dato,status){
                      stock = dato;
                      })
        await $.get("/lote/obtenerDisponiblesPorUrb/" + $(this).children().eq(0).text(), function(dato,status){
                      disponibles = dato;
                      })
        await $.get("/lote/obtenerVendidosPorUrb/" + $(this).children().eq(0).text(), function(dato,status){
                      vendidos = dato;
                      })

        $(this).children().eq(8).text(stock);
        $(this).children().eq(9).text(disponibles);
        $(this).children().eq(10).text(vendidos);

});


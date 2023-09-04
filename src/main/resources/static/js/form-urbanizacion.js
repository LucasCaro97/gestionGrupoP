$(".boton-alta-producto").click(function(){
     window.location.href = "/lote/altaProductoGrupo/"+$("#id").val();
});

$(".tbody tr").each(function(){
      if($(this).children().eq(5).text() == "true"){
          $(this).children().eq(5).text("Si");
      }else{
          $(this).children().eq(5).text("No");
      }
      //console.log("Iteracion" + $(this).children().eq(11).text());
});



$(".btnVerMapa").click(async function(){
    let idUrb = $("#id").val();
    let urlDato;
    await $.get("/urbanizacion/obtenerLinkMapa/" + idUrb, function(dato,status){
        urlMapa = dato;
    })
 window.open(urlMapa, '_blank')
})

$("#crearLotes").click(function(){
    let idUrbanizacion = $("#id").val()
    let idManzana = $("#manzana").val()
    let cantLotes = $("#cantLotes").val()

   //console.log("generarLotes/" + idUrbanizacion + "/" + idManzana + "/" + cantLotes)
    fetch("/lote/crearLotesPorGrupo/" + idUrbanizacion + "/" + idManzana + "/" + cantLotes, {
        method : "POST",
        headers:{
            "Content-Type" : "application/json"
            }
    })

})


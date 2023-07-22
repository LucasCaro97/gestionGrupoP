$(".boton-alta-producto").click(function(){
     window.location.href = "/lote/altaProductoGrupo/"+$("#id").val();
});

$(".tbody tr").each(function(){
      if($(this).children().eq(4).text() == "true"){
          $(this).children().eq(4).text("Si");
      }else{
          $(this).children().eq(4).text("No");
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
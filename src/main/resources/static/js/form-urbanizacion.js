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
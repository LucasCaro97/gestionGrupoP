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

    async function redireccionar() {
                //GUARDO EL TOTAL DE LA VENTA EN LA TABLA VENTA
                await fetch("/lote/crearLotesPorGrupo/" + idUrbanizacion + "/" + idManzana + "/" + cantLotes, {
                                method : "POST",
                                headers:{
                                    "Content-Type" : "application/json"
                                    }
                            })
        window.location.reload();
        }

    setTimeout(redireccionar, 500);
})


$("#btnCrearManzana").click(function(){
   var url = window.location.href;
   var urlObj = new URL(url);
   urlObj.pathname = "/manzana/createRest";
   var nuevaUrl = urlObj.href;

    if($("#inputManzana").val() == ""){
        alert("La descripcion esta vacia")
    }else{
        const data = {
            descripcion: $("#inputManzana").val(),
            idUrbanizacion: $("#id").val()
            };

        const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type' : 'application/json'
                },
                body: JSON.stringify(data)
                };

        console.log(data)

        fetch(nuevaUrl, requestOptions)
            .then(response =>{
                if(!response.ok){
                    alert("La solicitud no fue exitosa")
                }else{
                    window.location.reload()
                }
            })
            .catch(error => {
                alert("Hubo un error", error)
                })
    }
})


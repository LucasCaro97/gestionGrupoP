function consulta(){
    var id_urb=parseInt(document.getElementById("urbanizacion").value)
    console.log(id_urb)
    alert(id_urb)

    $.ajax({

        url : 'http://localhost:8080/urbanizacion/obtenerManzanasPorId/1',
        data : { 'id' : id_urb },
        type : 'GET',
        dataType : 'json',

        success : function(json) {
                $('<h1/>').text(json.title).appendTo('body');
                $('<div class="content"/>')
                    .html(json.html).appendTo('body');
            },

        error : function(jqXHR, status, error) {
                alert('Disculpe, existió un problema');
            },

            // código a ejecutar sin importar si la petición falló o no
            complete : function(jqXHR, status) {
                alert('Petición realizada');
            }
        });
    }
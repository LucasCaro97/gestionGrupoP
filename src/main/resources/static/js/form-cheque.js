$(document).ready(function () {
    $("#tipoCheque").change(function() {
        $("#chequeSubtipo option").remove()
        $("#chequeSubtipo").append('<option value="">Seleccione subtipo</option>');

        $.get("/tipoSubcheque/obtenerPorTipo/" + $("#tipoCheque").val(), function(datos, status){
            var parseDatos = $.parseJSON(datos);

            $.each(parseDatos, function(key, value){
                $("#chequeSubtipo").append("<option value=" + value.id + ">" + value.descripcion + "</option>");
            })
        })
    })
})






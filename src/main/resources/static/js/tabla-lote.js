$(document).ready(function () {

    $(".tbody tr").each(function(){
        if($(this).children().eq(11).text() == "true"){
            $(this).children().eq(11).text("Si");
        }else{
            $(this).children().eq(11).text("No");
        }
        //console.log("Iteracion" + $(this).children().eq(11).text());

    });

});
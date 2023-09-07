$(document).ready(function () {

    $(".tbody tr").each(function(){
        if($(this).children().eq(5).text() == "true"){
            $(this).children().eq(5).text("Si");
        }else{
            $(this).children().eq(5).text("No");
        }

    });

});
//const temaOscuro = () => {
//    document.querySelector("body").setAttribute("data-bs-theme", "dark");
//    document.querySelector("#dl-icon").setAttribute("class", "bi bi-sun-fill")
//}
//
//const temaClaro = () => {
//    document.querySelector("body").setAttribute("data-bs-theme", "ligth");
//    document.querySelector("#dl-icon").setAttribute("class", "bi bi-moon-fill")
//}
//
//const seleccionarTema = () => {
//    document.querySelector("body").getAttribute("data-bs-theme") === "ligth" ?
//    temaOscuro() : temaClaro();
//}


const botonTema = document.querySelector("#botonTema");

botonTema.addEventListener('click', () => {
    document.body.classList.toggle('dark')

    if(document.body.classList.contains('dark')){
        localStorage.setItem('dark-mode', 'true');
        document.querySelector("body").setAttribute("data-bs-theme", "dark");
        document.querySelector("#dl-icon").setAttribute("class", "bi bi-sun-fill")
    }else{
        localStorage.setItem('dark-mode', 'false');
        document.querySelector("body").setAttribute("data-bs-theme", "ligth");
        document.querySelector("#dl-icon").setAttribute("class", "bi bi-moon-fill")
    }
})

if(localStorage.getItem('dark-mode') === 'true'){
        document.body.classList.toggle('dark')
        document.querySelector("body").setAttribute("data-bs-theme", "dark");
        document.querySelector("#dl-icon").setAttribute("class", "bi bi-sun-fill")
}else{
        document.body.classList = [];
        document.querySelector("body").setAttribute("data-bs-theme", "ligth");
        document.querySelector("#dl-icon").setAttribute("class", "bi bi-moon-fill")
}



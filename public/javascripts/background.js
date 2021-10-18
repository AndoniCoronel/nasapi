function nasaRequest(){
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let data = JSON.parse(this.responseText);
            var urlImage = data["url"];
            let media_type = data["media_type"];
            if (media_type === "video") {
                let neptune="https://www.nasa.gov/sites/default/files/thumbnails/image/pia01492-main.jpg"
                document.body.style.backgroundImage = "url('"+neptune+"')";

            } else{
                document.body.style.backgroundImage = "url('"+urlImage+"')";
            }
        }
    }

    let urlPetition = "https://api.nasa.gov/planetary/apod?api_key=DaFi4M1aSffvFg0EGzfCxWruc6FyhR7wStWMPtxf&"
    xmlhttp.open("GET", urlPetition, true);
    xmlhttp.send();
}

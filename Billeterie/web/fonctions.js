function SetUpdate() {
    var form = document.getElementById("formPanier");
    if (form !== null) {
        form.setAttribute("action", "Panier");
        document.getElementById("submitPanier").setAttribute("value", "Mettre à jour le panier ");
        document.getElementById("submitPanier").setAttribute("class", "BoutonBleu");
    }
}

function CalculerTotal() {
    var prix = document.getElementsByClassName("prix");
    var qte = document.getElementsByClassName("quantite");
    var total = 0;
    for (var i = 0; i < prix.length; i++) {
        var str = prix[i].innerHTML;
        str = str.replace("$", "");
        total += str * qte[i].value;
    }
    document.getElementById("total").innerHTML = total + "$";
}
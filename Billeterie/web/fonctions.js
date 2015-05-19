function SetUpdate() {
    var form = document.getElementById("formPanier");
    if (form !== null) {
        form.setAttribute("action", "Panier");
        document.getElementById("submitPanier").setAttribute("value", "Mettre à jour le panier ");
        document.getElementById("submitPanier").setAttribute("class", "BoutonBleu");
    }
}

function SetDelete(id) {
    var form = document.getElementById("formPanier");
    if (form !== null) {
        form.setAttribute("action", "Delete");
        document.getElementsByName("delete")[0].setAttribute("value", id);
    }
}

function CalculerTotal() {
    var prix = document.getElementsByClassName("prix");
    var qte = document.getElementsByClassName("quantite");
    var total = 0;
    for (var i = 0; i < prix.length; i++) {
        var str = prix[i].innerHTML;
        str = str.replace("$", "");
        if (qte[i].innerHTML !== "Complet")
            total += str * qte[i].value;
    }
    document.getElementById("total").innerHTML = "Total : " + total + "$";
}

function Rechercher() {
    var formCB = document.getElementById("formCB");
    if (formCB !== null) {
        document.getElementById("cbtext").value = document.getElementById("recherche_textbox").value;
        formCB.submit();
    }else {
        document.getElementById("formText").submit();
    }
}
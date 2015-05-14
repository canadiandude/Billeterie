function SetUpdate() {
    var form = document.getElementById("formPanier");
    if (form !== null) {
        form.setAttribute("action", "Panier");
        document.getElementById("submitPanier").setAttribute("value", "Mettre à jour le panier ");
        document.getElementById("submitPanier").setAttribute("class", "BoutonBleu");
    }
}
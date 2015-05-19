/*
 *          OutilsHTML.java
 *
 *  Classe qui gère le code HTML pour tous les servlets 
 *  du package billets
 *  
 *  Crée par    : Saliha Yacoub
 *  Modifié par : François Rioux et Xavier Brosseau   
 *  Remis le    : 20 mai 2015 
 *  Cours       : 420-KEH-LG Systèmes de gestion de bases de données
 *                420-KEK-LG Communication en informatique de gestion
 *
 */
package billets;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import oracle.jdbc.OracleTypes;

public class OutilsHTML
{

    // Le flux de sortie vers le client
    private PrintWriter out;

    public OutilsHTML(PrintWriter pw)
    {
        out = pw;
    }

    /**
     * ouvrirHTML.
     *
     * Écrit l'entête du document HTML et produit la barre de navigation.
     *
     * @param titre le titre de la page
     * @param client le client présentement connecté
     */
    public void ouvrirHTML(String titre, String client)
    {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + titre + "</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">");
        out.println("<link href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700' rel='stylesheet' type='text/css'>\n"
                + "        <link href='http://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>");
        out.println("<script src=\"fonctions.js\"></script>");
        out.println("</head>");
        out.println("<body>");
        produireEntete(client);
    }

    /**
     * fermerHTML
     *
     * Ferme le document HTML.
     */
    public void fermerHTML()
    {
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * produireEntete
     *
     * Produit la barre de navigation contenant la barre de recherche. Si un
     * usager est connecté, son nom et un bouton de déconnection sont affichés
     * Sinon, les boutons connexion et inscription sont disponibles
     *
     * @param client le client présentement connecté
     */
    private void produireEntete(String client)
    {
        out.println("<table class=\"TopMenu\" width=\"100%\">");
        out.println("<tr class=\"TopMenu\">                    \n"
                + "                <td>\n"
                + "                    <img style=\"display: block;\n"
                + "                                margin-left: auto;\n"
                + "                                margin-right: auto\" src=\"http://www.functionkeys.ca/2012Conference/images/MOVIE_TICKETS.png\" width=\"240\" height=\"200\">\n"
                + "                </td>\n"
                + "                <td>\n"
                + "                    <table style=\"width: 100%\">\n"
                + "                        <tr>\n"
                + "                            <td class=\"MenuTopGauche\">\n"
                + "                                <div class=\"TiteSite\">Billetterie Express .com</div>\n"
                + "                            </td>\n"
                + "                            <td class=\"MenuTopDroite\">\n"
                + produireMenuConnexion(client)
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                        <tr>\n"
                + "                            <td class=\"MenuTopGauche\">\n"
                + "                               <form id=\"formText\" action=\"/Billeterie/Recherche\">\n"
                + "                                   <input class=\"css-input\" type=\"text\" name=\"recherche\" id=\"recherche_textbox\">\n"
                + "                                   <img align=\"top\" class=\"BoutonRecherche\" src=\"http://i.imgur.com/Zlb38M3.png\" onclick=\"Rechercher()\">\n"
                + "                               </form>\n"
                + "                            </td>\n"
                + "                            <td class=\"MenuTopDroite\">\n"
                + "                                <a href=\"/Billeterie/Panier\" style=\"padding-right: 40px;\">\n"
                + "                                    <img style=\"padding-top: 5px;\" height=\"75px\" width=\"75px\" src=\"http://i.imgur.com/YVYxaUu.png\">\n"
                + "                                </a>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                    </table>\n"
                + "                </td>\n"
                + "            </tr>");
        out.println("</table>");
    }

    /**
     * produireMenuConnexion
     *
     * Si un usager est connecté, écrit son nom et un bouton de déconnexion dans
     * la barre de navigation. Sinon, ils sont replacés par les boutons
     * connexion et inscription.
     *
     * @param client le client présentement connecté
     */
    private String produireMenuConnexion(String client)
    {
        String menu = "";

        if (client != null)
        {
            menu += "<h3>" + client + "</h3>\n"
                    + "<a href=\"/Billeterie/Deconnexion\" class=\"BoutonBleu\">Déconnexion</a>\n";
        } else
        {
            menu += "<a href=\"/Billeterie/Authentification\" class=\"BoutonBleu\" style=\"margin-bottom:5px\">Connexion</a>\n"
                    + "<a href=\"/Billeterie/Inscription\" class=\"BoutonBleu\">Inscription</a>\n";
        }

        return menu;
    }

    /**
     * produireFormAuthentification
     *
     * Produit le formulaire de connexion des usagers
     */
    public void produireFormAuthentification()
    {
        out.println("<form method=\"post\" action=\"Authentification\">");
        out.println("<table class=\"LoginForm\">");
        out.println("<tr>");
        out.println("<td>Courriel</td>");
        out.println("<td><input type=\"text\" name=\"email\"></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td>Mot de passe</td>");
        out.println("<td><input type=\"password\" name=\"mdp\"></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td></td><td><input type=\"submit\" class=\"BoutonVert\" value=\"Connexion\"></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
    }

    /**
     * produireFormInscription
     *
     * Produit le formulaire d'inscription des usagers
     */
    public void produireFormInscription()
    {
        out.println(""
                + "<form method=\"post\" action=\"Inscription\">\n"
                + "     <table class=\"LoginForm\">\n"
                + "		<tr><td><label for=\"email\">Courriel</label></td><td><input type=\"text\" name=\"email\"></td></tr>\n"
                + "		<tr><td><label for=\"mdp\">Mot de passe</label></td><td><input type=\"password\" name=\"mdp\"></td></tr>\n"
                + "		<tr><td><label for=\"confirm\">Confirmation</label></td><td><input type=\"password\" name=\"confirm\"></td></tr>\n"
                + "		<tr><td><label for=\"nom\">Nom</label></td><td><input type=\"text\" name=\"nom\"></td></tr>\n"
                + "		<tr><td><label for=\"prenom\">Prénom</label></td><td><input type=\"text\" name=\"prenom\"></td></tr>\n"
                + "		<tr><td><label for=\"adresse\">Adresse</label></td><td><input type=\"text\" name=\"adresse\"></td></tr>\n"
                + "		<tr><td><label for=\"telephone\">Téléphone</label></td><td><input type=\"text\" name=\"telephone\"></td></tr>\n"
                + "		<tr><td></td><td><input type=\"submit\" value=\"S'inscrire\" class=\"BoutonVert\"></td></tr>\n"
                + "	</table>\n"
                + "</form>");
    }

    /**
     * produireTableauPanier
     *
     * Produit le panier du client
     *
     * @param rst le ResultSet contenant le panier
     *
     * @return un tableau HTML représentant le panier ou la chaine "VIDE"
     *
     * @throws SQLException si une erreur de base de données survient
     */
    public static String produireTableauPanier(ResultSet rst) throws SQLException
    {
        String tableau = "";
        int total = 0;
        while (rst.next())
        {
            tableau += "<table class=\"PanierItem\">";
            tableau += "<tr>";
            tableau += "<td rowspan=\"4\" align=\"center\" class=\"BordPanier\"><img class=\"affiche\" src=\"" + rst.getString("AFFICHE") + "\"></img></td>";
            tableau += "<td class=\"TitrePanier\">" + rst.getString("TITRE") + " - " + rst.getString("ARTISTE") + "<input type=\"submit\" value=\"X\" onclick=\"SetDelete(" + rst.getInt("NUMACHAT") + ")\" style=\"float:right\"></td>";
            tableau += "<td rowspan=\"2\" align=\"center\" class=\"BordPanier\">Quantité <br />";
            tableau += "<input type=\"number\" min=\"0\" max=\"" + compterPlacesDispo(rst.getInt("QUANTITEBILLETS"), rst.getInt("CODEREPRESENTATION"), rst.getInt("CODESECTION"))
                    + "\" name=\"quantite_" + rst.getInt("NUMACHAT") + "\" onchange=\"SetUpdate();\" value=\"" + rst.getInt("QUANTITEBILLETS") + "\"></td>";
            tableau += "</tr>";

            tableau += "<tr>";
            String date = rst.getObject("DATEDEBUT").toString();
            date = date.substring(0, date.lastIndexOf(":"));
            tableau += "<td>" + date + "</td>";
            tableau += "</tr>";

            tableau += "<tr>";
            tableau += "<td>" + rst.getString("NOMSALLE") + "</td>";
            tableau += "<td align=\"center\">";
            tableau += ecrireCheckBox("print_" + rst.getInt("NUMACHAT"), rst.getString("IMPRIMER").equals("Y"), "Imprimer");
            tableau += "</tr>";

            tableau += "<tr>";
            tableau += "<td>" + rst.getString("NOMSECTION") + "(" + rst.getInt("PRIXSECTION") + "$)" + "</td>";
            tableau += "<td align=\"center\">Sous-total : " + rst.getInt("SOUSTOTAL") + " $</td>";
            total += rst.getInt("SOUSTOTAL");
            tableau += "</tr>";
            tableau += "</table>";
        }
        if (total != 0)
        {
            tableau += "<h3>Total : " + total + " $</h3>";
        } else
        {
            tableau = "VIDE";
        }

        return tableau;
    }

    /**
     * compterPlacesDispo
     *
     * Compte le nombre de places disponibles pour une section et une
     * représentation données
     *
     * @param quantite une quantité de départ, si le client possède déjà des
     * billets
     * @param rep le numéro de représentation
     * @param section le numéro de section
     *
     * @return le nombre de places disponibles au total
     *
     * @throws SQLException si une erreur de base de données survient
     */
    private static int compterPlacesDispo(int quantite, int rep, int section)
            throws SQLException
    {
        int places = quantite;
        //*** Appel à la base de données ***
        ConnexionOracle bd = new ConnexionOracle();
        CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.PLACES_DISPO(?,?) }");
        callstm.registerOutParameter(1, OracleTypes.NUMBER);
        callstm.setInt(2, rep);
        callstm.setInt(3, section);
        callstm.execute();
        //*** Calcul du nombre de places
        places += callstm.getInt(1);
        //*** Fermeture de la base de données ***
        callstm.close();
        bd.deconnecter();

        return places;
    }

    /**
     * afficherPanier
     *
     * Crée le corps du document HTML contenant le panier
     *
     * @param panier le panier à afficher
     */
    public void afficherPanier(String panier)
    {
        out.println("<div class=\"framePanier\" align=\"center\">");
        if (!panier.equals("VIDE"))
        {
            out.println("<form id=\"formPanier\" method=\"post\" action=\"Facture\">");
            out.println("<input type=\"hidden\" name=\"delete\">");
            out.println(panier);
            out.println("<input id=\"submitPanier\" class=\"BoutonVert\" type=\"submit\" value=\"Payer le panier\">");
            out.println("</form>");
        } else
        {
            out.println("<p>Votre panier est vide. Utilisez la barre de recherche pour trouver des spectacles<p>");
        }
        out.println("</div>");
    }

    /**
     * produireTableauRecherche
     *
     * Produit le tableau HTML contenant les résultats de la recherche en se
     * fiant aux préférences contenues dans la requête
     *
     * @param rstRep le ResultSet contenant les spectacles
     * @param request la requête qui contient les préférences de recherche
     *
     * @return Le tableau HTML de la recherche
     *
     * @throws SQLException si une erreur de base de données survient
     */
    public static String produireTableauRecherche(ResultSet rstRep, HttpServletRequest request) throws SQLException
    {
        //CheckBox
        String page = "<table style=\"width: 100%\">\n"
                + "            <tr> \n"
                + "             <form id=\"formCB\" method=\"post\" action=\"Recherche\" accept-charset=\"ISO-8859-1\">"
                + "             <input type=\"hidden\" id=\"cbtext\" name=\"recherche\">\n"
                + ConstruireCBsection(request)
                + "             </form>"
                + "                <td>\n"
                + "                    <div style=\"height: 70vh; overflow:auto;\"><!-- Scroll bar representation -->\n"
                + "                    <table class=\"SpectacleSection\">\n"
                + "                        <!-- Affichage des représentations -->";
        String resultatSpectacle = "";
        //Representation
        while (rstRep.next())
        {
            if (estDansLaRecherche(rstRep.getString(5), rstRep.getString(2), request))
            {
                String date = rstRep.getString(6);
                date = date.substring(0, date.lastIndexOf(":"));
                resultatSpectacle += ""
                        + "                            <tr class=\"Spectacle\">\n"
                        + "                                <td style=\"width: 100px\" >\n"
                        + "                                    <img class=\"affiche\" src=\"" + rstRep.getString(7) + "\">\n"
                        + "                                </td>\n"
                        + "                                <td>\n"
                        + "                                    <div class=\"TitreSpectacle\">" + rstRep.getString(3) + "</div><br>\n"
                        + "                                    <div class=\"NomArtiste\">" + rstRep.getString(4) + "</div>\n"
                        + "                                    <div class=\"SalleSpectacle\">" + rstRep.getString(2) + "</div>\n"
                        + "                                    <div class=\"DateSpectacle\">"
                        + date
                        + "</div>\n"
                        + "                                    <div class=\"AjouterPanier\">\n"
                        + "                                         <form action=\"Acheter\">\n"
                        + produireBoutonAjouter(rstRep.getInt(1))
                        + "                                              <input type=\"hidden\" value=\"" + rstRep.getInt(1) + "\" name=\"representation\">\n"
                        + "                                         </form>\n"
                        + "                                    </div>\n"
                        + "                                </td>\n"
                        + "                            </tr>\n";
            }
        }
        //Fermeture de representation
        if (!resultatSpectacle.equals(""))
        {
            page += resultatSpectacle;
        } else
        {
            page += "<tr class=\"Spectacle\" style=\"text-align: center;\">\n"
                    + "<td>"
                    + "<h1>Votre recherche n'a pas été fructueuse.. :(<h1>"
                    + "<div style=\"font-size: 20px;\">Vérifier l'orthographe et/ou cocher des éléments de spécification de recherche<div/>"
                    + "<td/>"
                    + "<tr/>";

        }
        page += "       </table>\n"
                + "                    </div>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>";
        return page;
    }

    /**
     * produireTableauRecherche
     *
     * Produit le tableau HTML contenant les résultats de la recherche en se
     * fiant aux préférences contenues dans un cookie
     *
     * @param rstRep le ResultSet contenant les spectacles
     * @param params Les préférences de recherche provenant du cookie
     *
     * @return Le tableau HTML de la recherche
     *
     * @throws SQLException si une erreur de base de données survient
     */
    public static String produireTableauRecherche(ResultSet rstRep, String params) throws SQLException
    {
        //CheckBox
        String page = "<table style=\"width: 100%\">\n"
                + "            <tr> \n"
                + "             <form id=\"formCB\" method=\"post\" action=\"Recherche\" accept-charset=\"ISO-8859-1\">"
                + "             <input type=\"hidden\" id=\"cbtext\" name=\"recherche\">\n"
                + ConstruireCBsection(params)
                + "             </form>"
                + "                <td>\n"
                + "                    <div style=\"height: 70vh; overflow:auto;\"><!-- Scroll bar representation -->\n"
                + "                    <table class=\"SpectacleSection\">\n"
                + "                        <!-- Affichage des représentations -->";
        String resultatSpectacle = "";

        //Representation
        while (rstRep.next())
        {
            if (estDansLaRecherche(rstRep.getString(5), rstRep.getString(2), params))
            {
                String date = rstRep.getString(6);
                date = date.substring(0, date.lastIndexOf(":"));
                resultatSpectacle += ""
                        + "                            <tr class=\"Spectacle\">\n"
                        + "                                <td style=\"width: 100px\" >\n"
                        + "                                    <img class=\"affiche\" src=\"" + rstRep.getString(7) + "\">\n"
                        + "                                </td>\n"
                        + "                                <td>\n"
                        + "                                    <div class=\"TitreSpectacle\">" + rstRep.getString(3) + "</div><br>\n"
                        + "                                    <div class=\"NomArtiste\">" + rstRep.getString(4) + "</div>\n"
                        + "                                    <div class=\"SalleSpectacle\">" + rstRep.getString(2) + "</div>\n"
                        + "                                    <div class=\"DateSpectacle\">"
                        + date
                        + "</div>\n"
                        + "                                    <div class=\"AjouterPanier\">\n"
                        + "                                         <form action=\"Acheter\">\n"
                        + produireBoutonAjouter(rstRep.getInt(1))
                        + "                                              <input type=\"hidden\" value=\"" + rstRep.getInt(1) + "\" name=\"representation\">\n"
                        + "                                         </form>\n"
                        + "                                    </div>\n"
                        + "                                </td>\n"
                        + "                            </tr>\n";
            }
        }
        //Fermeture de representation
        if (!resultatSpectacle.equals(""))
        {
            page += resultatSpectacle;
        } else
        {
            page += "<tr class=\"Spectacle\" style=\"text-align: center;\">\n"
                    + "<td>"
                    + "<h1>Votre recherche n'a pas été fructueuse.. :(<h1>"
                    + "<div style=\"font-size: 20px;\">Vérifier l'orthographe et/ou cocher des éléments de spécification de recherche<div/>"
                    + "<td/>"
                    + "<tr/>";

        }
        page += "       </table>\n"
                + "                    </div>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>";
        return page;
    }

    /**
     * estDansLaRecherche
     *
     * Vérifie si une représentation correspond aux critères de recherche
     * contenus dans la requête
     *
     * @param categorie la catégorie du spectacle
     * @param salle la salle du spectacle
     * @param request la requête qui contient les préférences de recherche
     *
     * @return vrai si le spectacle correspond aux critères de recherche, faux
     * sinon
     */
    private static boolean estDansLaRecherche(String categorie, String salle, HttpServletRequest request)
    {
        return request.getParameter(categorie) != null && request.getParameter(salle) != null;
    }

    /**
     * estDansLaRecherche
     *
     * Vérifie si une représentation correspond aux critères de recherche
     * contenus dans un cookie
     *
     * @param categorie la catégorie du spectacle
     * @param salle la salle du spectacle
     * @param params Les préférences de recherche provenant du cookie
     *
     * @return vrai si le spectacle correspond aux critères de recherche, faux
     * sinon
     */
    private static boolean estDansLaRecherche(String categorie, String salle, String params)
    {
        return params.contains(categorie) && params.contains(salle);
    }

    /**
     * produireBoutonAjouter
     *
     * Vérifie s'il reste des places pour une représentation donnée. Si oui, il
     * crée un bouton pour acheter, sinon un indicateur complet
     *
     * @param numrep le numéro de représentation
     *
     * @return Le bouton ou un indicateur complet
     *
     * @throws SQLException si une erreur de base de données survient
     */
    private static String produireBoutonAjouter(int numrep)
            throws SQLException
    {
        //*** Appel à la base de données ***
        ConnexionOracle bd = new ConnexionOracle();
        CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.TOTAL_PLACES_DISPO(?) }");
        callstm.registerOutParameter(1, OracleTypes.NUMBER);
        callstm.setInt(2, numrep);
        callstm.execute();

        CallableStatement callPrix = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_PRIX_MIN(?) }");
        callPrix.registerOutParameter(1, OracleTypes.NUMBER);
        callPrix.setInt(2, numrep);
        callPrix.execute();

        String bouton = "À partir de " + callPrix.getInt(1) + " $ <br/>";

        if (callstm.getInt(1) > 0)
        {
            bouton += "<input type=\"submit\" value=\"Ajouter au panier\" class=\"BoutonVert\">\n";
        } else
        {
            bouton += "<h5>Complet</h5>";
        }

        //*** Fermeture de la base de données ***
        callstm.close();
        callPrix.close();
        bd.deconnecter();

        return bouton;
    }

    /**
     * ecrireCheckBox
     *
     * Crée un input de type checkox
     *
     * @param name le name du input
     * @param checked si le checkbox doit être coché
     * @param label le label qui accompagne le checkbox
     *
     * @return le checkbox et son label
     */
    private static String ecrireCheckBox(String name, boolean checked, String label)
    {
        String cb = "<input type=\"checkbox\" name=\"" + name + "\" id=\"" + name + "\" onchange=\"SetUpdate();\"";

        if (checked)
        {
            cb += " checked>";
        } else
        {
            cb += ">";
        }
        cb += "<label for=\"" + name + "\">" + label + "</label>";
        return cb;
    }

    /**
     * ConstruireCBsection
     *
     * Produit la section où l'usager peut choisir ses préférences de recherche
     * Vérifie dans la requête si un checkbox doit être coché
     *
     * @param request la requête qui contient les préférences de recherche
     * @return le menu contenant les checkbox
     */
    public static String ConstruireCBsection(HttpServletRequest request)
    {
        String CbSection = "";
        try
        {
            //*** Appel à la base de données ***
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_SALLE() }");
            callstm.registerOutParameter(1, OracleTypes.CURSOR);
            callstm.execute();
            ResultSet restSalles = (ResultSet) callstm.getObject(1);

            CallableStatement callstm2 = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_CATEGORIE() }");
            callstm2.registerOutParameter(1, OracleTypes.CURSOR);
            callstm2.execute();
            ResultSet restCategories = (ResultSet) callstm2.getObject(1);

            CbSection = "<td class=\"CBsection\">\n"
                    + "                    <div class=\"TitreCB\">TYPE DE SPECTACLE</div>\n";
            while (restCategories.next())
            {
                CbSection += ecrireCheckBox(restCategories.getString(2), request.getParameter(restCategories.getString(2)) != null, restCategories.getString(2)) + "</br>";
            }
            CbSection += "</br>\n"
                    + "                    <hr style=\"width:70%\" align=\"left\"></hr>\n"
                    + "                    <div class=\"TitreCB\">SALLE DE SPECTACLE</div>\n";
            while (restSalles.next())
            {
                CbSection += ecrireCheckBox(restSalles.getString(2), request.getParameter(restSalles.getString(2)) != null, restSalles.getString(2)) + "</br>";
            }
            CbSection += "</br>\n"
                    + "                </td>\n";

            //*** Fermeture de la base de données ***
            callstm.close();
            callstm2.close();
            bd.deconnecter();
        } catch (SQLException ex)
        {
            CbSection += ex.getMessage();
        }
        return CbSection;
    }

    /**
     * ConstruireCBsection
     *
     * Produit la section où l'usager peut choisir ses préférences de recherche
     * Vérifie dans un cookie si un checkbox doit être coché
     *
     * @param params Les préférences de recherche provenant du cookie
     * @return le menu contenant les checkbox
     */
    public static String ConstruireCBsection(String params)
    {
        String CbSection = "";
        try
        {
            //*** Appel à la base de données ***
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_SALLE() }");
            callstm.registerOutParameter(1, OracleTypes.CURSOR);
            callstm.execute();
            ResultSet restSalles = (ResultSet) callstm.getObject(1);

            CallableStatement callstm2 = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_CATEGORIE() }");
            callstm2.registerOutParameter(1, OracleTypes.CURSOR);
            callstm2.execute();
            ResultSet restCategories = (ResultSet) callstm2.getObject(1);

            CbSection = "<td class=\"CBsection\">\n"
                    + "                    <div class=\"TitreCB\">TYPE DE SPECTACLE</div>\n";
            while (restCategories.next())
            {
                CbSection += ecrireCheckBox(restCategories.getString(2), params.contains(restCategories.getString(2)), restCategories.getString(2)) + "</br>";
            }
            CbSection += "</br>\n"
                    + "                    <hr style=\"width:70%\" align=\"left\"></hr>\n"
                    + "                    <div class=\"TitreCB\">SALLE DE SPECTACLE</div>\n";
            while (restSalles.next())
            {
                CbSection += ecrireCheckBox(restSalles.getString(2), params.contains(restSalles.getString(2)), restSalles.getString(2)) + "</br>";
            }
            CbSection += "</br>\n"
                    + "                </td>\n";

            //*** Fermeture de la base de données ***
            callstm.close();
            callstm2.close();
            bd.deconnecter();
        } catch (SQLException ex)
        {
            CbSection += ex.getMessage();
        }
        return CbSection;
    }

    /**
     * produireFacture
     *
     * Produit le tableau HTML contenant la facure
     *
     * @param rst le ResultSet contenant la facture
     *
     * @return le tableau HTML contenant la facure
     *
     * @throws SQLException si une erreur de base de données survient
     */
    public static String produireFacture(ResultSet rst)
            throws SQLException
    {
        String facture = "";

        facture
                += facture += "<table class=\"Facture\">";
        facture += "<tr><td>Spectacle</td><td>Quantité</td><td>Total</td></tr>";
        int total = 0;
        int billets = 0;
        int nofacture = 0;
        while (rst.next())
        {
            facture += "<tr>";
            facture += "<td>" + rst.getString("TITRE") + " - " + rst.getString("ARTISTE") + "</td>";
            facture += "<td>" + rst.getInt("QUANTITEBILLETS") + "</td>";
            billets += rst.getInt("QUANTITEBILLETS");
            facture += "<td>" + rst.getInt("SOUSTOTAL") + " $</td>";
            total += rst.getInt("SOUSTOTAL");
            nofacture = rst.getInt("NUMFACTURE");
        }

        facture += "<tr><td>Total</td><td>" + billets + "</td><td>" + total + " $</td></tr>";
        facture += "</table>";

        facture = "<h1>Facture n°" + nofacture + "</h1>" + facture;

        return facture;
    }

    /**
     * afficherPanier
     *
     * Crée le corps du document HTML contenant le panier
     *
     * @param facture la facture à afficher
     */
    public void afficherFacture(String facture)
    {
        out.println("<div class=\"framePanier\" align=\"center\">");
        out.println(facture);
        out.println("</div>");
    }
    
    /**
     * produireFormAcheter
     * 
     * Produit le formulaire permettant d'ajouter des billets au panier
     * 
     * @param rstRep le ResultSet contennant le spectacle
     * @param rstSec le ResultSet contennant les sections
     * 
     * @return le formulaire d'ajout de billets
     * 
     * @throws SQLException si une erreur de base de données survient
     */
    public static String produireFormAcheter(ResultSet rstRep, ResultSet rstSec)
            throws SQLException
    {
        String date = rstRep.getObject("DATEDEBUT").toString();
        date = date.substring(0, date.lastIndexOf(":"));
        String acheter = "";
        acheter += ""
                + "        <table style=\"margin: auto; margin-top:10px;\">\n"
                + "            <tr>\n"
                + "                <td rowspan=\"3\"><img class=\"GrosseAffiche\" src=\"" + rstRep.getString("AFFICHE") + "\"></td>\n"
                + "                <td>" + rstRep.getString("TITRE") + "<br />" + rstRep.getString("ARTISTE") + "</td>\n"
                + "            </tr>\n"
                + "            <tr>\n"
                + "                <td>" + date + "   " + rstRep.getString("NOMSALLE") + "</td>\n"
                + "            </tr>\n"
                + "            <tr>\n"
                + "                <td>\n"
                + "                    <form method=\"post\" action=\"Acheter\">\n"
                + "                        <input type=\"hidden\" name=\"representation\" value=\"" + rstRep.getInt("NUMREPRESENTATION") + "\">"
                + "                        <table>\n"
                + "                            <tr>\n"
                + "                                <td>\n"
                + "                                    <table>\n";
        while (rstSec.next())
        {
            int places = compterPlacesDispo(0, rstRep.getInt("NUMREPRESENTATION"), rstSec.getInt("NUMSECTION"));
            String input = places > 0 ? "<input type=\"number\" name=\"" + rstSec.getInt("NUMSECTION") + "\" min=\"0\" max=\"" + places + "\" onchange=\"CalculerTotal()\" class=\"quantite\">"
                    : "<p class=\"quantite\">Complet</p>";
            acheter += ""
                    + "                                        <tr>\n"
                    + "                                            <td>" + rstSec.getString("NOM") + "</td><td class=\"prix\">" + rstSec.getInt("PRIXSECTION") + "$</td>"
                    + "                                            <td>" + input + "</td>\n"
                    + "                                            <td><input type=\"hidden\" name=\"sections\" value=\"" + rstSec.getInt("NUMSECTION") + "\"></td>"
                    + "                                        </tr>";
        }
        acheter += "                                    </table>\n"
                + "                                </td>\n"
                + "                            </tr>\n"
                + "                            <tr>\n"
                + "                                <td id=\"total\">Total : 0$</td>\n"
                + "                            </tr>\n"
                + "                            <tr>\n"
                + "                                <td><input type=\"submit\" class=\"BoutonVert\" value=\"Ajouter au panier\"></td>\n"
                + "                            </tr>\n"
                + "                        </table>\n"
                + "                    </form>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>";

        return acheter;
    }
}

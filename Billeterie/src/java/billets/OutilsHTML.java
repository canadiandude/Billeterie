/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Francois
 */
public class OutilsHTML
{

    private PrintWriter out;

    public OutilsHTML(PrintWriter pw)
    {
        out = pw;
    }

    public void ouvrirHTML()
    {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Recherche</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">");
        out.println("<link href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700' rel='stylesheet' type='text/css'>\n"
                + "        <link href='http://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>");
        out.println("<script src=\"fonctions.js\"></script>");
        out.println("</head>");
        out.println("<body>");
        produireEntete();
    }

    public void fermerHTML()
    {
        out.println("</body>");
        out.println("</html>");
    }

    private void produireEntete()
    {
        out.println("<table class=\"TopMenu\" width=\"100%\">");
        //out.println("<a href='/Billeterie/Authentification'>Connexion</a>");
        //out.println("<a href='/Billeterie/Panier'>Panier</a>");
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
                + "                                <a href=\"/Billeterie/Authentification\" class=\"BoutonBleu\" style=\"margin-bottom:5px\">Connexion</a>\n"
                + "                                <a href=\"/Billeterie/Inscription\" class=\"BoutonBleu\">Inscription</a>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                        <tr>\n"
                + "                            <td class=\"MenuTopGauche\">\n"
                + "                               <form action=\"/Billeterie/Recherche\">\n"
                + "                                   <input class=\"css-input\" type=\"text\" name=\"recherche\">\n"
                + "                                   <input type=\"image\" align=\"top\" height=\"43px\" width=\"43px\" src=\"http://i.imgur.com/Zlb38M3.png\">\n"
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

    public static String produireTableauPanier(ResultSet rst) throws SQLException
    {
        String tableau = "";
        int total = 0;
        while (rst.next())
        {
            tableau += "<table class=\"PanierItem\">";
            tableau += "<tr>";
            tableau += "<td rowspan=\"4\"><img class=\"affiche\" src=\"" + rst.getString("AFFICHE") + "\"></img></td>";
            tableau += "<td class=\"TitrePanier\">" + rst.getString("TITRE") + " - " + rst.getString("ARTISTE") + "</td>";
            tableau += "<td rowspan=\"2\" align=\"center\">Quantité <br />";
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
            tableau += ecrireCheckBox("print_" + rst.getInt("NUMACHAT"), rst.getString("IMPRIMER").equals("Y"));
            tableau += "<label for=\"print_" + rst.getInt("NUMACHAT") + "\">Imprimer*</label></td>";
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

    private static int compterPlacesDispo(int quantite, int rep, int section)
            throws SQLException
    {
        int places = quantite;
        ConnexionOracle bd = new ConnexionOracle();
        CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.PLACES_DISPO(?,?) }");
        callstm.registerOutParameter(1, OracleTypes.NUMBER);
        callstm.setInt(2, rep);
        callstm.setInt(3, section);
        callstm.execute();
        places += callstm.getInt(1);
        callstm.close();
        bd.deconnecter();
        return places;
    }

    public void afficherPanier(String panier)
    {
        out.println("<div class=\"framePanier\" align=\"center\">");
        if (!panier.equals("VIDE"))
        {
            out.println("<form id=\"formPanier\" method=\"post\" action=\"Facture\">");
            out.println(panier);
            out.println("<input id=\"submitPanier\" class=\"BoutonVert\" type=\"submit\" value=\"Payer le panier\">");
            out.println("</form>");
        } else
        {
            out.println("<p>Votre panier est vide. Utilisez la barre de recherche pour trouver des spectacles<p>");
        }
        out.println("</div>");
    }

    public static String produireTableauRecherche(ResultSet rstRep) throws SQLException
    {
        //CheckBox
        String page = "<table style=\"width: 100%\">\n"
                + "            <tr> \n"
                + "                <td class=\"CBsection\">\n"
                + "                    <div class=\"TitreCB\">TYPE DE SPECTACLE</div>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"Musique\">Musique</br>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"Sports\" checked>Sports</br>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"ArtEtTheatre\" checked>Arts & Théâtre</br>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"Famille\" checked>Famille</br>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"Festivals\" checked>Festivals</br>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"Humour\" checked>Humour</br>\n"
                + "                    </br>\n"
                + "                    <hr style=\"width:70%\" align=\"left\"></hr>\n"
                + "                    <div class=\"TitreCB\">SALLE DE SPECTACLE</div> \n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"SaintDenis\" checked>Saint-Denis</br>\n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"CentreBell\">Centre Bell</br>                    \n"
                + "                    <input type=\"checkbox\" name=\"salle\" value=\"Zenith\">Le Zénith</br></br>\n"
                + "                </td>\n"
                + "                <td>\n"
                + "                    <div style=\"height: 70vh; overflow:auto;\"><!-- Scroll bar representation -->\n"
                + "                    <table class=\"SpectacleSection\">\n"
                + "                        <!-- Affichage des représentations -->";
        //Representation
        while (rstRep.next())
        {
            page += "       <form>\n"
                    + "                            <tr class=\"Spectacle\">\n"
                    + "                                <td style=\"width: 100px\" >\n"
                    + "                                    <img class=\"affiche\" src=\"" + rstRep.getString(7) + "\">\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    <div class=\"TitreSpectacle\">" + rstRep.getNString(3) + "</div><br>\n"
                    + "                                    <div class=\"NomArtiste\">" + rstRep.getNString(4) + "</div>\n"
                    + "                                    <div class=\"SalleSpectacle\">" + rstRep.getNString(2) + "</div>\n"
                    + "                                    <div class=\"DateSpectacle\">" + rstRep.getNString(6) + "</div>\n"
                    + "                                    <div class=\"AjouterPanier\">\n"
                    + "                                        <input type=\"submit\" value=\"Ajouter au panier\" id=\"repID\">\n"
                    + "                                        <!-- input type=\"hidden\" value=\"\" name=\"representation\" -->\n"
                    + "                                    </div>\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                        </form>";
        }
        //Fermeture de representation
        page += "       </table>\n"
                + "                    </div>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>";
        return page;
    }

    private static String ecrireCheckBox(String name, boolean checked)
    {
        String cb = "<input type=\"checkbox\" name=\"" + name + "\" id=\"" + name + "\" onchange=\"SetUpdate();\"";

        if (checked)
        {
            cb += " checked>";
        } else
        {
            cb += ">";
        }

        return cb;
    }

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

    public void afficherFacture(String facture)
    {
        out.println("<div class=\"framePanier\" align=\"center\">");
        out.println(facture);
        out.println("</div>");
    }
}

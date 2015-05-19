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
import javax.servlet.http.HttpServletRequest;
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
        produireEntete(titre, client);
    }

    public void fermerHTML()
    {
        out.println("</body>");
        out.println("</html>");
    }

    private void produireEntete(String titre, String client)
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
        //Representation
        while (rstRep.next())
        {
            if (estDansLaRecherche(rstRep.getString(5), rstRep.getString(2), request))
            {
                String date = rstRep.getString(6);
                date = date.substring(0, date.lastIndexOf(":"));
                page += ""
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
        page += "       </table>\n"
                + "                    </div>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>";
        return page;
    }

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
        //Representation
        while (rstRep.next())
        {
            if (estDansLaRecherche(rstRep.getString(5), rstRep.getString(2), params))
            {
                String date = rstRep.getString(6);
                date = date.substring(0, date.lastIndexOf(":"));
                page += ""
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
        page += "       </table>\n"
                + "                    </div>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>";
        return page;
    }

    private static boolean estDansLaRecherche(String categorie, String salle, HttpServletRequest request)
    {
        return request.getParameter(categorie) != null && request.getParameter(salle) != null;
    }

    private static boolean estDansLaRecherche(String categorie, String salle, String params)
    {
        return params.contains(categorie) && params.contains(salle);
    }

    private static String produireBoutonAjouter(int numrep)
            throws SQLException
    {
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

        callstm.close();
        callPrix.close();
        bd.deconnecter();

        return bouton;
    }

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

    public static String ConstruireCBsection(HttpServletRequest request)
    {
        String CbSection = "";
        try
        {
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
            callstm.close();
            callstm2.close();
            bd.deconnecter();
        } catch (SQLException ex)
        {
            CbSection += ex.getMessage();
        }
        return CbSection;
    }

    public static String ConstruireCBsection(String params)
    {
        String CbSection = "";
        try
        {
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
            callstm.close();
            callstm2.close();
            bd.deconnecter();
        } catch (SQLException ex)
        {
            CbSection += ex.getMessage();
        }
        return CbSection;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        out.println("<a href='/Billeterie/Authentification'>Connexion</a>");
        out.println("<a href='/Billeterie/Panier'>Panier</a>");
        out.println("<hr />");
    }
    
    public void produireFormAuthentification()
    {
        out.println("<form method=\"post\" action=\"Authentification\">");
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>Courriel</td>");
        out.println("<td><input type=\"text\" name=\"email\"></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td>Mot de passe</td>");
        out.println("<td><input type=\"password\" name=\"mdp\"></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td></td><td><input type=\"submit\" value=\"Connexion\"></td>");
        out.println("</tr>");
        out.println("</table>");        
        out.println("</form>");
    }

    public static String produireTableauPanier(String client, ResultSet rst) throws SQLException
    {
        String tableau = "<table>";
        
        while (rst.next())
        {
            tableau += "<tr>";
            tableau += "<td rowspan=\"4\"><img src=\"" + rst.getString("AFFICHE") + "\" style=\"height:128px\"></img></td>";
            tableau += "<td>" + rst.getString("TITRE") + " - " + rst.getString("ARTISTE") + "</td>";
            tableau += "<td rowspan=\"2\"><input type=\"number\" onchange=\"SetUpdate();\" value=\""+ rst.getInt("QUANTITEBILLETS") +"\"></td>";
            tableau += "</tr>";
            
            tableau += "<tr>";
            String date = rst.getObject("DATEDEBUT").toString();
            date = date.substring(0, date.lastIndexOf(":"));
            tableau += "<td>" + date + "</td>";
            tableau += "</tr>";
            
            tableau += "<tr>";
            tableau += "<td>" + rst.getString("NOMSALLE") + "</td>";
            tableau += "<td rowspan=\"2\">"+ rst.getInt("SOUSTOTAL") +" $</td>";
            tableau += "</tr>";
            
            tableau += "</tr>";
            tableau += "<td>" + rst.getString("NOMSECTION") + "(" + rst.getInt("PRIXSECTION") +"$)" + "</td>";
            tableau += "</tr>";
        }
        
        tableau += "</table>";
        return tableau;
    }
    
    public void afficherPanier(String panier)
    {
        out.println("<script src=\"fonctions.js\"></script>");
        out.println("<form id=\"formPanier\" method=\"post\" action=\"Facture\">");
        out.println(panier);
        out.println("<input id=\"submitPanier\" type=\"submit\" value=\"Payer\">");
        out.println("</form>");
    }
}

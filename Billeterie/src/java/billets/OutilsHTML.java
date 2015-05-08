/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.*;

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
        String url = "/Billeterie/Authentification";
        out.println("<a href='" + url + "'>Connexion</a>");
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
}

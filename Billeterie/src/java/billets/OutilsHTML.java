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
    }
    
    public void fermerHTML()
    {
        out.println("</body>");
        out.println("</html>");
    }
    
    public void produireEntete()
    {
        out.println("<h1>This is a header</h1>");
        out.println("<hr />");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Francois
 */
@WebServlet(name = "Inscription", urlPatterns =
{
    "/Inscription"
})
public class Inscription extends HttpServlet
{

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);
            html.ouvrirHTML("Inscription", (String) request.getSession().getAttribute("client"));
            if (request.getSession().getAttribute("inscription") != null)
            {
                out.println("<h3>" + (String)request.getSession().getAttribute("inscription") + "</h3>");
                request.getSession().setAttribute("inscription", null);
            }

            html.produireFormInscription();
            html.fermerHTML();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String confirm = request.getParameter("confirm");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String adresse = request.getParameter("adresse");
        String telephone = request.getParameter("telephone");

        String erreur = "";
        if (email.isEmpty())
        {
            erreur += "le courriel ";
        }

        if (mdp.isEmpty())
        {
            erreur += "le mot de passe ";
        }

        if (confirm.isEmpty() || !confirm.equals(mdp))
        {
            erreur += "confirmation du mot de passe, ";
        }

        if (nom.isEmpty())
        {
            erreur += "votre nom ";
        }

        if (prenom.isEmpty())
        {
            erreur += "votre prenom ";
        }

        if (adresse.isEmpty())
        {
            erreur += "votre adresse ";
        }

        if (telephone.isEmpty())
        {
            erreur += "votre telephone ";
        }

        if (erreur.isEmpty())
        {
            try
            {
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ call PKG_BILLETS.INSERT_CLIENT(?,?,?,?,?,?) }");
                callstm.setString(1, email);
                callstm.setString(2, mdp);
                callstm.setString(3, nom);
                callstm.setString(4, prenom);
                callstm.setString(5, adresse);
                callstm.setString(6, telephone);
                callstm.execute();
                
                request.getSession().setAttribute("client", request.getParameter("email"));
                response.sendRedirect("Panier");
            }
            catch (SQLException e)
            {
                response.sendRedirect("erreur.html");
            } 
        } else
        {
            request.getSession().setAttribute("inscription", erreur);
            response.sendRedirect("Inscription");
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}

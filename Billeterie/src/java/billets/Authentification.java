/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

/**
 *
 * @author Francois
 */
@WebServlet(name = "Authentification", urlPatterns =
{
    "/Authentification"
})
public class Authentification extends HttpServlet
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
            html.ouvrirHTML();

            html.produireFormAuthentification();

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
        int existe = 0;
        try
        {
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AUTHENTIFIER(?,?) }");
            callstm.registerOutParameter(1, OracleTypes.NUMBER);
            callstm.setString(2, request.getParameter("email"));
            callstm.setString(3, request.getParameter("mdp"));
            callstm.execute();
            existe = callstm.getInt(1);
            callstm.close();
            bd.deconnecter();
        } 
        catch (SQLException sqle)
        {
            response.sendRedirect("erreur.html");
        }

        try (PrintWriter out = response.getWriter())
        {
            if (existe != 0)
            {
                request.getSession().setAttribute("client", request.getParameter("email"));
                response.sendRedirect("Panier");
            } 
            else
            {
                response.setContentType("text/html;charset=UTF-8");
                OutilsHTML html = new OutilsHTML(out);
                html.ouvrirHTML();
                out.println("<h1 style=\"color: red\">Courriel/Mot de passe invalide !</h1>");
                html.produireFormAuthentification();
                html.fermerHTML();
            }
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

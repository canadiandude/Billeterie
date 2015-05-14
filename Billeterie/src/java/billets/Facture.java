/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Francois
 */
@WebServlet(name = "Facture", urlPatterns =
{
    "/Facture"
})
public class Facture extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        payerPanier(request, response);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);
            html.ouvrirHTML();
            out.println("Payer");
            html.fermerHTML();
        }
    }

    private void payerPanier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String client = (String) request.getSession().getAttribute("client");
        String facture = "<h1>Une erreur est survenue</h1>";// Message par défaut
        if (client != null)
        {
            try
            {
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callPayer = bd.prepareCall("{ call PKG_BILLETS.PAYER_PANIER(?) }");
                callPayer.setString(1, client);
                callPayer.execute();
                callPayer.close();
                
                CallableStatement callFacture = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_FACTURE(?) }");
                callFacture.registerOutParameter(1, OracleTypes.CURSOR);
                callFacture.setString(2, client);
                callFacture.execute();
                facture = OutilsHTML.produireFacture((ResultSet) callFacture.getObject(1));
                callFacture.close();
                
                bd.deconnecter();
            } catch (SQLException sqle)
            {
                response.sendRedirect("erreur.html");
            }

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter())
            {
                OutilsHTML html = new OutilsHTML(out);
                html.ouvrirHTML();
                out.println(facture);
                html.fermerHTML();
            }
        } else
        {
            response.sendRedirect("Authentification");
        }
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
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

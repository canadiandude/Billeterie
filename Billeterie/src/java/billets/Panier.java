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
@WebServlet(name = "Panier", urlPatterns =
{
    "/Panier"
})
public class Panier extends HttpServlet
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
        String client = (String) request.getSession().getAttribute("client");
        String tableau = "<h1>Une erreur est survenue</h1>";// Message par défaut
        if (client != null)
        {
            try
            {
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_PANIER(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);
                callstm.setString(2, client);
                callstm.execute();
                tableau = OutilsHTML.produireTableauPanier(client, (ResultSet) callstm.getObject(1));
                callstm.close();
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
                html.afficherPanier(tableau);
                html.fermerHTML();
            }
        } else
        {
            response.sendRedirect("Authentification");
        }
    }

    private void mettreAJourPanier(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        try
        {
            String client = (String)request.getSession().getAttribute("client");
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callList = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_PANIER(?) }");
            callList.registerOutParameter(1, OracleTypes.CURSOR);
            callList.setString(2, client);
            callList.execute();
            ResultSet rst = (ResultSet) callList.getObject(1);
            
            while (rst.next())
            {
                CallableStatement callUpdate = bd.prepareCall("{ call PKG_BILLETS.UPDATE_PANIER(?,?,?) }");
                int numachat = rst.getInt("NUMACHAT");
                callUpdate.setInt(1, numachat);
                int qte = Integer.parseInt(request.getParameter("quantite_"+numachat));
                callUpdate.setInt(2, qte);
                String print = request.getParameter("print_"+numachat) != null ? "Y" : "N";
                callUpdate.setString(3, print);
                callUpdate.execute();
            }

            callList.close();
            bd.deconnecter();
        } catch (SQLException sqle)
        {
            response.sendRedirect("erreur.html");
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
        mettreAJourPanier(request, response);
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

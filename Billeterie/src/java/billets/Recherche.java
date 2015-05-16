/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import static billets.OutilsHTML.produireTableauRecherche;
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
@WebServlet(name = "Recherche", urlPatterns =
{
    "/Recherche"
})
public class Recherche extends HttpServlet
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);
            
            html.ouvrirHTML("Recherche", (String) request.getSession().getAttribute("client"));
            
            try
            {
                String RechercheText  = request.getParameter("recherche");
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_RESULTATRECHERCHE(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);
                //if (RechercheText == null)
                //{
                //    RechercheText = "*";
                //}                    
                callstm.setString(2, RechercheText);
                callstm.execute();
                ResultSet rest = (ResultSet) callstm.getObject(1);
                out.println(produireTableauRecherche(rest));
                callstm.close();
                bd.deconnecter();                 
            }
            catch (SQLException ex)
            {
                response.sendRedirect("erreur.html");
            }            
            //out.println("<h1>Servlet Recherche at " + request.getContextPath() + "</h1>");
            //out.println(request.getParameter("recherche"));            
            html.fermerHTML();
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

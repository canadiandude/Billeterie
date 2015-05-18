/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Date;
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
@WebServlet(name = "Acheter", urlPatterns =
{
    "/Acheter"
})
public class Acheter extends HttpServlet
{
    String redirect = null;
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
        String acheter = "";
        try
        {
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callRep = bd.prepareCall("{ ?= call PKG_BILLETS.GET_INFO_REPRESENTATION(?) }");
            callRep.registerOutParameter(1, OracleTypes.CURSOR);
            callRep.setInt(2, Integer.parseInt(request.getParameter("representation")));
            redirect = request.getParameter("representation");
            callRep.execute();
            ResultSet rstRep = (ResultSet) callRep.getObject(1);
            rstRep.next();

            CallableStatement callSec = bd.prepareCall("{ ?= call PKG_BILLETS.GET_INFO_SECTIONS(?)}");
            callSec.registerOutParameter(1, OracleTypes.CURSOR);
            callSec.setString(2, rstRep.getString("NOMSALLE"));
            callSec.execute();

            acheter = OutilsHTML.produireFormAcheter(rstRep, (ResultSet) callSec.getObject(1));
            callRep.close();
            callSec.close();
            bd.deconnecter();
        } catch (SQLException sqle)
        {
            response.sendRedirect("erreur.html");
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);
            html.ouvrirHTML("Achat de billets", (String) request.getSession().getAttribute("client"));
            out.println(acheter);
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
        String client = (String) request.getSession().getAttribute("client");
        if (client != null)
        {
            try
            {
                ConnexionOracle bd = new ConnexionOracle();

                String[] sections = request.getParameterValues("sections");
                for (String section : sections)
                {
                    String quantite = request.getParameter(section);
                    if (quantite != null && !quantite.isEmpty() && !quantite.equals("0"))
                    {
                        CallableStatement callstm = bd.prepareCall("{ call PKG_BILLETS.INSERT_ACHAT(?,?,?,?,?,?) }");
                        callstm.setInt(1, Integer.parseInt(request.getParameter("representation")));
                        callstm.setInt(2, Integer.parseInt(section));
                        callstm.setString(3, client);
                        callstm.setDate(4, new Date(new java.util.Date().getTime()));
                        callstm.setString(5, "N");
                        callstm.setInt(6, Integer.parseInt(quantite));
                        callstm.execute();
                        callstm.close();
                    }
                }
                 bd.deconnecter();
            } catch (SQLException e)
            {
                response.sendRedirect("erreur.html");
            }
            
            response.sendRedirect("Panier");
        } else
        {
            request.getSession().setAttribute("redirect", redirect);
            response.sendRedirect("Authentification");
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


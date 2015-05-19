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
import javax.servlet.http.Cookie;
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

            Cookie[] tabCookies = request.getCookies();
            if (tabCookies != null)
            {
                for (Cookie c : tabCookies)
                {
                    if (c.getName().equals("params"))
                    {
                        out.println("Params recherche :  " + c.getValue());
                    }
                }
            }

            try
            {
                String RechercheText = request.getParameter("recherche");
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_RESULTATRECHERCHE(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);                  
                callstm.setString(2, RechercheText);
                callstm.execute();
                ResultSet rest = (ResultSet) callstm.getObject(1);
                out.println(produireTableauRecherche(rest, request));
                callstm.close();
                bd.deconnecter();
            } catch (SQLException ex)
            {
                response.sendRedirect("erreur.html");
            }      
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
        Cookie[] tabCookies = request.getCookies();
        String params = "";
        if (tabCookies != null)
        {
            for (Cookie c : tabCookies)
            {
                if (c.getName().equals("params"))
                {
                    params = c.getValue();
                }
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);

            html.ouvrirHTML("Recherche", (String) request.getSession().getAttribute("client"));
            try
            {
                String RechercheText = request.getParameter("recherche");
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_RESULTATRECHERCHE(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);                   
                callstm.setString(2, RechercheText);
                callstm.execute();
                ResultSet rest = (ResultSet) callstm.getObject(1);
                out.println(produireTableauRecherche(rest, params));
                callstm.close();
                bd.deconnecter();
            } catch (SQLException ex)
            {
                response.sendRedirect("erreur.html");
            }       
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
        ecrireCookie(request, response);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);

            html.ouvrirHTML("Recherche", (String) request.getSession().getAttribute("client"));

            try
            {
                String RechercheText = request.getParameter("recherche");
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_RESULTATRECHERCHE(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);                 
                callstm.setString(2, RechercheText);
                callstm.execute();
                ResultSet rest = (ResultSet) callstm.getObject(1);
                out.println(produireTableauRecherche(rest, request));
                callstm.close();
                bd.deconnecter();
            } catch (SQLException ex)
            {
                response.sendRedirect("erreur.html");
            }        
            html.fermerHTML();
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

    private void ecrireCookie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
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

            String params = "";

            while (restCategories.next())
            {
                if (request.getParameter(restCategories.getString(2)) != null)
                {
                    params += restCategories.getString(2) + ",";
                }
            }

            while (restSalles.next())
            {
                if (request.getParameter(restSalles.getString(2)) != null)
                {
                    params += restSalles.getString(2) + ",";
                }
            }

            Cookie cookie = new Cookie("params", params);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);

            callstm.close();
            callstm2.close();
            bd.deconnecter();
        } catch (SQLException e)
        {
            response.sendRedirect("erreur.html");
        }
    }

}

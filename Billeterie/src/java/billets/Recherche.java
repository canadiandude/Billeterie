/*
 *          Recherche.java
 *
 *  Servlet dont la tâche est de permettre èa l'usager de rechercher
 *  des spectacles par le nom de l'artiste ou du spectacle en choisissant
 *  les catégories et salles qu'il désire.
 *  
 *  Auteurs  : François Rioux et Xavier Brosseau   
 *  Remis le : 20 mai 2015 
 *  Cours    : 420-KEH-LG Systèmes de gestion de bases de données
 *             420-KEK-LG Communication en informatique de gestion
 *
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

@WebServlet(name = "Recherche", urlPatterns =
{
    "/Recherche"
})
public class Recherche extends HttpServlet
{

    /**
     * Appel de Recherche par la méthode GET.
     *
     * Se produit lorsque que la recherche est invoquée d'une page autre que la
     * page de recherche
     *
     * Lit les préférences de recherche dans un cookie et affiche les résultats
     * pour les termes de recherches entrés.
     *
     * @param request la requête au servlet
     * @param response la réponse du servlet
     *
     * @throws ServletException si une erreur de servlet se produit.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        //*** Lecture du cookie ***
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

        //*** Écriture de la recherche chez le client ***
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);

            html.ouvrirHTML("Recherche", (String) request.getSession().getAttribute("client"));
            try
            {
                String RechercheText = request.getParameter("recherche");
                //*** Appel à la base de données ***
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_RESULTATRECHERCHE(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);
                callstm.setString(2, RechercheText);
                callstm.execute();
                ResultSet rest = (ResultSet) callstm.getObject(1);
                out.println(produireTableauRecherche(rest, params));
                //*** Fermeture de la base de données ***
                callstm.close();
                bd.deconnecter();
            } catch (SQLException ex)
            {
                ex.printStackTrace();
                // Redirection en cas d'erreur de base de données
                response.sendRedirect("erreur.html");
            }
            html.fermerHTML();
        }
    }

    /**
     * Appel de Recherche par la méthode POST.
     *
     * Se produit lorsque que la recherche est invoquée par la page de recherche
     *
     * Écrit les préférences de recherche dans un cookie et affiche les
     * résultats pour les termes de recherches entrés.
     *
     * @param request la requête au servlet
     * @param response la réponse du servlet
     *
     * @throws ServletException si une erreur de servlet se produit.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        //*** Écriture du cookie ***
        ecrireCookie(request, response);

        //*** Écriture de la recherche chez le client ***
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);

            html.ouvrirHTML("Recherche", (String) request.getSession().getAttribute("client"));
            try
            {
                String RechercheText = request.getParameter("recherche");
                //*** Appel à la base de données ***
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_RESULTATRECHERCHE(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);
                callstm.setString(2, RechercheText);
                callstm.execute();
                ResultSet rest = (ResultSet) callstm.getObject(1);
                out.println(produireTableauRecherche(rest, request));
                //*** Fermeture de la base de données ***
                callstm.close();
                bd.deconnecter();
            } catch (SQLException ex)
            {
                // Redirection en cas d'erreur de base de données
                response.sendRedirect("erreur.html");
            }
            html.fermerHTML();
        }
    }

    /**
     * Retourne une brève description du servlet.
     *
     * @return un String contenant la description du servlet
     */
    @Override
    public String getServletInfo()
    {
        return "Recherche de spectacles";
    }

    /**
     * ecrireCookie.
     *
     * Écrit les préférences de recherche dans un cookie.
     *
     * @param request la requête au servlet
     * @param response la réponse du servlet
     *
     * @throws ServletException si une erreur de servlet se produit.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    private void ecrireCookie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            //*** Appel à la base de données ***
            // Récupération du nom des salles
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_SALLE() }");
            callstm.registerOutParameter(1, OracleTypes.CURSOR);
            callstm.execute();
            ResultSet restSalles = (ResultSet) callstm.getObject(1);
            // Récupération du nom des catégories
            CallableStatement callstm2 = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_CATEGORIE() }");
            callstm2.registerOutParameter(1, OracleTypes.CURSOR);
            callstm2.execute();
            ResultSet restCategories = (ResultSet) callstm2.getObject(1);

            // Les préférences à conserver
            String params = "";

            //*** Ajout des catégories ***
            if (request.getParameter("allCategories").equals("true"))
            {
                params += "allCategories,";
            } else
            {
                while (restCategories.next())
                {
                    if (request.getParameter(restCategories.getString(2)) != null)
                    {
                        params += restCategories.getString(2) + ",";
                    }
                }
            }

            //*** Ajout des salles ***
            if (request.getParameter("allSalles").equals("true"))
            {
                params += "allSalles,";
            } else
            {
                while (restSalles.next())
                {
                    if (request.getParameter(restSalles.getString(2)) != null)
                    {
                        params += restSalles.getString(2) + ",";
                    }
                }
            }

            //*** Envoi du cookie ***
            Cookie cookie = new Cookie("params", params);
            // Durée de vie d'une semaine
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);

            //*** Fermeture de la base de données ***
            callstm.close();
            callstm2.close();
            bd.deconnecter();
        } catch (SQLException e)
        {
            // Redirection en cas d'erreur de base de données
            response.sendRedirect("erreur.html");
        }
    }
}

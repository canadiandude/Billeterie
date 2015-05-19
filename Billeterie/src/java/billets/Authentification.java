/*
 *          Acheter.java
 *
 *  Servlet dont la tâche est d'afficher un formulaire
 *  pour la connexion de l'usager et s'occupe de le mettre en ligne
 *  
 *  Auteurs  : François Rioux et Xavier Brosseau   
 *  Remis le : 20 mai 2015 
 *  Cours    : 420-KEH-LG Systèmes de gestion de bases de données
 *             420-KEK-LG Communication en informatique de gestion

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

@WebServlet(name = "Authentification", urlPatterns =
{
    "/Authentification"
})
public class Authentification extends HttpServlet
{
    /**
     * Appel de Authentification par la méthode GET.
     *
     * Affiche le formulaire de connexion.
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
        //*** Envoi du formulaire au client ***
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);
            html.ouvrirHTML("Connexion", (String) request.getSession().getAttribute("client"));
            html.produireFormAuthentification();
            html.fermerHTML();
        }
    }

    /**
     * Appel de Authentification par la méthode POST.
     *
     * Vérifie si l'usager a entré des informations de connexion
     * valides et si oui le redirige vers son panier. Sinon, le 
     * formulaire se raffiche avec un message d'erreur
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
        // La base de donnée retourne le nombre d'usager
        // au informations entrées
        int existe = 0;
        try
        {
            //*** Appel à la base de données ***
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AUTHENTIFIER(?,?) }");
            callstm.registerOutParameter(1, OracleTypes.NUMBER);
            callstm.setString(2, request.getParameter("email"));
            callstm.setString(3, request.getParameter("mdp"));
            callstm.execute();
            existe = callstm.getInt(1);
            //*** Fermeture de la base de données ***
            callstm.close();
            bd.deconnecter();
        } catch (SQLException sqle)
        {
            // Redirection en cas d'erreur de base de données
            response.sendRedirect("erreur.html");
        }

        try (PrintWriter out = response.getWriter())
        {
            if (existe != 0)
            {
                // L'usager en maintenant en ligne
                request.getSession().setAttribute("client", request.getParameter("email"));
                if ((String) request.getSession().getAttribute("redirect") != null)
                {
                    // Avant de se connecter, l'usager achetait des billets pour
                    // un spectacle, on le renvoi à cette page
                    String redirect = (String) request.getSession().getAttribute("redirect");
                    request.getSession().setAttribute("redirect", null);
                    response.sendRedirect("Acheter?representation=" + redirect);
                } else
                {
                    // Sinon on redirige vers le panier
                    response.sendRedirect("Panier");
                }
            } else
            {
                //*** Il y a eu erreur, on raffiche le formulaire ***
                response.setContentType("text/html;charset=UTF-8");
                OutilsHTML html = new OutilsHTML(out);
                html.ouvrirHTML("Connexion", (String) request.getSession().getAttribute("client"));
                out.println("<h1 style=\"color: red\">Courriel/Mot de passe invalide !</h1>");
                html.produireFormAuthentification();
                html.fermerHTML();
            }
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
        return "Connexion de l'usager";
    }
}

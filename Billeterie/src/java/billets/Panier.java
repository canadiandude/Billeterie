/*
 *          Panier.java
 *
 *  Servlet dont la tâche est d'afficher le panier du client.
 *  
 *  Auteurs  : François Rioux et Xavier Brosseau   
 *  Remis le : 20 mai 2015 
 *  Cours    : 420-KEH-LG Systèmes de gestion de bases de données
 *             420-KEK-LG Communication en informatique de gestion
 *
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
     * Traitement d'une requête à Panier par la méthode GET ou POST.
     * Affiche le panier du client.
     *
     * @param request la requête au servlet
     * @param response la réponse du servlet
     *
     * @throws ServletException si une erreur de servlet se produit.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Récupération des informations client
        String client = (String) request.getSession().getAttribute("client");
        // Le panier
        String tableau = "<h1>Une erreur est survenue</h1>";// Message par défaut
        if (client != null)
        {
            try
            {
                //*** Appel à la base de données ***
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_PANIER(?) }");
                callstm.registerOutParameter(1, OracleTypes.CURSOR);
                callstm.setString(2, client);
                callstm.execute();
                //*** Écriture du panier ***
                tableau = OutilsHTML.produireTableauPanier((ResultSet) callstm.getObject(1));
                //*** Fermeture de la base de données ***
                callstm.close();
                bd.deconnecter();
            } catch (SQLException sqle)
            {
                // Redirection en cas d'erreur de base de donnéesS
                response.sendRedirect("erreur.html");
            }

            //*** Envoi du panier au client ***
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter())
            {
                OutilsHTML html = new OutilsHTML(out);
                html.ouvrirHTML("Panier", (String) request.getSession().getAttribute("client"));
                html.afficherPanier(tableau);
                html.fermerHTML();
            }
        } else
        {
            //*** L'usager n'est pas connecté, on le revoi à la connexion
            response.sendRedirect("Authentification");
        }
    }

    /**
     * mettreAJourPanier
     * 
     * Met à jour les quantités de billets modifiées par le client dans la base
     * de données
     *
     * @param request la requête au servlet
     * @param response la réponse du servlet
     *
     * @throws ServletException si une erreur de servlet se produit.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    private void mettreAJourPanier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            // Récupération des informations client
            String client = (String) request.getSession().getAttribute("client");
            //*** Appel à la base de données ***
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callList = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_PANIER(?) }");
            callList.registerOutParameter(1, OracleTypes.CURSOR);
            callList.setString(2, client);
            callList.execute();
            // Récupération du panier pour les numéro d'achat
            ResultSet rst = (ResultSet) callList.getObject(1);
            
            //*** Mise à jour de la base de données
            while (rst.next())
            {
                CallableStatement callUpdate = bd.prepareCall("{ call PKG_BILLETS.UPDATE_PANIER(?,?,?) }");
                int numachat = rst.getInt("NUMACHAT");
                callUpdate.setInt(1, numachat);
                int qte = Integer.parseInt(request.getParameter("quantite_" + numachat));
                callUpdate.setInt(2, qte);
                String print = request.getParameter("print_" + numachat) != null ? "Y" : "N";
                callUpdate.setString(3, print);
                callUpdate.execute();
                callUpdate.close();
            }
            //*** Fermeture de la base de données ***
            callList.close();
            bd.deconnecter();
        } catch (SQLException sqle)
        {
            // Redirection en cas d'erreur de base de données
            response.sendRedirect("erreur.html");
        }
    }

    /**
     * Appel de Panier par la méthode GET ou POST. 
     * 
     * Affiche le panier
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
        processRequest(request, response);
    }

    /**
     * Appel de Panier par la méthode GET ou POST. 
     * 
     * Met èa jour et affiche le panier
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
        mettreAJourPanier(request, response);
        processRequest(request, response);
    }

    /**
     * Retourne une brève description du servlet.
     *
     * @return un String contenant la description du servlet 
     */
    @Override
    public String getServletInfo()
    {
        return "Panier du client";
    }
}

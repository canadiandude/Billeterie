/*
 *          Facture.java
 *
 *  Servlet dont la tâche est de confirmer l'achat d'un client
 *  et lui afficher sa facture.
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

@WebServlet(name = "Facture", urlPatterns =
{
    "/Facture"
})
public class Facture extends HttpServlet
{

    /**
     * Appel de Facture par la méthode GET ou POST.
     *
     * Confirme l'achat et affiche la facture.
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
        // Récupération des informations du client
        String client = (String) request.getSession().getAttribute("client");
        // La facture
        String facture = "<h1>Une erreur est survenue</h1>";// Message par défaut
        if (client != null)
        {
            try
            {
                //*** Appel à la base de données ***
                // Payer le panier
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callPayer = bd.prepareCall("{ call PKG_BILLETS.PAYER_PANIER(?) }");
                callPayer.setString(1, client);
                callPayer.execute();
;
                // Afficher la facture
                CallableStatement callFacture = bd.prepareCall("{ ?= call PKG_BILLETS.AFFICHER_FACTURE(?) }");
                callFacture.registerOutParameter(1, OracleTypes.CURSOR);
                callFacture.setString(2, client);
                callFacture.execute();
                //*** Écriture de la facture ***
                facture = OutilsHTML.produireFacture((ResultSet) callFacture.getObject(1));
                
                //*** Fermeture de la base de données ***
                callPayer.close();           
                callFacture.close();
                bd.deconnecter();
            } catch (SQLException sqle)
            {
                // Redirection en cas d'erreur de base de données
                response.sendRedirect("erreur.html");
            }
            
            //*** Envoi de la facture au client ***
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter())
            {
                OutilsHTML html = new OutilsHTML(out);
                html.ouvrirHTML("Facture", (String) request.getSession().getAttribute("client"));
                html.afficherFacture(facture);
                html.fermerHTML();
            }
        } else
        {
            // L'usager n'est pas connecté, on l'envoi à la connexion
            response.sendRedirect("Authentification");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Appel de Facture par la méthode GET.
     *
     * Appelle la méhode principale.
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
     * Appel de Facture par la méthode POST.
     *
     * Appelle la méhode principale.
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
        return "Confirmation de l'achat et facture";
    }// </editor-fold>

}

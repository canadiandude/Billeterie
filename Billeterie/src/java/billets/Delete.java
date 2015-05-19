/*
 *          Delete.java
 *
 *  Servlet dont la tâche est de supprimer un achat du panier
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
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Delete", urlPatterns =
{
    "/Delete"
})
public class Delete extends HttpServlet
{

    /**
     * Appel de Delete par la méthode GET ou POST.
     *
     * Efface l'achat voulu du panier.
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
        try
        {
            //*** Appel à la base de données ***
            ConnexionOracle bd = new ConnexionOracle();
            CallableStatement callstm = bd.prepareCall("{ call PKG_BILLETS.DELETE_ACHAT(?)}");
            callstm.setInt(1, Integer.parseInt(request.getParameter("delete")));
            callstm.execute();
            //*** Fermeture de la base de données ***
            callstm.close();
            bd.deconnecter();
            //*** Retour au panier *** 
            response.sendRedirect("Panier");
        }
        catch (SQLException e)
        {
            // Redirection en cas d'erreur de base de données
            response.sendRedirect("erreur.html");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Appel de Delete par la méthode GET.
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
     * Appel de Delete par la méthode POST.
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
        return "Suppression d'un achat";
    }// </editor-fold>

}

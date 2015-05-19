/*
 *          Inscription.java
 *
 *  Servlet dont la tâche est d'afficher un formulaire
 *  pour l'inscription d'un usager et l'ajouter à la base de données.
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

@WebServlet(name = "Inscription", urlPatterns =
{
    "/Inscription"
})
public class Inscription extends HttpServlet
{
    /**
     * Appel de Inscription par la méthode GET.
     *
     * Affiche le formulaire d'inscription.
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
        //*** Écriture du formulaire ***
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            OutilsHTML html = new OutilsHTML(out);
            html.ouvrirHTML("Inscription", (String) request.getSession().getAttribute("client"));
            // Affichage d'un message d'erreur si l'usager a mal rempli le formulaire auparavant
            if (request.getSession().getAttribute("erreur_inscription") != null)
            {
                out.println("<h3>" + (String) request.getSession().getAttribute("erreur_inscription") + "</h3>");
                request.getSession().setAttribute("erreur_inscription", null);
            }
            html.produireFormInscription();
            html.fermerHTML();
        }
    }

    /**
     * Appel de Inscription par la méthode POST.
     *
     * Valide les paramètres et inscrit l'usager. Si un paramètre est invalide,
     * une message d'erreur est créé et l'usager est redirigé vers le formulaire
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
        //*** Récupération des paramètres ***
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String confirm = request.getParameter("confirm");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String adresse = request.getParameter("adresse");
        String telephone = request.getParameter("telephone");

        //*** Validation des paramètres ***
        String erreur = "";
        if (email.isEmpty())
        {
            erreur += "le courriel ";
        }

        if (mdp.isEmpty() || mdp.length() > 50)
        {
            erreur += "le mot de passe ";
        }

        if (confirm.isEmpty() || !confirm.equals(mdp))
        {
            erreur += "confirmation du mot de passe ";
        }

        if (nom.isEmpty() || nom.length() > 50)
        {
            erreur += "votre nom ";
        }

        if (prenom.isEmpty() || prenom.length() > 50)
        {
            erreur += "votre prenom ";
        }

        if (adresse.isEmpty() || adresse.length() > 150)
        {
            erreur += "votre adresse ";
        }

        if (telephone.isEmpty() || telephone.length() > 15)
        {
            erreur += "votre telephone ";
        }

        if (erreur.isEmpty())
        {
            //*** Si les paramètres sont valides ***
            try
            {
                //*** Appel à la base de données ***
                ConnexionOracle bd = new ConnexionOracle();
                CallableStatement callstm = bd.prepareCall("{ call PKG_BILLETS.INSERT_CLIENT(?,?,?,?,?,?) }");
                callstm.setString(1, email);
                callstm.setString(2, mdp);
                callstm.setString(3, nom);
                callstm.setString(4, prenom);
                callstm.setString(5, adresse);
                callstm.setString(6, telephone);
                callstm.execute();
                //*** Fermeture de la base de données ***
                callstm.close();
                bd.deconnecter();
                //*** Le client est en ligne, redirection vers le panier ***
                request.getSession().setAttribute("client", request.getParameter("email"));
                response.sendRedirect("Panier");
            } catch (SQLException e)
            {
                // Redirection en cas d'erreur de base de données
                response.sendRedirect("erreur.html");
            }
        } else
        {
            //*** Il y a eu erreur, on raffiche le formulaire ***
            request.getSession().setAttribute("erreur_inscription", erreur);
            response.sendRedirect("Inscription");
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
        return "Inscription";
    }
}

/*
 *          Acheter.java
 *
 *  Servlet dont la tâche est d'afficher un formulaire
 *  pour l'ajout de billets au panier et de mettre à jour
 *  la base de données une foit la séléction éffectuée.
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;

@WebServlet(name = "Acheter", urlPatterns =
{
    "/Acheter"
})
public class Acheter extends HttpServlet
{
    // Le numéro de la représentation. Utilisé pour
    // restaurer la page après la connexion de l'usager
    // si celui-ci n'était pas connecté au moment de l'ajout
    String redirect = null;

    /**
     * Appel de Acheter par la méthode GET.
     *
     * Affiche le formulaire d'achat.
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
        // Le formulaire
        String acheter = "";

        try
        {
            //*** Appel à la base de données ***
            ConnexionOracle bd = new ConnexionOracle();
            // Informations sur la représentation voulue par le client
            CallableStatement callRep = bd.prepareCall("{ ?= call PKG_BILLETS.GET_INFO_REPRESENTATION(?) }");
            callRep.registerOutParameter(1, OracleTypes.CURSOR);
            callRep.setInt(2, Integer.parseInt(request.getParameter("representation")));
            redirect = request.getParameter("representation");
            callRep.execute();
            ResultSet rstRep = (ResultSet) callRep.getObject(1);
            rstRep.next();
            // Informations sur les sections, pour connaitre le nombre de places disponibles
            CallableStatement callSec = bd.prepareCall("{ ?= call PKG_BILLETS.GET_INFO_SECTIONS(?)}");
            callSec.registerOutParameter(1, OracleTypes.CURSOR);
            callSec.setString(2, rstRep.getString("NOMSALLE"));
            callSec.execute();

            //*** Écriture du formulaire ***
            acheter = OutilsHTML.produireFormAcheter(rstRep, (ResultSet) callSec.getObject(1));

            //*** Fermeture de la base de données ***
            callRep.close();
            callSec.close();
            bd.deconnecter();
        } catch (SQLException sqle)
        {
            // Redirection en cas d'erreur de base de données
            response.sendRedirect("erreur.html");
        }

        //*** Envoi du formulaire au client ***
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
     * Appel de Acheter par la méthode POST.
     *
     * Ajoute l'achat au panier si l'usager est connecté, sinon il est redirigé
     * vers la page de connexion.
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
        //*** Récupération des informations du client ***
        String client = (String) request.getSession().getAttribute("client");
        if (client != null)
        {
            try
            {
                //*** Connexion à la base de données ***
                ConnexionOracle bd = new ConnexionOracle();

                //*** Récupération des numéros des sections ***
                String[] sections = request.getParameterValues("sections");
                for (String section : sections)
                {
                    //*** Récupération des quantité de billets pour chaque section ***
                    String quantite = request.getParameter(section);
                    if (quantite != null && !quantite.isEmpty() && !quantite.equals("0"))
                    {
                        //*** Insertion dans la base de données ***
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
                //*** Fermeture de la base de données ***
                bd.deconnecter();
            } catch (SQLException e)
            {
                // Redirection en cas d'erreur de base de données
                response.sendRedirect("erreur.html");
            }

            //*** Redirection vers le panier ***
            response.sendRedirect("Panier");
        } else
        {
            //*** L'usager n'est pas connecté, redirection vers la connexion ***
            request.getSession().setAttribute("redirect", redirect);
            response.sendRedirect("Authentification");
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
        return "Ajout de billets au panier";
    }
}

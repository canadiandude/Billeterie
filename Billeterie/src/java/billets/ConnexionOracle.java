/*
 *          ConnexionOracle.java
 *
 *  Classe qui gère la connexion à la base de données Mercure.
 *  Elle se connecte à l'instanciation et ne gère aucune exception
 *  
 *  Crée par    : Saliha Yacoub
 *  Modifié par : François Rioux et Xavier Brosseau   
 *  Remis le    : 20 mai 2015 
 *  Cours       : 420-KEH-LG Systèmes de gestion de bases de données
 *                420-KEK-LG Communication en informatique de gestion
 *
 */

package billets;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.pool.*;

public class ConnexionOracle
{
   private final String nomUsager = "brosseau";
   private final String motDePasse = "ORACLE2";
   private final String url = "jdbc:oracle:thin:@205.237.244.251:1521:orcl";
   private Connection connexion = null;
   
   public ConnexionOracle() throws SQLException
   {
       this.connecter();
   }
   
   public void connecter() throws SQLException
   {
         OracleDataSource ods = new OracleDataSource();
         ods.setURL( url );
         ods.setUser( nomUsager );
         ods.setPassword( motDePasse );
         connexion = ods.getConnection();
   }
    
   public void deconnecter()
   {
      try 
      { 
         connexion.close();
      }
      catch( SQLException se )
      {
         connexion = null;  
      }
   }
    
   public Connection getConnexion()
   {
      return connexion;
   }
   
   public CallableStatement prepareCall(String call) throws SQLException
   {
       return connexion.prepareCall(call);
   }
}
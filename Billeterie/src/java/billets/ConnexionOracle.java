package billets;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.pool.*;

public class ConnexionOracle
{
   private String nomUsager = "brosseau";
   private String motDePasse = "ORACLE2";
   private String url = "jdbc:oracle:thin:@205.237.244.251:1521:orcl";
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
         System.out.println("connexion");
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
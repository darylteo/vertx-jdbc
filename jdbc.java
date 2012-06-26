import org.vertx.java.core.*;
import org.vertx.java.deploy.*;

import java.sql.*;

public class jdbc extends Verticle {
   
   public void start(){
      
      final String connectionString = "jdbc:postgresql:jdbctest";
      final String username = "jdbctest";
      final String password = "jdbctest";

      Connection conn = null;

      try{
         /*
          * Supposedly with JDBC 4 this should not be required,
          * but I can't seem to get it to work without it...
          * Someone tell me what I'm missing ~dteo 2012-06-26
          */
         Class.forName("org.postgresql.Driver"); 

         conn = DriverManager.getConnection(
            connectionString,
            username,
            password
         );

      }catch(Exception e){
         e.printStackTrace();
      }finally{
         closeConnection(conn);
      }

   }

   private void closeConnection(Connection conn){
      if(conn == null){
         return;
      }

      try{
         conn.close();
      }catch(SQLException e){
         e.printStackTrace();
      }
      
   }

}

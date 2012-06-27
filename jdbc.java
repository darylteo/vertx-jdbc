import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;
import org.vertx.java.deploy.*;

import java.sql.*;

import jdbc.*;
import jdbc.handlers.*;

public class jdbc extends Verticle {
  
   public void start(){
     
      final Config configuration = loadConfig(container.getConfig());

      try{
         loadDriver(configuration.driver);
         beginListening(configuration);
      }catch(ClassNotFoundException e){
         container.getLogger().fatal(
            String.format(
               "Could not load JDBC driver %s",
               configuration.driver
            ),
            e
         );

         return;
      }

      beginListening(configuration);
   }


   /* EventBus Methods */
   private void beginListening(Config configuration){
      EventBus eb = vertx.eventBus();   

      eb.registerHandler(
         "test.address",
         new SelectHandler(configuration)
      );
   }


   /* JDBC Methods */
   private void loadDriver(String driver) throws ClassNotFoundException{
      /*
       * Supposedly with JDBC 4 this should not be required,
       * but I can't seem to get it to work without it...
       * Someone tell me what I'm missing ~dteo 2012-06-26
       */
      if(driver.isEmpty()){
         return;
      }
      
      Class.forName(driver); 
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



   /* Configuration Methods */
   private Config loadConfig(JsonObject config){
      Config result = new Config();

      if(config == null){
         return result;
      }

      result.driver = config.getString("driver");
      result.url = config.getString("url");
      result.username = config.getString("username");
      result.password = config.getString("password");
      
      return result;
   }

}

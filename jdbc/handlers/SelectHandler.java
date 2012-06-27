package jdbc.handlers;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;

import java.sql.*;

import jdbc.Config;

public class SelectHandler extends JdbcHandler {

   public SelectHandler(Config config){
      super(config);
   }

   public void handle(Message<JsonObject> message){

      System.out.println("Message Received: " + message.body);

      try(
         Connection conn = super.openConnection()
      ){
         final String query = message.body.getString("query"); 

         final PreparedStatement stmt = conn.prepareStatement(
            query
         );

         final ResultSet rs = stmt.executeQuery();
         final JsonObject result = resultSetToJsonObject(rs);

         message.reply(result);
      }catch(SQLException e){
         e.printStackTrace();
      }
   }
}

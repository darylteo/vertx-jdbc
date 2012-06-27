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
      try(
         Connection conn = super.openConnection()
      ){

         PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM test;"
         );

         ResultSet rs = stmt.executeQuery();

         while(rs.next()){
            System.out.print(rs.getInt(1) + " : ");
            System.out.println(rs.getString(2));
         }

      }catch(SQLException e){
         e.printStackTrace();
      }
   }
}

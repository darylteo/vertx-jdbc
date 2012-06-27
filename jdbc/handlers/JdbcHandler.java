package jdbc.handlers;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;

import java.sql.*;

import jdbc.Config;

public abstract class JdbcHandler
   implements Handler<Message<JsonObject>> {

   private Config config;

   public JdbcHandler(Config config){
      this.config = config;
   }

   Connection openConnection() 
      throws SQLException {

      return DriverManager.getConnection(
         config.url,
         config.username,
         config.password
      );
   }
}

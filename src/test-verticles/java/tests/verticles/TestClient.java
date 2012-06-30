package jdbc.tests.verticles;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;
import org.vertx.java.framework.*;

import java.sql.*;

public class TestClient extends TestClientBase {
   
   public void start(){
      super.start();

      container.deployWorkerVerticle(
         "jdbc-v1.0",
         new JsonObject()
            .putString("driver","org.postgresql.Driver")
            .putString("url","jdbc:postgresql:jdbctest")
            .putString("username", "jdbctest")
            .putString("password", "jdbctest")
            .putString("prefix", "jdbc")
         ,
         1,
         new Handler(){
            public void handle(Object res) {
               tu.appReady();
            }
         }
      );

   }

   public void stop() {
      super.stop();
   }

   public void testSingleSelectQuery() throws Exception {
      EventBus eb = vertx.eventBus();

      eb.send(
         "jdbc.query",
         new JsonObject()
            .putArray(
               "queries", 
               new JsonArray()
                  .addObject(new JsonObject()
                     .putString(
                        "query",
                        "SELECT * FROM items;"
                     )
                  )
            ),
         new Handler<Message<JsonObject>>(){
            public void handle(Message<JsonObject> message){
               tu.testComplete();
            }
         }
      );
   }
   
   public void testInsertData() throws Exception {
      tu.testComplete();
   }
}

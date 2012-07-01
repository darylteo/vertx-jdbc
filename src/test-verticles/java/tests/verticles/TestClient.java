package jdbc.tests.verticles;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;
import org.vertx.java.framework.*;

import java.sql.*;
import java.util.*;

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

               System.out.println(message.body); 

               try{
                  JsonObject body = message.body;

                  tu.azzert(body.getBoolean("success") == true, "Query was unsuccessful");
                  
                  Object[] resultArray = body.getArray("result").toArray();

                  tu.azzert(resultArray.length == 1, "Incorrect number of results");

                  List queryResult = (List)resultArray[0];

                  tu.azzert(queryResult.size() == 3, "Query return incorrect number of rows");

                  tu.testComplete();
               }catch (Throwable e){
                  tu.exception(e, "Failed to parse message body");
                  tu.testComplete();
               }
            }
         }
      );
   }
   
   public void testInsertData() throws Exception {
      tu.testComplete();
   }
}

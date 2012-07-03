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
            .putObject(
               "queries", 
               new JsonObject()
                  .putString(
                     "query",
                     "SELECT * FROM items;"
                  )
               
            ),
         new Handler<Message<JsonObject>>(){
            public void handle(Message<JsonObject> message){

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
   
   public void testTransactionQueryPass() throws Exception {
 
      final EventBus eb = vertx.eventBus();

      Handler<Message<JsonObject>> replyHandler = 
         new Handler<Message<JsonObject>>(){
            public void handle(Message<JsonObject> message){
               try{
                  JsonObject body = message.body;

                  tu.azzert(body.getBoolean("success") == true, "Query was not successful");
                  tu.testComplete();
               }catch (Throwable e){
                  tu.exception(e, "Failed to parse message body");
                  tu.testComplete();
               }
            }
         };

      JsonObject command = 
         new JsonObject()
            .putBoolean(
               "transaction",
               true
            ).putArray(
               "queries", 
               new JsonArray()
                  .addObject(new JsonObject()
                     .putString(
                        "query",
                        "UPDATE orders SET dateofpurchase = ? WHERE id = ?"
                     ).putArray(
                        "params",
                        new JsonArray().addObject(
                           new JsonObject()
                              .putString("type","DATE")
                              .putString("value",new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date()))
                        ).addObject(
                           new JsonObject()
                              .putString("type","INTEGER")
                              .putNumber("value",1)
                        )
                     )
                  )
                  .addObject(new JsonObject()
                     .putString(
                        "query",
                        "SELECT * FROM orders WHERE id = ?"
                     ).putArray(
                        "params",
                        new JsonArray().addObject(
                           new JsonObject()
                              .putString("type","INTEGER")
                              .putNumber("value",1)
                        )
                     )
                  )
            )
      ;

      executeCommand(command,replyHandler);
   }

   public void testTransactionQueryRollback() throws Exception {
 
      final Handler<Message<JsonObject>> verifyHandler = 
         new Handler<Message<JsonObject>>(){
            public void handle(Message<JsonObject> message){

               List resultArray = (List)(message.body.getArray("result").toArray()[0]);

               Map resultMap = (Map)resultArray.get(0);
               int rowcount = (Integer)resultMap.get("rowcount");

               try{
                  tu.azzert(rowcount == 3);
               }catch(Throwable e){
                  tu.exception(e, "Failed to parse message body");
               }
               
               tu.testComplete();
            }
         };


      final Handler<Message<JsonObject>> replyHandler = 
         new Handler<Message<JsonObject>>(){
            public void handle(Message<JsonObject> message){

               try{
                  JsonObject body = message.body;

                  tu.azzert(body.getBoolean("success") == false, "Query was successful");

                  TestClient.this.executeCommand(
                     new JsonObject()
                        .putArray(
                           "queries",
                           new JsonArray()
                              .addObject(new JsonObject()
                                 .putString(
                                    "query",
                                    "SELECT COUNT(*) rowcount FROM items"
                                 )
                              )
                        ),
                     verifyHandler
                  );

               }catch (Throwable e){
                  tu.exception(e, "Failed to parse message body");
                  tu.testComplete();
               }
            }
         };

      JsonObject command = 
         new JsonObject()
            .putBoolean(
               "transaction",
               true
            ).putArray(
               "queries", 
               new JsonArray()
                  .addObject(new JsonObject()
                     .putString(
                        "query",
                        "INSERT INTO items (id,name,quantity,price,description) " + 
                           "VALUES (4,'Mirror',5,20.00,'A mirror')"
                     )
                  ).addObject(new JsonObject()
                     .putString(
                        "query",
                        "INSERT INTO items (id,name,quantity,price,description) " + 
                           "VALUES (4,'Mirror Dupe',5,20.00,'This should fail')"
                     )
                  ).addObject(new JsonObject()
                     .putString(
                        "query",
                        "SELECT * FROM items;"
                     )
                  )
            );
      
      executeCommand(command,replyHandler);
   }


   private void executeCommand(
      final JsonObject command, 
      final Handler<Message<JsonObject>> handler)
   {
      System.out.println("==== Execute Command ====");
      System.out.println("\n" + command);

      vertx.eventBus().send(
         "jdbc.query",
         command,
         new Handler<Message<JsonObject>>(){
            public void handle(Message<JsonObject> message){
               System.out.println("\n==== Reply: " + message.body);

               handler.handle(message);
            }
         }
      );
   }
}

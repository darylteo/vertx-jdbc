package jdbc.handlers;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;

public class SelectHandler implements Handler<Message<JsonObject>>{

   public SelectHandler(){

   }

   public void handle(Message<JsonObject> message){
      System.out.println("Message Received: " + message);
   }
}

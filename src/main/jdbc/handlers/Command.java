package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

public class Command {
   private boolean transaction = false;
   private List<Query> queries = new LinkedList<>();

   public Command(JsonObject object){
      this.transaction = object.getBoolean("transaction",false);

      insertQueries(object);
   } 

   /* Accessors */
   public boolean isTransaction(){
      return this.transaction;
   }

   public List<Query> getQueries(){
      return this.queries;
   }


   /* Private Methods */
   private void insertQueries(JsonObject object){
      Object field = object.getField("queries"); 

      if (field instanceof JsonArray){
         for(Object query : (JsonArray)field){
            queries.add(
               new Query((JsonObject)query)
            );
         }
      }else{
         queries.add(
            new Query((JsonObject)field)
         );
      }

   }
}



package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

public class Query {
   private String query = "";
   private List<Parameter> params = new LinkedList<>();

   public Query(JsonObject object){
      this.query = object.getString("query");

      insertParameters(object);
   }

   /* Accessors */
   public List<Parameter> getParameters(){
      return this.params;
   }
   public String getQueryString(){
      return this.query;
   }

   /* Private Methods */
   private void insertParameters(JsonObject object){
      Object field = object.getField("params");

      if(field == null){
         return;
      }

      JsonArray paramArray = (JsonArray)field;

      System.out.println("WTF " + paramArray);
      for(Object param : paramArray){
         params.add(
            new Parameter((JsonObject)param)
         );
      }
   }
}


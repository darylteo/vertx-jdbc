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

      if(params == null){
         return;
      }

      for(Object param : params){
         params.add(
            new Parameter((JsonObject)param)
         );
      }
   }
}


package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.List;
import java.util.ArrayList;

class Command {
   private boolean transaction = false;
   private List<Query> queries = new ArrayList<>();

   public Command(JsonObject object){
      this.transaction = object.getBoolean("transaction");

      if(!insertQueries(object)){
         if (!insertSingleQuery(object)){
            /* TODO: Fail */
         }
      }
   }

   /* Accessors */
   public boolean isTransaction(){
      return this.transaction;
   }

   public List<Query> getQueries(){
      return this.queries;
   }

   /* Json Methods */
   public JsonObject toJsonObject(){
      return new JsonObject();
   }

   /* Private Methods */
   private boolean insertQueries(JsonObject object){
      try{
         JsonArray queryArray = object.getArray("queries");         

         for(Object query : queryArray){
            queries.add(
               new Query((JsonObject)query)
            );
         }
      }catch(ClassCastException e){
         return false;   
      }

      return true;
   }
   private boolean insertSingleQuery(JsonObject object){
      try{
         JsonObject queryObject = object.getObject("queries");         

         queries.add(
            new Query((JsonObject)queryObject)
         );
      }catch(ClassCastException e){
         return false;
      }

      return true;
   }
}


class Query {
   private String query = "";
   private List<Parameter> params = new ArrayList<>();

   public Query(JsonObject object){
      this.query = object.getString("query");

      insertParameters(object.getArray("params"));
   }

   /* Accessors */
   public List<Parameter> getParameters(){
      return this.params;
   }
   public String getQueryString(){
      return this.query;
   }

   /* Private Methods */
   private void insertParameters(JsonArray params){
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

class Parameter {
   private String type;
   private Object value;

   public Parameter(JsonObject object){
      this.type = object.getString("type");
      this.value = object.getField("value");
   }

   /* Accessors */
   public String getType(){
      return this.type;
   }

   public Short getValueAsShort(){
      return (Short)this.value;
   }

   public Integer getValueAsInteger(){
      return (Integer)this.value;
   }

   public Long getValueAsLong(){
      return (Long)this.value;
   }

   public String getValueAsString(){
      return (String)this.value;
   }
}


class Reply {
   private boolean success = true;
   private List<Result> results;

   /* Mutators */
   public void setSuccess(boolean value){
      this.success = value;
   }
}

class Result {
    
}

package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

class Command {
   private boolean transaction = false;
   private List<Query> queries = new LinkedList<>();

   public Command(JsonObject object){
      this.transaction = object.getBoolean("transaction");

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
      JsonArray queryArray = object.getArray("queries");         

      for(Object query : queryArray){
         queries.add(
            new Query((JsonObject)query)
         );
      }
   }
}


class Query {
   private String query = "";
   private List<Parameter> params = new LinkedList<>();

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
   private List<ResultGroup> results = new LinkedList<>();

   /* Mutators */
   public void setSuccess(boolean value){
      this.success = value;
   }

   public void addResultGroup(ResultGroup result){
      this.results.add(result);
   }

   public JsonObject toJson(){
      JsonObject result = new JsonObject();

      result.putBoolean("success",success);

      JsonArray array = new JsonArray();

      for(ResultGroup group : results){
         array.addArray(group.toJson());
      }
      
      result.putArray("result",array);

      return result;

   }
}

class ResultGroup {
   private List<Result> results = new LinkedList<>();

   public void addResult(Result result){
      this.results.add(result);
   }

   public JsonArray toJson(){
      System.out.println("Array Length: " + this.results.size());
      JsonArray array = new JsonArray();
      
      for(Result result : results){
         array.addArray(result.toJson());
      }

      return array;
   }
}

class Result {
   private List<JsonObject> rows = new LinkedList<>();   

   public void addRow(JsonObject row){
      this.rows.add(row);
   }

   public JsonArray toJson(){
      JsonArray array = new JsonArray();
      for (JsonObject object : this.rows){
         array.addObject(object);
      }

      return array;
   }
}

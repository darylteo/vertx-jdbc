package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

class Reply {
   private boolean success = true;
   private List<Result> results = new LinkedList<>();
   private List<Exception> exceptions = new LinkedList<>();

   /* Mutators */
   public void setSuccess(boolean value){
      this.success = value;
   }

   public void addResult(Result result){
      this.results.add(result);
   }

   public void addException(Exception e){
      this.exceptions.add(e);
   }

   public JsonObject toJson(){
      JsonObject reply = new JsonObject();

      reply.putBoolean("success",success);

      this.addResults(this.results,reply);
      this.addExceptions(this.exceptions,reply);

      return reply;

   }
   
   private void addResults(
      List<Result> results,
      JsonObject object
   ){
      if(results.size() == 0){
         return;
      }

      /* Add array of results, if any */
      JsonArray array = new JsonArray();

      for(Result result : results){
         array.addArray(result.toJson());
      }
      
      object.putArray("result",array);
   }

   private void addExceptions(
      List<Exception> exceptions,
      JsonObject object
   ){
      if(exceptions.size() == 0){
         return;
      }

      /* Add array of results, if any */
      JsonArray array = new JsonArray();

      for(Exception exception : exceptions){
         array.addObject(
            new JsonObject()
               .putString(
                  "message",
                  exception.getMessage()
               )
         );
      }
      
      object.putArray("errors",array);
   }
}


package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

class Reply {
   private boolean success = true;
   private List<Result> results = new LinkedList<>();

   /* Mutators */
   public void setSuccess(boolean value){
      this.success = value;
   }

   public void addResult(Result result){
      this.results.add(result);
   }

   public JsonObject toJson(){
      JsonObject reply = new JsonObject();

      reply.putBoolean("success",success);

      JsonArray array = new JsonArray();

      for(Result result : results){
         array.addArray(result.toJson());
      }
      
      reply.putArray("result",array);

      return reply;

   }
}


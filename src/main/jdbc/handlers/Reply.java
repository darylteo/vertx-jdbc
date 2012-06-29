package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

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


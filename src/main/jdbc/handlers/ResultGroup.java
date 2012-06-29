package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

public class ResultGroup {
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


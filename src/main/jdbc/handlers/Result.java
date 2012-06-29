package jdbc.handlers;

import org.vertx.java.core.json.*;

import java.util.*;

public class Result {
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

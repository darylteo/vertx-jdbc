package jdbc.handlers;

import org.vertx.java.core.json.*;

public class Parameter {
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


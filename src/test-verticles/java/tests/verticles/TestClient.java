package jdbc.tests.verticles;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;
import org.vertx.java.framework.*;

public class TestClient extends TestClientBase {
   
   public void start(){

      container.deployWorkerVerticle(
         "vertx-jdbc",
         new JsonObject(
          
         ),
         1,
         new Handler(){
            public void handle(Object res) {
               tu.appReady();
            }
         }
      );
   }

}

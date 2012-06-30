package jdbc.tests;

import org.junit.Test;
import org.vertx.java.framework.TestBase;
import jdbc.tests.verticles.TestClient;

public class JdbcTests extends TestBase {
   
   @Override
   protected void setUp() throws Exception {
      super.setUp();
      startApp(TestClient.class.getName());
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

}

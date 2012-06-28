package jdbc.handlers;

import org.vertx.java.core.*;
import org.vertx.java.core.eventbus.*;
import org.vertx.java.core.json.*;

import java.sql.*;
import java.util.*;

import jdbc.Config;

public class SelectHandler extends JdbcHandler {

   public SelectHandler(Config config){
      super(config);
   }

   public void handle(Message<JsonObject> message){

      System.out.println("Message Received: " + message.body);

      try(
         final Connection conn = super.openConnection()
      ){
         Command command = new Command(message.body);

         for(Query query : command.getQueries()){  
            Statement stmt = this.executeQuery(query,conn);

            ResultSet rs = stmt.getResultSet();
            while(rs != null){
               JsonObject result = resultSetToJsonObject(rs);
               
               message.reply(result);

               stmt.getMoreResults();
               rs = stmt.getResultSet();
            }
         }

      }catch(SQLException e){
         e.printStackTrace();

         message.reply(
            super.generateErrorReply(e)
         );
      }
   }


   private Statement executeQuery(Query query, Connection conn)
      throws SQLException {
      
      final PreparedStatement stmt = conn.prepareStatement(
         query.getQueryString() 
      );

      setParameters(stmt,query.getParameters());

      final boolean success = stmt.execute();
      /* TODO: Throw exception if fail */ 

      return stmt;
   }

   private void setParameters(
      PreparedStatement stmt,
      List<Parameter> params
   ) throws SQLException {

      for(int i = 0; i < params.size(); i++){
         /* JDBC Parameter Index is 1 based. */
         setParameter(stmt,params.get(i),i+1);
      }
   }

   private void setParameter(
      PreparedStatement stmt,
      Parameter param,
      int index
   ) throws SQLException {
      final String type = param.getType();


      /* Information about mapping SQL Types to Java Types can 
       * be found here
       * http://docs.oracle.com/javase/1.5.0/docs/guide/jdbc/
            getstart/mapping.html 
       **/
      switch(type){
         /* TINYINT and SMALLINT map to Short */
         case "TINYINT":
         case "SMALLINT":
            stmt.setShort(index,param.getValueAsShort());
            break;

         case "INTEGER":
            stmt.setInt(index,param.getValueAsInteger());
            break;

         case "BIGINT":
            stmt.setLong(index,param.getValueAsLong());
            break;

         /* String Types*/
         case "CHAR":
         case "NCHAR":
         case "VARCHAR":
         case "NVARCHAR":
            stmt.setString(index,param.getValueAsString());
            break;
      }
   }
}

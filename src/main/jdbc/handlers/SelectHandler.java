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

      message.reply(
         executeCommand(message.body)
            .toJson()
      );
   }

   private Reply executeCommand(JsonObject body){
      Reply reply = new Reply();

      try{
         try(
            final Connection conn = super.openConnection()
         ){
            Command command = new Command(body);

            if(command.isTransaction()){
               this.executeTransactionCommand(
                  command,
                  conn,
                  reply
               );
            }else{
               this.executeNonTransactionCommand(
                  command,
                  conn,
                  reply
               );
            }
         }
      }catch(Exception e){
         reply.setSuccess(false);
         reply.addException(e);
      }

      return reply;
   }

   private Reply executeNonTransactionCommand(
      Command command,
      Connection conn,
      Reply reply
   ) throws SQLException {

      executeQueries(
         command.getQueries(),
         conn,
         reply
      );

      return reply;
   }

   private Reply executeTransactionCommand(
      Command command,
      Connection conn,
      Reply reply
   ) throws SQLException {

      try {
         conn.setAutoCommit(false);

         executeQueries(
            command.getQueries(),
            conn,
            reply
         );

         conn.commit();
      }catch(SQLException e){
         conn.rollback();
         throw e;
      }

      return reply;
   }

   private void executeQueries(
      List<Query> queries,
      Connection conn,
      Reply reply
   ) throws SQLException {
     
      for(Query query : queries){  
         Statement stmt = this.executeQuery(query,conn);

         ResultSet rs = stmt.getResultSet();
         Result result = resultSetToResult(rs);

         reply.addResult(result);
      }
   }

   private Statement executeQuery(
      Query query,
      Connection conn
   ) throws SQLException {
      
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

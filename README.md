vertx-jdbc
==========

JDBC Busmod for Vertx.io

Usage
-----
To Come

Prefix
------
The default prefix for the busmod is "jdbc".
This means that to send messages to this module via the EventBus,
you should use "jdbc.query".

Query Json Specification
------------------------

A simple query:


    {
       "query" : "SELECT * FROM <table>"
    }

A query with parameters:

    {
       "query"  : "SELECT * FROM <table> WHERE id = ?"
       "params" : [
          { "type" : "INTEGER", "value" : 3 }
       ]
    }


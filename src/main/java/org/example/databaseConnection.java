package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class databaseConnection {
    
    static Connection connection = null;

    public static Connection getConnection(){
        //if we are having connection so we return that connection
        if(connection != null){
            return connection;
        }
        //if we does not having the connection we create new connection to the database
        String db = "searchengine";
        String user = "root";
        String pass = "Abhi@7449";
        return getConnection(db ,user ,pass);

    }
    private static Connection getConnection(String db , String user , String pass){
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
           connection = DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?user="+user+"&password="+pass);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return connection;
    }
}

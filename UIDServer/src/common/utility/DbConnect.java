/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class is used to connect to the database
 * 
 * @author Duy Buffet
 */
public class DbConnect {
    private static DbConnect instance = new DbConnect();
    private Connection conn = null;
    public static final String DATABASE = "UNIQUE_IDENTIFICATION";
    public static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName="+DATABASE;
    public static final String USER = "sa";
    public static final String PASSWORD = "1234567";
    public static final String DB_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private DbConnect() {
        try {
            Class.forName(DB_CLASS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method creates and warrants that there is only one connection
     * to the database at the time application's running.
     * @return Connection This returns a connection to the database 
     */
    private Connection createConnection() {        
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            }            
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    /**
     * This method return to developer a connection to perform SQL queries.
     * @return Connection This returns a connection to the database 
     */
    public static Connection getConnection() {
        return instance.createConnection();
    }   
}

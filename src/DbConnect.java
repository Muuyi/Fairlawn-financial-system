/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andrew
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
public class DbConnect {
     Connection conn = null;
     public static Connection connectDb(){
        try{
            //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/fairlawns","fairlawns","fairlawns");
            return conn;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
    }
    /*private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:fairlawns;create=true";
    Connection conn;
    public void connectDb(){
        try {
            this.conn = DriverManager.getConnection(JDBC_URL);
            if(this.conn != null){
                System.out.println("Connected to database");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
  /*Connection conn = null;
    public static Connection connectDb(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fairlawn","root","");
            return conn;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }*/
    
}

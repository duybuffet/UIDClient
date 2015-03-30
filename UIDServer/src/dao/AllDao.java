/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import common.utility.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DH
 */
public class AllDao {

    public AllDao() {
    }
    
    public ResultSet getTables(String nameTable) throws Exception{
        
        String query = "SELECT * FROM "+nameTable;
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return rs;
    }
    
    public ResultSet selectAllPersonofArea() throws SQLException{
        String query = "SELECT a.AreaCode, COUNT(a.AreaCode) As NumOfCitizens\n" +
                    "FROM Area a\n" +
                    "INNER JOIN Centre c\n" +
                    "ON a.AreaCode = c.AreaCode\n" +
                    "INNER JOIN PersonDetails p \n" +
                    "ON c.CentreId = p.CentreId\n" +
                    "GROUP BY a.AreaCode\n" +
                    "HAVING COUNT(a.AreaCode) > -1";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        
        return rs;
    }
    
    public static void main(String[] args) {
        try {
            ResultSet rs = new AllDao().selectAllPersonofArea();
            while (rs.next()) {
                System.out.println(rs.getString("AreaCode") + "-" + rs.getDouble("NumOfCitizens"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AllDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

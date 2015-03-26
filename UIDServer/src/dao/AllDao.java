/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import common.utility.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import common.model.Area;
import common.model.Centre;
import common.utility.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class is responsible performing SQL queries on table Area
 *
 * @author Duy Buffet
 */
public class AreaDAO {

    public AreaDAO() {
    }

    /**
     * This method performs SELECT query to get all areas from the database.
     *
     * @return ArrayList<Area>
     * @throws SQLException
     */
    public ArrayList<Area> selectAll() throws SQLException {
        ArrayList<Area> arrAreas = new ArrayList<>();
        String query = "SELECT AreaCode, AreaName FROM Area";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            arrAreas.add(new Area(rs.getString("AreaCode"), rs.getString("AreaName")));
        }
        return arrAreas;
    }

    /**
     * This method performs SELECT query to get all areas with their centres
     * from the database.
     *
     * @return ArrayList<Area>
     * @throws SQLException
     */
    public ArrayList<Area> selectAllWithCentres() throws SQLException {
        ArrayList<Area> arrAreas = new ArrayList<>();
        CentreDAO cDao = new CentreDAO();
        String query = "SELECT AreaCode, AreaName FROM Area";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ArrayList<Centre> arrCentres = cDao.selectAllByAreaCode(rs.getString("AreaCode"));
            arrAreas.add(new Area(rs.getString("AreaCode"), rs.getString("AreaName"), arrCentres));
        }
        return arrAreas;
    }

    /**
     * This method performs SELECT query to get an area with specific area code
     * from the database.
     *
     * @param areaCode
     * @return Area
     * @throws SQLException
     */
    public Area selectAreaByCode(String areaCode) throws SQLException {
        Area area = null;
        String query = "SELECT AreaCode, AreaName FROM Area WHERE AreaCode = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, areaCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            area = new Area(rs.getString("AreaCode"), rs.getString("AreaName"));
        }
        return area;
    }

    /**
     * This method performs SELECT query to get an area and its centres with
     * specific area code from the database.
     *
     * @param areaCode
     * @return
     * @throws SQLException
     */
    public Area selectAreaByCodeWithCentres(String areaCode) throws SQLException {
        Area area = null;
        CentreDAO cDao = new CentreDAO();
        String query = "SELECT AreaCode, AreaName FROM Area WHERE AreaCode = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, areaCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            ArrayList<Centre> arrCentres = cDao.selectAllByAreaCode(areaCode);
            area = new Area(rs.getString("AreaCode"), rs.getString("AreaName"), arrCentres);
        }
        return area;
    }

    /**
     * This method performs INSERT query to insert an area into the database.
     *
     * @param area The area we want to insert to the database.
     * @throws SQLException
     */
    public void insert(Area area) throws SQLException {
        String query = "INSERT INTO Area(AreaCode, AreaName)VALUES(?,?)";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, area.getAreaCode());
        ps.setString(2, area.getAreaName());
        ps.executeUpdate();
    }

    /**
     * This method performs UPDATE query to update an area in the database.
     *
     * @param area The area we want to update in the database.
     * @throws SQLException
     */
    public void update(Area area) throws SQLException {
        String query = "UPDATE Area SET AreaName = ? WHERE AreaCode = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, area.getAreaName());
        ps.setString(2, area.getAreaCode());
        ps.executeUpdate();
    }

    /**
     * This method performs DELETE query to delete an area from the database.
     *
     * @param area The area we want to delete from the database.
     * @throws SQLException
     */
    public void delete(Area area) throws SQLException {
        String query = "DELETE FROM Area WHERE AreaCode = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, area.getAreaCode());
        ps.executeUpdate();
    }

}

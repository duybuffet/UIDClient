/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import common.model.Centre;
import common.model.PersonDetails;
import common.utility.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class is responsible performing SQL queries on table Centre
 * @author Duy Buffet
 */
public class CentreDAO {

    public CentreDAO() {
    }

    /**
     * This method performs SELECT query to get all centres from the database.
     *
     * @return ArrayList<Centre>
     * @throws SQLException
     */
    public ArrayList<Centre> selectAll() throws SQLException {
        ArrayList<Centre> arrCentres = new ArrayList<>();
        String query = "SELECT CentreId, CentreName FROM Centre";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            arrCentres.add(new Centre(rs.getInt("CentreId"), rs.getString("CentreName")));
        }
        return arrCentres;
    }

    /**
     * This method performs SELECT query to get all centres with their people
     * from the database.
     *
     * @return ArrayList<Centre>
     * @throws SQLException
     */
    public ArrayList<Centre> selectAllWithPeople() throws SQLException {
        ArrayList<Centre> arrCentres = new ArrayList<>();
        PersonDetailsDAO pdDao = new PersonDetailsDAO();
        String query = "SELECT CentreId, CentreName FROM Centre";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ArrayList<PersonDetails> arrPeople = pdDao.selectAllByCentreId(rs.getInt("CentreId"));
            arrCentres.add(new Centre(rs.getInt("CentreId"), rs.getString("CentreName"), arrPeople));
        }
        return arrCentres;
    }

    /**
     * This method performs SELECT query to get an centre with specific centre
     * id with its people from the database.
     *
     * @param centreId
     * @return Centre
     * @throws SQLException
     */
    public Centre selectCentreById(int centreId) throws SQLException {
        Centre centre = null;
        String query = "SELECT CentreId, CentreName FROM Centre WHERE CentreId = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, centreId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            centre = new Centre(rs.getInt("CentreId"), rs.getString("CentreName"));
        }
        return centre;
    }

    /**
     * This method performs SELECT query to get an centre and its people with
     * specific centre id from the database.
     *
     * @param centreId
     * @return
     * @throws SQLException
     */
    public Centre selectCentreByIdWithPeople(int centreId) throws SQLException {
        Centre centre = null;
        PersonDetailsDAO pdDao = new PersonDetailsDAO();
        String query = "SELECT CentreId, CentreName FROM Centre WHERE CentreId = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, centreId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            ArrayList<PersonDetails> arrayPeople = pdDao.selectAllByCentreId(centreId);
            centre = new Centre(rs.getInt("CentreId"), rs.getString("CentreName"), arrayPeople);
        }
        return centre;
    }

    /**
     * This method performs SELECT query to get all centres with specific area
     * code from the database.
     *
     * @param areaCode The area code is code of the area where we want to select
     * centres from
     * @return ArrayList<Centre>
     * @throws SQLException
     */
    public ArrayList<Centre> selectAllByAreaCode(String areaCode) throws SQLException {
        ArrayList<Centre> arrCentres = new ArrayList<>();
        String query = "SELECT CentreId, CentreName FROM Centre WHERE AreaCode = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, areaCode);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            arrCentres.add(new Centre(rs.getInt("CentreId"), rs.getString("CentreName")));
        }
        return arrCentres;
    }

    /**
     * This method performs INSERT query to insert an centre into the database.
     *
     * @param centre The centre we want to insert to the database.
     * @throws SQLException
     */
    public void insert(String centreName, String areaCode) throws SQLException {
        String query = "INSERT INTO Centre(CentreName, AreaCode)VALUES(?,?)";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, centreName);
        ps.setString(2, areaCode);
        ps.executeUpdate();
    }

    /**
     * This method performs UPDATE query to update an centre in the database.
     *
     * @param centre The centre we want to update in the database.
     * @throws SQLException
     */
    public void update(Centre centre) throws SQLException {
        String query = "UPDATE Centre SET CentreName = ? WHERE CentreId = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, centre.getCentreName());
        ps.setInt(2, centre.getCentreId());
        ps.executeUpdate();
    }

    /**
     * This method performs DELETE query to delete an centre from the database.
     *
     * @param centre The centre we want to delete from the database.
     * @throws SQLException
     */
    public void delete(Centre centre) throws SQLException {
        String query = "DELETE FROM Centre WHERE CentreID = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, centre.getCentreId());
        ps.executeUpdate();
    }
}

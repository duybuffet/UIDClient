/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import common.model.PersonDetails;
import common.utility.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class is responsible performing SQL queries on table PersonDetails
 * @author DH
 */
public class PersonDetailsDAO {

    public PersonDetailsDAO() {
    }
    /**
     * This method performs SELECT query to get all people from the database.
     * @return ArrayList<PersonDetails>
     * @throws SQLException
     */  
    public ArrayList<PersonDetails> selectAll() throws SQLException {
        ArrayList<PersonDetails> arrPersonDetails = new ArrayList<>();
        String query = "SELECT RequestedId, UID,Status,FirstName,MiddleName,LastName,DOB,Gender,Email,Address,Education,Occupation,Married,AddressProof,CitizenshipProof FROM PersonDetails";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            arrPersonDetails.add(new PersonDetails(rs.getInt("RequestedId"), rs.getString("UID"),rs.getInt("Status"),
                     rs.getString("FirstName"), rs.getString("MiddleName"),
                    rs.getString("LastName"), rs.getString("DOB"), rs.getInt("Gender"),
                    rs.getString("Email"), rs.getString("Address"), rs.getString("Education"),
                    rs.getString("Occupation"), rs.getInt("Married"), rs.getInt("AddressProof"),rs.getString("CitizenshipProof")));
        }
        return arrPersonDetails;
    }
    
    /**
     * This method performs SELECT query to get all people of the specific centre id from the database.
     * @param centreId
     * @return ArrayList<PersonDetails>
     * @throws SQLException
     */  
    public ArrayList<PersonDetails> selectAllByCentreId(int centreId) throws SQLException {
        ArrayList<PersonDetails> arrPersonDetails = new ArrayList<>();
        String query = "SELECT RequestedId, UID,Status,FirstName,MiddleName,LastName,DOB,Gender,Email,Address,Education,Occupation,Married,AddressProof,CitizenshipProof FROM PersonDetails WHERE centreId = " + centreId;
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            arrPersonDetails.add(new PersonDetails(rs.getInt("RequestedId"), rs.getString("UID"),rs.getInt("Status"),
                     rs.getString("FirstName"), rs.getString("MiddleName"),
                    rs.getString("LastName"), rs.getString("DOB"), rs.getInt("Gender"),
                    rs.getString("Email"), rs.getString("Address"), rs.getString("Education"),
                    rs.getString("Occupation"), rs.getInt("Married"), rs.getInt("AddressProof"),rs.getString("CitizenshipProof")));
        }
        return arrPersonDetails;
    }

    /**
     * This method performs SELECT query to get a person with specific Unique Identification from the database.
     * @param uid The unique identification
     * @return PersonDetails
     * @throws SQLException
     */
    public PersonDetails selectPersonDetailsByUid(String uid) throws SQLException {
        PersonDetails personDetails = null;
        String query = "SELECT RequestedId, UID,Status,FirstName,MiddleName,LastName,DOB,Gender,Email,Address,Education,Occupation,Married,AddressProof,CitizenshipProof FROM PersonDetails WHERE UID = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, uid);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            personDetails = new PersonDetails(rs.getInt("RequestedId"), rs.getString("UID"),rs.getInt("Status"),
                     rs.getString("FirstName"), rs.getString("MiddleName"),
                    rs.getString("LastName"), rs.getString("DOB"), rs.getInt("Gender"),
                    rs.getString("Email"), rs.getString("Address"), rs.getString("Education"),
                    rs.getString("Occupation"), rs.getInt("Married"), rs.getInt("AddressProof"),rs.getString("CitizenshipProof"));
        }
        return personDetails;
    }
    
    /**
     * This method performs SELECT query to get a person with specific requested id from the database.
     * @param rid Requested ID
     * @return PersonDetails
     * @throws SQLException
     */
    public PersonDetails selectPersonDetailsByRequestedId(int rid) throws SQLException {
        PersonDetails personDetails = null;
        String query = "SELECT UID,Status,FirstName,MiddleName,LastName,DOB,Gender,Email,Address,Education,Occupation,Married,AddressProof,CitizenshipProof FROM PersonDetails WHERE RequestedId = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, rid);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            personDetails = new PersonDetails(rid, rs.getString("UID"),rs.getInt("Status"),
                     rs.getString("FirstName"), rs.getString("MiddleName"),
                    rs.getString("LastName"), rs.getString("DOB"), rs.getInt("Gender"),
                    rs.getString("Email"), rs.getString("Address"), rs.getString("Education"),
                    rs.getString("Occupation"), rs.getInt("Married"), rs.getInt("AddressProof"),rs.getString("CitizenshipProof"));
        }
        return personDetails;
    }

    /**
     * This method performs INSERT query to insert an person into the database.
     * @param personDetails The personDetails we want to insert to the database.
     * @param centreId The centre that person belongs to
     * @throws SQLException
     */
    public void insert(PersonDetails personDetails, int centreId) throws SQLException {
        String query = "INSERT INTO PersonDetails(UID,Status,FirstName,MiddleName,LastName,DOB,Gender,Email,Address,Education,Occupation,Married,AddressProof,CitizenshipProof, CentreId)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, personDetails.getUid());
        ps.setInt(2, personDetails.getStatus());
        ps.setString(3, personDetails.getFirstName());
        ps.setString(4, personDetails.getMiddleName());
        ps.setString(5, personDetails.getLastName());
        ps.setString(6, personDetails.getDob());
        ps.setInt(7, personDetails.getGender());
        ps.setString(8, personDetails.getEmail());
        ps.setString(9, personDetails.getAddress());
        ps.setString(10, personDetails.getEducation());
        ps.setString(11, personDetails.getOccupation());
        ps.setInt(12, personDetails.getMarried());
        ps.setInt(13, personDetails.getAddressProof());
        ps.setString(14, personDetails.getCitizenshipProff());
        ps.setInt(15, centreId);
        ps.executeUpdate();
    }
    /**
     * This method performs UPDATE query to update an person in the database.
     * @param personDetails The personDetails we want to update in the database.
     * @param centreId The centre that person belongs to
     * @throws SQLException
     */
    public void update(PersonDetails personDetails, int centreId) throws SQLException {
        String query = "UPDATE PersonDetails SET Status = ?,FirstName = ?,MiddleName = ?,LastName = ?,DOB = ?,Gender = ?,Email = ?,Address = ?,Education = ?,Occupation = ?,Married = ?,AddressProof = ?,CitizenshipProof = ?, CentreId = ? WHERE RequestedId = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, personDetails.getStatus());
        ps.setString(2, personDetails.getFirstName());
        ps.setString(3, personDetails.getMiddleName());
        ps.setString(4, personDetails.getLastName());
        ps.setString(5, personDetails.getDob());
        ps.setInt(6, personDetails.getGender());
        ps.setString(7, personDetails.getEmail());
        ps.setString(8, personDetails.getAddress());
        ps.setString(9, personDetails.getEducation());
        ps.setString(10, personDetails.getOccupation());
        ps.setInt(11, personDetails.getMarried());
        ps.setInt(12, personDetails.getAddressProof());
        ps.setString(13, personDetails.getCitizenshipProff());
        ps.setInt(14, centreId);
        ps.setInt(15, personDetails.getRequestedId());
        ps.executeUpdate();
    }
    /**
     * This method performs DELETE query to delete an person from the database.
     * @param personDetails The personDetails we want to delete from the database.
     * @throws SQLException
     */
    public void delete(PersonDetails personDetails) throws SQLException {
        String query = "DELETE FROM PersonDetails WHERE RequestedId = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, personDetails.getRequestedId());
        ps.executeUpdate();
    }
    
    /**
     * This method count how many record there are in PersonDetails table. 
     * @return int Number of record
     * @throws SQLException
     */
    public int countRecord() throws SQLException {
        int total = 0;
        String query = "SELECT RequestedId FROM PersonDetails";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            total += 1;
        }
        return total;
    }

}   
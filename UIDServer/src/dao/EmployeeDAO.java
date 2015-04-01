/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import common.model.Employee;
import common.utility.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class is responsible performing SQL queries on table Employee
 * @author Tran
 */
public class EmployeeDAO {

    public EmployeeDAO() {
    }

    /**
     * This method performs SELECT query to get all employees from the database.
     * @return ArrayList<Employee>
     * @throws SQLException
     */
    public ArrayList<Employee> selectAllEmployee() throws SQLException {
        ArrayList<Employee> arrEmployee = new ArrayList<>();
        String query = "SELECT Id,Username,Pass,Gender FROM Employee";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            arrEmployee.add(new Employee(rs.getInt("Id"),rs.getString("Username"),rs.getString("Pass"),rs.getInt("Gender")));
        }
        return arrEmployee;
    }

    /**
     * This method performs SELECT query to get an employee specified by employee id from the database.
     * @param id
     * @return
     * @throws SQLException
     */
    public Employee selectEmployeeByID(int id) throws SQLException {
        Employee employe = null;
        String query = "SELECT Id,Username,Pass,Gender FROM Employee WHERE Id = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            employe = new Employee(rs.getInt("Id"),rs.getString("Username"),rs.getString("Pass"),rs.getInt("Gender"));
        }
        return employe;
    }
    
    /**
     * This method performs INSERT query to insert an employee into the database.
     * @param employee
     * @throws SQLException
     */
    public void insert(Employee employee) throws SQLException {
        String query = "INSERT INTO Employee(Username,Pass,Gender)VALUES(?,?,?)";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, employee.getUsername());
        ps.setString(2, employee.getPass());
        ps.setInt(3, employee.getGender());
        ps.executeUpdate();
    }
    
    /**
     * This method performs UPDATE query to update an employee in the database.
     * @param employee
     * @throws SQLException
     */
    public void update(Employee employee) throws SQLException {
        String query = "UPDATE Employee SET Username = ? , Pass = ?, Gender = ? WHERE Id = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, employee.getUsername());
        ps.setString(2, employee.getPass());
        ps.setInt(3, employee.getGender());
        ps.setInt(4, employee.getId());
        
        ps.executeUpdate();
    }
    
    /**
     * This method performs DELETE query to delete an employee from the database.
     *
     * @param employee The employee we want to delete from the database.
     * @throws SQLException
     */
    public void delete(Employee employee) throws SQLException {
        String query = "DELETE FROM Employee WHERE Id = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setInt(1, employee.getId());
        ps.executeUpdate();
    }
    
    public boolean login(Employee employee) throws SQLException {
        boolean flag = false;
        String query = "SELECT Username,Pass FROM Employee WHERE Username = ? AND Pass = ?";
        PreparedStatement ps = DbConnect.getConnection().prepareStatement(query);
        ps.setString(1, employee.getUsername());
        ps.setString(2, employee.getPass());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            flag = true;
        }
        return flag;
    }
    
}

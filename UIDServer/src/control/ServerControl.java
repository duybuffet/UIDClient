/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import common.model.Employee;
import dao.EmployeeDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import view.AreaPanel;
import view.CentrePanel;
import view.EmployeePanel;
import view.LoginPanel;
import view.MenuPanel;
import view.RequestPanel;
import view.ServerFrame;

/**
 *
 * @author Duy Buffet
 */
public class ServerControl {
    private ServerFrame serverFrame;
    private AreaPanel areaPanel;
    private EmployeePanel employeePanel;
    private LoginPanel loginPanel;
    private RequestPanel requestPanel;
    private MenuPanel menuPanel;

    public ServerControl(ServerFrame serverFrame) {        
        initComponents(serverFrame);
    }

    private void initComponents(ServerFrame serverFrame) {
        // init all components
        this.serverFrame = serverFrame;
        loginPanel = new LoginPanel();        
        areaPanel = new AreaPanel();
        employeePanel = new EmployeePanel();
        requestPanel = new RequestPanel();
        menuPanel = new MenuPanel();
        
        // set login panel first when main frame is opened
        this.serverFrame.getMainSplitPane().setRightComponent(loginPanel);
        this.serverFrame.getMainSplitPane().setLeftComponent(menuPanel);
        
        // add action listener
        loginPanel.addBtnLoginListener(new LoginListener());        
    }

    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = "", pass = "";
            Employee employee = new Employee();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            userName = loginPanel.getTxtUName().getText().trim();
            pass = loginPanel.getTxtPass().getText().trim();
            
            employee.setUsername(userName);
            employee.setPass(pass);
            
//            try {
//                if (employeeDAO.login(employee)) {
//                    serverFrame.showMessage("Login successfully!");
                    serverFrame.getMainSplitPane().setRightComponent(employeePanel);
                    menuPanel.addBtnMenuListener(new MenuListener());
                    serverFrame.addBtnControlListener(new ControlServerListener());                    
//                } else {
//                    serverFrame.showMessage("Login unsuccessfully. Please reinput username or password");
//                }
//            } catch (SQLException ex) {
//                serverFrame.showMessage("Server error! Sorry for this unconvenient.");
//            }
        }
        
    }
    
    class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
            if (btn == menuPanel.getBtnArea()) {
                serverFrame.getMainSplitPane().setRightComponent(areaPanel);
                areaPanel.addBtnAreaListener(new AreaListener());
            } else if (btn == menuPanel.getBtnEmp()) {
                serverFrame.getMainSplitPane().setRightComponent(employeePanel);
                employeePanel.addBtnEmployeeListener(new EmployeeListener());
            } else if (btn == menuPanel.getBtnRequest()) {
                serverFrame.getMainSplitPane().setRightComponent(requestPanel);
                
            }
        }
        
    }
    
    class ControlServerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    class AreaListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (btn == areaPanel.getBtnAdd()) {
                // add
            }
        }
        
    }
    
    class CentreListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    class RequestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    class EmployeeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}

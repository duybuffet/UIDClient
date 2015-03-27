/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import remote.RMICitizenAction;
import view.ClientFrame;
import view.InfoDisplayPanel;
import view.OptionPanel;
import view.RegisterPanel;
import view.SearchPanel;

/**
 *
 * @author Duy Buffet
 */
public class ClientControl {

    private ClientFrame clientFrame;
    private InfoDisplayPanel infoPanel;
    private RegisterPanel registerPanel;
    private SearchPanel searchPanel;
    private OptionPanel optionPanel;

    private static final int SERVER_PORT = 8989;
    private static final String SERVER_HOST = "localhost";
    private static final String RMI_SERVICE = "RMIClientAction";
    private Registry registry;
    private RMICitizenAction remoteObject;

    public ClientControl(ClientFrame clientFrame) {
        initComponent(clientFrame);
        connectServer();
    }

    private void initComponent(ClientFrame clientFrame) {
        this.clientFrame = clientFrame;
        infoPanel = new InfoDisplayPanel();
        registerPanel = new RegisterPanel();
        searchPanel = new SearchPanel();
        optionPanel = new OptionPanel();

        clientFrame.setContentPane(optionPanel);
        optionPanel.addBtnListener(new OptionListener());
    }

    private void connectServer() {
        try {
            registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
            remoteObject = (RMICitizenAction)registry.lookup(RMI_SERVICE);
            clientFrame.showMessage("Server is running!");
        } catch (RemoteException ex) {
            clientFrame.showMessage("Server has not started already! Please close this application!");
        } catch (NotBoundException ex) {
            clientFrame.showMessage("Server has not started already! Please close this application!");
        }
    }
    
    class OptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
            if (btn == optionPanel.getBtnRegister()) {
                clientFrame.setContentPane(registerPanel);
                registerPanel.addBtnListener(new RegisterListener());
//                clientFrame.showMessage("register");
            } else if (btn == optionPanel.getBtnSearch()) {                
                clientFrame.setContentPane(searchPanel);
                searchPanel.addBtnListener(new SearchListener());
//                clientFrame.showMessage("search");
            }
        }
        
    }
    
    class RegisterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
            if (btn == registerPanel.getBtnBack()) {
                clientFrame.setContentPane(optionPanel);
            } else if (btn == registerPanel.getBtnReset()) {
                
            } else if (btn == registerPanel.getBtnSubmit()) { 
                
            }
        }
        
    }
    
    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
            if (btn == searchPanel.getBtnBack()) {
                clientFrame.setContentPane(optionPanel);
            } else if (btn == searchPanel.getBtnSearch()) {
                
            } 
        }
        
    }
}

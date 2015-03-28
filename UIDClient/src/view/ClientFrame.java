/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import de.javasoft.plaf.synthetica.SyntheticaClassyLookAndFeel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import remote.RMICitizenAction;

/**
 *
 * @author Duy Buffet
 */
public class ClientFrame extends javax.swing.JFrame {

    private static final int SERVER_PORT = 8989;
    private static final String SERVER_HOST = "localhost";
    private static final String RMI_SERVICE = "RMIClientAction";
    private Registry registry;
    private RMICitizenAction remoteObject;

    /**
     * Creates new form ClientFrame
     */
    public ClientFrame() {
        initComponents();
        this.tbMainPane.add("Register", new RegisterPanel());
        this.tbMainPane.add("Search", new SearchPanel());
        setResizable(false);
        connectServer();
        try {
            UIManager.setLookAndFeel(new SyntheticaClassyLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tbMainPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbMainPane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbMainPane, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */    

    private void connectServer() {
        try {
            registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
            remoteObject = (RMICitizenAction) registry.lookup(RMI_SERVICE);
            this.setTitle(this.getTitle() + ". SERVER: RUNNING");
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Server has not started already! Please close this application!");
        } catch (NotBoundException ex) {
            JOptionPane.showMessageDialog(this, "Server has not started already! Please close this application!");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tbMainPane;
    // End of variables declaration//GEN-END:variables
}

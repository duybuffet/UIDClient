/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import common.model.Area;
import common.model.Centre;
import common.model.Employee;
import dao.AllDao;
import dao.AreaDAO;
import dao.CentreDAO;
import dao.EmployeeDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import view.AreaPanel;
import view.CentreFrame;
import view.ChartPanel;
import view.EmployeePanel;
import view.ExportToExcelPanel;
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
    private CentreFrame centreFrame;
    private AreaPanel areaPanel;
    private EmployeePanel employeePanel;
    private LoginPanel loginPanel;
    private RequestPanel requestPanel;
    private MenuPanel menuPanel;
    private ExportToExcelPanel exportToExcelPanel;

    private AreaDAO areaDAO;
    private String areaCode, areaName;
    private String areaCodeTemp;

    private CentreDAO centreDAO;
    private String centreName;
    private int centreID;
    
    private ChartPanel chartPanel;
    
    private HSSFWorkbook wb;
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
        exportToExcelPanel = new ExportToExcelPanel();
        chartPanel = new ChartPanel();

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
            JButton btn = (JButton) e.getSource();
            if (btn == menuPanel.getBtnArea()) {
                serverFrame.getMainSplitPane().setRightComponent(areaPanel);
                areaPanel.addBtnAreaListener(new AreaListener());
                new AreaListener().showAllArea();
            } else if (btn == menuPanel.getBtnEmp()) {
                serverFrame.getMainSplitPane().setRightComponent(employeePanel);
                employeePanel.addBtnEmployeeListener(new EmployeeListener());
            } else if (btn == menuPanel.getBtnRequest()) {
                serverFrame.getMainSplitPane().setRightComponent(requestPanel);
            }
            else if (btn == menuPanel.getBtnExportToExcel()) {
                serverFrame.getMainSplitPane().setRightComponent(exportToExcelPanel);
                exportToExcelPanel.addBtnExportToExcelListener(new ExportToExcelListener());
                new ExportToExcelListener().showAllArea();
                new ExportToExcelListener().showAllCentre();
                new ExportToExcelListener().showAllEmployee();
            }
            else if (btn == menuPanel.getBtnChart()) {
                serverFrame.getMainSplitPane().setRightComponent(chartPanel);
                ChartArea chart = new ChartArea();
                chart.showChart();
//                chart.
//                showChart();
//                new ChartArea().showChart();
                
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

            // add
            if (btn == areaPanel.getBtnAdd()) {
                // check JTextFiel areaCode and areaName
                if(testTxtInput() != false){
                    insertArea();
                }

            }
            // show all
            if (btn == areaPanel.getBtnAll()) {
                showAllArea();
            }
            // edit
            if(btn == areaPanel.getBtnEdit()){
                if(testTxtInput() != false){
                    updateArea();
                }
            }
            // Delete
            if(btn == areaPanel.getBtnDel()){
                if(testTxtInput() != false){
                    deleteArea();
                }
            }
            // Search
            if(btn == areaPanel.getBtnSearch()){
                String search = JOptionPane.showInputDialog(areaPanel,"Input Key Search ");
                if(search != null){
                    showSearchArea(search);
                }
            }
            // Go to centre
            if(btn == areaPanel.getBtnGoto()){
                gotoCentre();
            }
        }
        // Insert Area
        private void insertArea() {
            int result = JOptionPane.showConfirmDialog(areaPanel, "AreCode : " + areaCode + "\nAreaName : " + areaName, "Are you update?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {

                areaDAO = new AreaDAO();
                try {
                    areaDAO.insert(new Area(areaCode, areaName));
                    showMessageDialog("Insert Success!");
                    areaPanel.getTxtCode().setText("");
                    areaPanel.getTxtName().setText("");
                    showAllArea();
                } catch (SQLException ex) {
                    showMessageDialog("Insert wrong!");
                    Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //Test input area code and area name
        private boolean testTxtInput() {
            int countTest = 0;
            boolean test = false;
            areaCode = areaPanel.getTxtCode().getText().trim();
            areaName = areaPanel.getTxtName().getText().trim();
            
            if (areaCode.isEmpty()|| areaCode.length()!=2) {
                countTest += 1;
            }
            if (areaName.isEmpty()) {
                countTest += 2;
            }
            switch (countTest) {
                case 0:
                    test = true;
                    break;
                case 1:
                    showMessageDialog("Request:\nAreaCode not null and AreaCode not equals 2 key!");
                    break;
                case 2:
                    showMessageDialog("Request:\nAreaName not Null!");
                    break;
                case 3:
                    showMessageDialog("Request:\nAreaCode not null and AreaCode not equals 2 key!\nAreaName not Null!");
                    break;
            }
            return test;
        }

        private void updateArea() {
            System.out.println(areaCodeTemp);
            System.out.println(areaCode);
            
            int result = JOptionPane.showConfirmDialog(areaPanel, "AreCode : " + areaCode + "\nAreaName : " + areaName, "Are you insert?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if(!areaCodeTemp.equals(areaCode)){
                    showMessageDialog(" You not change Area Code!");
                }
                else{
                    areaDAO = new AreaDAO();
                    try {
                        areaDAO.update(new Area(areaCode, areaName));
                        showMessageDialog("Update Success!");
                        showAllArea(); 
                    } catch (SQLException ex) {
                        showMessageDialog("Update wrong!");
                        Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        private void showAllArea() {
            areaDAO = new AreaDAO();
            ArrayList<Area> listArae = new ArrayList<>();
            try {
                listArae = areaDAO.selectAll();
                Vector tblRecords = new Vector();
                Vector tblTitle = new Vector();
                tblTitle.add("Area Code");
                tblTitle.add("Area Name");

                for (Area ls : listArae) {
                    Vector record = new Vector();
                    record.add(ls.getAreaCode());
                    record.add(ls.getAreaName());
                    tblRecords.add(record);
                }

                areaPanel.getTblArea().setModel(new DefaultTableModel(tblRecords, tblTitle));
                eventTableArea();
            } catch (SQLException ex) {
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void deleteArea() {
            int result = JOptionPane.showConfirmDialog(areaPanel, "AreCode : " + areaCode + "\nAreaName : " + areaName, "Are you delete?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {

                areaDAO = new AreaDAO();
                try {
                    areaDAO.delete(new Area(areaCode, areaName));
                    showMessageDialog("Delete Success!");
                    showAllArea();
                } catch (SQLException ex) {
                    showMessageDialog("Delete wrong!");
                    Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void eventTableArea() {
            areaPanel.getTblArea().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int row = areaPanel.getTblArea().getSelectedRow();

                    areaPanel.getTxtCode().setText((String) areaPanel.getTblArea().getValueAt(row, 0));
                    areaPanel.getTxtName().setText((String) areaPanel.getTblArea().getValueAt(row, 1));
                    areaCodeTemp = (String) areaPanel.getTblArea().getValueAt(row, 0);
//                    areaPanel.getTxtCode().setEditable(false);
                }
            });
            
        }

        private void showSearchArea(String areaCode) {
            areaDAO = new AreaDAO();
            Area area = new Area();
            try {
                area = areaDAO.selectAreaByCode(areaCode);
                if(area != null){
                    Vector tblRecords = new Vector();
                    Vector tblTitle = new Vector();
                    tblTitle.add("Area Code");
                    tblTitle.add("Area Name");

                    Vector record = new Vector();
                    record.add(area.getAreaCode());
                    record.add(area.getAreaName());
                    tblRecords.add(record);

                    areaPanel.getTblArea().setModel(new DefaultTableModel(tblRecords, tblTitle));
                    eventTableArea();
                }
                else{
                    showMessageDialog("Enter key search Incorrect!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void showMessageDialog(String message) {
            JOptionPane.showMessageDialog(areaPanel, message);
        }

        private void gotoCentre() {
            areaCode = areaPanel.getTxtCode().getText().trim();
            if(!areaCode.isEmpty()){
                centreFrame = new CentreFrame();
                centreFrame.setVisible(true);
                centreFrame.addBtnCentreListener(new CentreListener());
                centreFrame.getTxtAreaCode().setText(areaCode);
                new CentreListener().showAllCentre();
            }
            else{
                showMessageDialog("Not Choice row in table !");
            }
        }
    }

    class CentreListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (btn == centreFrame.getBtnAdd()) {
                // check JTextFiel areaCode and areaName
                if(testTxtInput() != false){
                    insertCentre();
                }

            }
            // show all
            if (btn == centreFrame.getBtnAll()) {
                showAllCentre();
            }
            // edit
            if(btn == centreFrame.getBtnEdit()){
                if(testTxtInput() != false){
                    updateCentre();
                }
            }
            // Delete
            if(btn == centreFrame.getBtnDel()){
                if(testTxtInput() != false){
                    deleteCentre();
                }
            }
            // Search
            if(btn == centreFrame.getBtnSearch()){
                try {
                    int search =Integer.parseInt(JOptionPane.showInputDialog(centreFrame,"Input Key Search "));
                    if(search != 0){
                        showSearchCentre(search);
                    }
                } catch (NumberFormatException ex) {
                }
            }
        }
        // Insert Area
        private void insertCentre() {
            int result = JOptionPane.showConfirmDialog(centreFrame, "CentreName : " + centreName + "\nArea Code : " + areaCode, "Are you insert?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                
                centreDAO = new CentreDAO();
                try {
                    centreDAO.insert(centreName,areaCode);
                    showMessageDialog("Insert Success!");
                    centreFrame.getTxtId().setText("");
                    centreFrame.getTxtName().setText("");
                    showAllCentre();
                } catch (SQLException ex) {
                    showMessageDialog("Insert wrong!");
//                    showMessageDialog(ex.toString());
                    Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //Test input area code and area name
        private boolean testTxtInput() {
            int countTest = 0;
            boolean test = false;
            centreName = centreFrame.getTxtName().getText().trim();

            if (centreName.isEmpty()) {
                countTest += 2;
            }
            switch (countTest) {
                case 0:
                    test = true;
                    break;
                case 2:
                    showMessageDialog("Request:\nCentre Name is Null!");
                    break;
            }
            return test;
        }

        private void updateCentre() {
            int result = JOptionPane.showConfirmDialog(centreFrame, "CentreID : " + centreID + "\nCentreName : " + centreName, "Are you insert?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {

                centreDAO = new CentreDAO();
                try {
                    centreDAO.update(new Centre(centreID, centreName));
                    showMessageDialog("Update Success!");
                    showAllCentre(); 
                } catch (SQLException ex) {
                    showMessageDialog("Update wrong!");
                    Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void showAllCentre() {
            centreDAO = new CentreDAO();
            ArrayList<Centre> listCentre = new ArrayList<>();
            try {
                listCentre = centreDAO.selectAllByAreaCode(areaCode);
                Vector tblRecords = new Vector();
                Vector tblTitle = new Vector();
                tblTitle.add("Centre ID");
                tblTitle.add("Centre Name");

                for (Centre lc : listCentre) {
                    Vector record = new Vector();
                    record.add(lc.getCentreId());
                    record.add(lc.getCentreName());
                    tblRecords.add(record);
                }

                centreFrame.getTblCentre().setModel(new DefaultTableModel(tblRecords, tblTitle));
                eventTableCentre();
            } catch (SQLException ex) {
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void deleteCentre() {
            int result = JOptionPane.showConfirmDialog(centreFrame, "Centre ID : " + centreID + "\nCentre Name : " + centreName, "Are you delete?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {

                centreDAO = new CentreDAO();
                try {
                    centreDAO.delete(new Centre(centreID, centreName));
                    showMessageDialog("Delete Success!");
                    showAllCentre();
                } catch (SQLException ex) {
                    showMessageDialog("Delete wrong!");
                    Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void eventTableCentre() {
            centreFrame.getTblCentre().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int row = centreFrame.getTblCentre().getSelectedRow();
                    
                    centreFrame.getTxtId().setText((String) centreFrame.getTblCentre().getValueAt(row, 0).toString());
                    centreFrame.getTxtName().setText((String) centreFrame.getTblCentre().getValueAt(row, 1));
                    centreID = Integer.parseInt(centreFrame.getTblCentre().getValueAt(row, 0).toString());
//                    areaPanel.getTxtCode().setEditable(false);
                }
            });
            
        }

        private void showSearchCentre(int centreID) {
            centreDAO = new CentreDAO();
            Centre centre = new Centre();
            try {
                centre = centreDAO.selectCentreById(centreID);
                if(centre != null){
                    Vector tblRecords = new Vector();
                    Vector tblTitle = new Vector();
                    tblTitle.add("Centre ID");
                    tblTitle.add("Centre Name");

                    Vector record = new Vector();
                    record.add(centre.getCentreId());
                    record.add(centre.getCentreName());
                    tblRecords.add(record);

                    centreFrame.getTblCentre().setModel(new DefaultTableModel(tblRecords, tblTitle));
                    eventTableCentre();
                }
                else{
                    showMessageDialog("Enter key search Incorrect!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        private void showMessageDialog(String message) {
            JOptionPane.showMessageDialog(centreFrame, message);
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
    class ExportToExcelListener implements ActionListener,ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            int index = exportToExcelPanel.getTabPanel().getSelectedIndex();
            if(index == 0){
//                showAllArea();
            }
            if(index == 1){
//                showAllCentre();
            }
            if(index == 2){
//                showAllArea();
            }
        }
        private void showAllArea() {
            areaDAO = new AreaDAO();
            ArrayList<Area> listArae = new ArrayList<>();
            try {
                listArae = areaDAO.selectAll();
                Vector tblRecords = new Vector();
                Vector tblTitle = new Vector();
                tblTitle.add("Area Code");
                tblTitle.add("Area Name");

                for (Area ls : listArae) {
                    Vector record = new Vector();
                    record.add(ls.getAreaCode());
                    record.add(ls.getAreaName());
                    tblRecords.add(record);
                }
                exportToExcelPanel.getTblArea().setModel(new DefaultTableModel(tblRecords, tblTitle));
            } catch (SQLException ex) {
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void showAllCentre() {
            centreDAO = new CentreDAO();
            ArrayList<Centre> listCentre = new ArrayList<>();
            try {
                listCentre = centreDAO.selectAll();
                Vector tblRecords = new Vector();
                Vector tblTitle = new Vector();
                tblTitle.add("Centre ID");
                tblTitle.add("Centre Name");

                for (Centre lc : listCentre) {
                    Vector record = new Vector();
                    record.add(lc.getCentreId());
                    record.add(lc.getCentreName());
                    tblRecords.add(record);
                }
                exportToExcelPanel.getTblCentre().setModel(new DefaultTableModel(tblRecords, tblTitle));
            } catch (SQLException ex) {
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        private void showAllEmployee() {
        
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (btn == exportToExcelPanel.getBtnExportToExcel()) {
                ExportToExcel();        
            }
        }
        private void ExportToExcel() {
            String[] nameTableData = {"Area","Centre","Employee"};
            String[] nameTable = {"Areas sheet","Centre sheet","Employee sheet"};
            String[][] nameColumn = {{"Area Code","Area Name"},
                                     {"Centre ID","Area Code","Centre Name"},
                                     {"Id","Username","Pass","Gender"}};
            try {
                HSSFWorkbook wb = new HSSFWorkbook();
                for (int i = 0; i < nameTableData.length; i++) {
                    ResultSet rs = new AllDao().getTables(nameTableData[i]);
                    HSSFSheet sheet = wb.createSheet(nameTable[i]);
                    HSSFRow rowhead = sheet.createRow((short) 0);
                    for (int j = 0; j < nameColumn[i].length; j++) {
                        rowhead.createCell((short) j).setCellValue(nameColumn[i][j]);
                    }
                    int index = 1;
                    while (rs.next()) {
                        HSSFRow row = sheet.createRow((short) index);
                        for (int x = 0; x < nameColumn[i].length; x++) {
                            row.createCell((short) x).setCellValue(rs.getString(x+1));
                        }
                        index++;
                    }
                }
                FileOutputStream fileOut = new FileOutputStream("D:\\excelFile.xls");
                wb.write(fileOut);
                fileOut.close();
                JOptionPane.showMessageDialog(exportToExcelPanel, "Export file Excel Success ! Folder: D:\\excelFile.xls");
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(exportToExcelPanel, "Export file excel Fails!");
                Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public class ChartArea {
        private CategoryDataset createDataset() {
            // row keys...
        final String series1 = "First";
        final String series2 = "Second";
        final String series3 = "Third";

        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";
        final String category5 = "Category 5";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);

        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        dataset.addValue(6.0, series3, category5);
        
        return dataset;
        
    }
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart Demo",         // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
        public void showChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(6, "Profit1", "Jane");
        dataset.setValue(3, "Profit2", "Jane");
        dataset.setValue(7, "Profit1", "Tom");
        dataset.setValue(10, "Profit2", "Tom");
        dataset.setValue(8, "Profit1", "Jill");
        dataset.setValue(8, "Profit2", "Jill");
        dataset.setValue(5, "Profit1", "John");
        dataset.setValue(6, "Profit2", "John");
        dataset.setValue(12, "Profit1", "Fred");
        dataset.setValue(5, "Profit2", "Fred");
        // Profit1, Profit2 represent the row keys
        // Jane, Tom, Jill, etc. represent the column keys
        JFreeChart chart = ChartFactory.createBarChart3D( "Comparison between Salesman", 
        "Salesman", "Value ($)", dataset, PlotOrientation.VERTICAL, true, true, false );
        chart.setBackgroundPaint(Color.yellow); // Set the background colour of the chart
        chart.getTitle().setPaint(Color.blue); // Adjust the colour of the title
        CategoryPlot p = chart.getCategoryPlot(); // Get the Plot object for a bar graph
        p.setBackgroundPaint(Color.black); // Modify the plot background 
        p.setRangeGridlinePaint(Color.red);
        }
    }
}

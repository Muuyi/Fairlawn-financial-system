/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andrew
 */
import java.util.Date;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.MessageFormat;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

public class MainPanel extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    Statement st = null;
    PreparedStatement pst = null;
    public static String accId = null;
    public static String transId = null;
    public static String  classAmnt = null;
    public static String payId = null;
    public static String supplierId = null;
    public static String suplyId = null;
    public static String employeeId = null;
    public static String budgetId = null;
    public static String firstName = null;
    public static String lastName = null;
    /**
     * Creates new form MainPanel
     */
    public MainPanel() {
        initComponents();
        conn = DbConnect.connectDb();
        setLocationRelativeTo(null);
        chartsTable();
        transTable();
        totalTransactions();
        pupilsPayments();
        classPayments();
        fillPaymentCombo();
        AddTransaction.setLocationRelativeTo(null);
        FillTransCombo();
        FillAccountCombo();
        AddAccount.setLocationRelativeTo(null);
        UpdateAccounts.setLocationRelativeTo(null);
        UpdateTransaction.setLocationRelativeTo(null);
        Payments.setLocationRelativeTo(null);
        AddClass.setLocationRelativeTo(null);
        AddPupil.setLocationRelativeTo(null);
        EditPayments.setLocationRelativeTo(null);
        AddSupplier.setLocationRelativeTo(null);
        EditSupplier.setLocationRelativeTo(null);
        AddSupplies.setLocationRelativeTo(null);
        EditSupplies.setLocationRelativeTo(null);
        AddEmployee.setLocationRelativeTo(null);
        EditEmployee.setLocationRelativeTo(null);
        AddSalary.setLocationRelativeTo(null);
        AddPeriod.setLocationRelativeTo(null);
        AddBudget.setLocationRelativeTo(null);
        EditBudget.setLocationRelativeTo(null);
        FillUpdateAccountCombo();
        FillUpdateTransCombo();
        fillClass();
        paymnetSummary();
        fillPaymentClass();
        fillEditPaymentClass();
        suppliersTable();
        suppliesProducts();
        suppliesTable();
        editsuppliesProducts();
        fillProfessionCombo();
        fillDepartmentCombo();
        employeesTable();
        salaryComboBox();
        salaryTable();
        strtprdCombo();
        budgetTable();
    }
    //DISPLAYING BUDGETS IN BUDGTES TABLE
    private void budgetTable(){
        try{
            String sql = "SELECT bug_id AS ID, start_period AS BEGINNING_PERIOD,acc_name AS ACCOUNT, acat_name AS CATEGORY,bug_amount AS AMOUND, bug_date AS DATE_ADDED"
                    + " FROM budgets ORDER BY start_period, acc_name ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            budgetTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //DISPLAYING STARTING PERIOD IN JCOMBOBOX
    private void strtprdCombo(){
        try{
            String sql = "SELECT * FROM budget_period";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String period = rs.getString("start_period");
                bstartPeriod.addItem(period);
                btsttPanCombo.addItem(period);
                bstartPeriod1.addItem(period);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //DISPLAYING SALARIES TABLE
    private void salaryTable(){
        try{
            String sql = "SELECT salaries.sal_id AS ID,employees.emp_fname AS FIRST_NAME,employees.emp_lname AS LAST_NAME,salaries.emp_no AS EMPLOYEE_NO,"
                    + "employees.salary AS BASIC_SALARY, employees.bonusses AS BONUSSES, employees.allowances AS ALLOWANCES,employees.deductions AS DEDUCTIONS,"
                    + "salaries.sal_date AS SALARY_DATE,(employees.salary + employees.bonusses + employees.allowances -employees.deductions) AS NET_SALARY"
                    + " FROM salaries INNER JOIN employees ON salaries.emp_no = employees.emp_no";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            salaryTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL SALARIES COMBO BOX
    private void salaryComboBox(){
        try {
            String sql = "SELECT * FROM employees";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String emplNo = rs.getString("emp_no");
                salEmbNo.addItem(emplNo);
            }
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL EMPLOYEES TABLE
    private void employeesTable(){
        try {
            String sql = "SELECT emp_id AS ID,pos_name AS DESIGNATION, dep_name AS DEPARTMENT,emp_fname AS FIRST_NAME, emp_lname AS LAST_NAME,id_no AS ID_NO,phone_no"
                    + " AS PHONE_NO,emp_no AS EMPLOYEE_NO,emp_email AS EMAIL, date_of_appointment AS APPOINTMENT_DATE FROM employees ";
            pst= conn.prepareStatement(sql);
            rs = pst.executeQuery();
            employeesTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    //FILL EMPLOYEE DEPARTMENT COMBO BOX
    public void fillDepartmentCombo(){
        try{
            String sql = "SELECT * FROM departments";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String dep = rs.getString("dep_name");
               departNme.addItem(dep);
               departNme1.addItem(dep);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL PROFFESSION COMBO BOX
    public void fillProfessionCombo(){
        try{
            String sql = "SELECT * FROM positions";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String pos = rs.getString("pos_name");
                profName.addItem(pos);
                profName1.addItem(pos);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL SUPPLIES TABLE
    private void suppliesTable(){
        try{
            String sql = "SELECT supplies.suply_id AS ID, suppliers.sup_fname AS FIRSE_NAME, suppliers.sup_lname AS LAST_NAME, supplies.sup_product AS PRODUCT,"
                    + "supplies.suply_amount AS AMOUNT, supplies.suply_date AS SUPPLY_DATE FROM supplies INNER JOIN suppliers ON supplies.sup_product=suppliers.sup_product";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            suppliesTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL EDIT SUPPLIES PRODUCTS COMBO BOX
    private void editsuppliesProducts(){
        try{
            String sql = "SELECT * FROM suppliers";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String product = rs.getString("sup_product");
                editsuplyProduct.addItem(product);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL SUPPLIES PRODUCTS COMBO BOX
    private void suppliesProducts(){
        try{
            String sql = "SELECT * FROM suppliers";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String product = rs.getString("sup_product");
                suplyProduct.addItem(product);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //SUPPLIERS TABLE
    public void suppliersTable(){
        try {
            String sql = "SELECT sup_id AS ID, sup_fname AS FIRST_NAME, sup_lname AS LAST_NAME,sup_email AS EMAIL,sup_phone AS PHONE, sup_product AS PRODUCT, sup_date"
                    + " AS DATE_REGISTERED FROM suppliers";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            suppliersTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //TOTAL PAYMENTS SECTIONS
    public void paymnetSummary(){
        try {
            String sql = "SELECT SUM(amount) AS TOTAL, SUM(balance) AS BALANCE, (SUM(amount) + SUM(balance)) AS TOTAL_PAYMENTS FROM payments";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                String total = rs.getString("TOTAL");
                totalFees.setText("Kshs. "+total);
                String balance = rs.getString("BALANCE");
                totalBalance.setText("Kshs. "+balance);
                int bdTotal = Integer.parseInt(total);
                int dbBala = Integer.parseInt(balance);
                String ttlAmnt = rs.getString("TOTAL_PAYMENTS");
                int dbttlAmnt = Integer.parseInt(ttlAmnt);
                int percntPaid = (bdTotal * 100)/dbttlAmnt;
                int pcntBal = (dbBala * 100)/dbttlAmnt;
                String totalPaid = Integer.toString(percntPaid);
                String pctBal = Integer.toString(pcntBal);
                pcntPaid.setText(totalPaid+"%");
                pcntBalance.setText(pctBal+"%");
               
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILLING THE CLASS COMBO BOX
    public void fillClass(){
        try{
            String sql = "SELECT * FROM classes";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String pplClass = rs.getString("class_name");
                pupilClass.addItem(pplClass);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILLING THE PAYMENT COMBO BOX
    public void fillPaymentClass(){
        try{
            String sql = "SELECT * FROM classes";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String pplClass = rs.getString("class_name");
                pupilClassItm.addItem(pplClass);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL EDIT PAYMENTS COMBO BOX
    public void fillEditPaymentClass(){
        try{
            String sql = "SELECT * FROM classes";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String pplClass = rs.getString("class_name");
                pupilClassItmEdit.addItem(pplClass);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL TRANSACTIONS COMBO BOX
    private void FillTransCombo() {
        try {
            String sql = "SELECT * FROM accounts_categories";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String tCat = rs.getString("acat_name");
                acntCatName.addItem(tCat);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL UPDATE TRANSACTION COMBO BOX
     private void FillUpdateTransCombo() {
        try {
            String sql = "SELECT * FROM accounts_categories";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String tCat = rs.getString("acat_name");
                upacntCatName.addItem(tCat);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //FILLING THE PUPILS PAYEMENT COMBO WITH CLASSES
    public void fillPaymentCombo() {
        try {
            String sql = "SELECT * FROM classes";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                String cl = rs.getString("class_name");
                selClass.addItem(cl);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //SHOWING THE CHARTS OF ACCOUNTS CONTENTS
    public void chartsTable() {
        try {
            //String sql = "SELECT acat_name AS ACCOUNTS_NAMES, SUM(t_amount) AS TOTAL_AMOUNT FROM transactions GROUP BY acat_name ORDER BY SUM(t_amount) DESC";
            String sql = "SELECT acat_id AS ID, ACC_NAME AS ACCOUNTS, acat_name AS TYPE FROM accounts_categories ORDER BY acc_name";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            accntTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //FILLING PUPILS PAYMENTS TABLE WITH PAYMENTS DETAILS
    public void pupilsPayments() {
        try {
            String sql = "SELECT p_id, class_name, admn_no,amount, balance AS BALANCES,p_date FROM payments ORDER BY class_name ASC";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            pupilsPayments.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILLING THE CLASS PAYMENTS TABLE

    public void classPayments() {
        try {
            String sql = "SELECT class_name AS CLASS_NAME,SUM(amount) AS TOTAL_AMOUNT,SUM(balance) AS TOTAL_BALANCES FROM payments GROUP BY class_name ORDER BY class_name ASC";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            classPayments.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //SHOWING ALL THE TRANSACTIONS IN THE TRANSACTIONS SECTION
    public void transTable() {
        try {
            String sql = "SELECT t_id AS ID, acat_name AS ACCOUNT, t_name AS COMMODITY, t_amount AS AMOUNT, t_date AS DATE FROM transactions ORDER BY t_date DESC";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            transactionTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void totalTransactions() {
        try {
            String sql = "SELECT SUM (t_amount) AS TOTAL, MIN (t_amount) AS MINNIMUM, MAX (t_amount) AS MAXIMUM FROM transactions";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                String total = rs.getString("TOTAL");
                totalTran.setText("Kshs. " + total);
                String max = rs.getString("MAXIMUM");
                maxTran.setText("Kshs. " + max);
                String min = rs.getString("MINNIMUM");
                minTran.setText("Kshs. " + min);
            } else {
                JOptionPane.showMessageDialog(null, "Value not found");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        AddTransaction = new javax.swing.JDialog();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        acntCatName = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        acntDescription = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        tAmount = new javax.swing.JTextField();
        transSave = new javax.swing.JButton();
        btnTransCancel = new javax.swing.JButton();
        AddAccount = new javax.swing.JDialog();
        jPanel38 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        addAccntLbl = new javax.swing.JLabel();
        accntCategory = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        accntName = new javax.swing.JTextField();
        accntCancel = new javax.swing.JButton();
        accntSave = new javax.swing.JButton();
        UpdateAccounts = new javax.swing.JDialog();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        addAccntLbl1 = new javax.swing.JLabel();
        uaCombo = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        updtAccntName = new javax.swing.JTextField();
        updtAccntCancel = new javax.swing.JButton();
        accntSave1 = new javax.swing.JButton();
        UpdateTransaction = new javax.swing.JDialog();
        jPanel42 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        upacntCatName = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        upacntDescription = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        uptAmount = new javax.swing.JTextField();
        transSave1 = new javax.swing.JButton();
        btnTransCancel1 = new javax.swing.JButton();
        Payments = new javax.swing.JDialog();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        amount = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        savePayment = new javax.swing.JButton();
        cancelPayment = new javax.swing.JButton();
        admnNo = new javax.swing.JTextField();
        pupilClassItm = new javax.swing.JComboBox<>();
        AddClass = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        saveClass = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        className = new javax.swing.JTextField();
        classAmount = new javax.swing.JTextField();
        AddPupil = new javax.swing.JDialog();
        jPanel46 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        pupilClass = new javax.swing.JComboBox<>();
        adminNo = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        pupFName = new javax.swing.JTextField();
        pupLName = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        pupilSave = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel47 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        EditPayments = new javax.swing.JDialog();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        pupilClassItmEdit = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        amountEdit = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        savePayment1 = new javax.swing.JButton();
        cancelPayment1 = new javax.swing.JButton();
        payAdmnnoEdit = new javax.swing.JComboBox<>();
        AddSupplier = new javax.swing.JDialog();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        sFName = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        sLName = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        sEmail = new javax.swing.JTextField();
        sPhone = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        sProduct = new javax.swing.JTextField();
        supBtnSave = new javax.swing.JButton();
        btnSupplier = new javax.swing.JButton();
        EditSupplier = new javax.swing.JDialog();
        jPanel53 = new javax.swing.JPanel();
        jPanel54 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        esFName = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        esLName = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        esEmail = new javax.swing.JTextField();
        esPhone = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        esProduct = new javax.swing.JTextField();
        editsupBtnSave = new javax.swing.JButton();
        editbtnSupplierclose = new javax.swing.JButton();
        AddSupplies = new javax.swing.JDialog();
        jPanel55 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        suplyProduct = new javax.swing.JComboBox<>();
        supplyAmount = new javax.swing.JLabel();
        suplyAmount = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        EditSupplies = new javax.swing.JDialog();
        jPanel58 = new javax.swing.JPanel();
        jPanel59 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        editsuplyProduct = new javax.swing.JComboBox<>();
        supplyAmount1 = new javax.swing.JLabel();
        editsuplyAmount = new javax.swing.JTextField();
        saveEditSupply = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        AddEmployee = new javax.swing.JDialog();
        jPanel60 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        profName = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        departNme = new javax.swing.JComboBox<>();
        jLabel73 = new javax.swing.JLabel();
        empFName = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        empLName = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        idNo = new javax.swing.JTextField();
        phoneNo = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        birthDate = new com.toedter.calendar.JDateChooser();
        appointDate = new com.toedter.calendar.JDateChooser();
        jLabel79 = new javax.swing.JLabel();
        empNo = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        salary = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        bonus = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        allowance = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        deduction = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        postalCode = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        homeAddress = new javax.swing.JTextField();
        employeeSave = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        EditEmployee = new javax.swing.JDialog();
        jPanel62 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        profName1 = new javax.swing.JComboBox<>();
        jLabel89 = new javax.swing.JLabel();
        departNme1 = new javax.swing.JComboBox<>();
        jLabel90 = new javax.swing.JLabel();
        empFName1 = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        empLName1 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        idNo1 = new javax.swing.JTextField();
        phoneNo1 = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        birthDate1 = new com.toedter.calendar.JDateChooser();
        appointDate1 = new com.toedter.calendar.JDateChooser();
        jLabel96 = new javax.swing.JLabel();
        empNo1 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        salary1 = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        bonus1 = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        allowance1 = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        email1 = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        deduction1 = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        postalCode1 = new javax.swing.JTextField();
        jLabel103 = new javax.swing.JLabel();
        homeAddress1 = new javax.swing.JTextField();
        employeeSave1 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        AddSalary = new javax.swing.JDialog();
        jPanel64 = new javax.swing.JPanel();
        jPanel65 = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        salEmbNo = new javax.swing.JComboBox<>();
        salaryPayment = new javax.swing.JButton();
        salaryCancel = new javax.swing.JButton();
        AddPeriod = new javax.swing.JDialog();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        startDate = new com.toedter.calendar.JDateChooser();
        jLabel108 = new javax.swing.JLabel();
        endDate = new com.toedter.calendar.JDateChooser();
        addBudgetSave = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        AddBudget = new javax.swing.JDialog();
        jPanel69 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        bstartPeriod = new javax.swing.JComboBox<>();
        jLabel111 = new javax.swing.JLabel();
        adAcntCombo = new javax.swing.JComboBox<>();
        jLabel112 = new javax.swing.JLabel();
        adbgtCatCompo = new javax.swing.JComboBox<>();
        jLabel113 = new javax.swing.JLabel();
        bgtAmnt = new javax.swing.JTextField();
        saveBgtBtn = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        EditBudget = new javax.swing.JDialog();
        jPanel71 = new javax.swing.JPanel();
        jPanel72 = new javax.swing.JPanel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        bstartPeriod1 = new javax.swing.JComboBox<>();
        jLabel116 = new javax.swing.JLabel();
        adAcntCombo1 = new javax.swing.JComboBox<>();
        jLabel117 = new javax.swing.JLabel();
        adbgtCatCompo1 = new javax.swing.JComboBox<>();
        jLabel118 = new javax.swing.JLabel();
        bgtAmnt1 = new javax.swing.JTextField();
        saveBgtBtn1 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        addAccount = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        addAdmin = new javax.swing.JButton();
        addClassroom = new javax.swing.JButton();
        addPupil = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        addEmployee = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        sideMenu = new javax.swing.JPanel();
        lblChartsOfAccounts = new javax.swing.JLabel();
        lblClassLists = new javax.swing.JLabel();
        employeesPanelLabel = new javax.swing.JLabel();
        suppliersLabelPanel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        mainLayer = new javax.swing.JPanel();
        logo = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        accountCharts = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        accntTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        accountsEdit = new javax.swing.JButton();
        dltAccount = new javax.swing.JButton();
        searchAccounts = new javax.swing.JTextField();
        prntAccounts = new javax.swing.JButton();
        chartsofAccountspdfButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        transactionTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        transactionsDelete = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        transactionsReport = new javax.swing.JButton();
        seachTransactions = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        totalTran = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        maxTran = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        minTran = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        classLists = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        classPayments = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        printClassPayments = new javax.swing.JButton();
        printClassLists = new javax.swing.JButton();
        totalFees = new javax.swing.JLabel();
        totalBalance = new javax.swing.JLabel();
        pcntBalance = new javax.swing.JLabel();
        pcntPaid = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        pupilsPayments = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        selClass = new javax.swing.JComboBox<>();
        jPanel28 = new javax.swing.JPanel();
        pupilsPaymentEdit = new javax.swing.JButton();
        searchPupilsPayments = new javax.swing.JTextField();
        pupilsPayDelete = new javax.swing.JButton();
        pupilsPayPrint = new javax.swing.JButton();
        paymentsReport = new javax.swing.JButton();
        employees = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        employeeEdit = new javax.swing.JButton();
        employeeDelete = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        employeesReport = new javax.swing.JButton();
        searchEmployees = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        salaryTable = new javax.swing.JTable();
        jPanel66 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        searchSalaries = new javax.swing.JTextField();
        jButton24 = new javax.swing.JButton();
        printSalaries = new javax.swing.JButton();
        printSalaries1 = new javax.swing.JButton();
        reports = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        suppliers = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        suppliersTable = new javax.swing.JTable();
        jPanel52 = new javax.swing.JPanel();
        searchSuppliers = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        supplierDelete = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        suppliesTable = new javax.swing.JTable();
        jPanel57 = new javax.swing.JPanel();
        suppliesEdit = new javax.swing.JButton();
        suppliesBtnDelete = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        suppliesReport = new javax.swing.JButton();
        budget = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        budgetTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        btsttPanCombo = new javax.swing.JComboBox<>();
        to = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        addPeriod = new javax.swing.JButton();
        addBudgetBtn = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jMenuItem3.setText("jMenuItem3");

        jMenu2.setText("jMenu2");

        AddTransaction.setMinimumSize(new java.awt.Dimension(401, 202));
        AddTransaction.setModal(true);
        AddTransaction.setUndecorated(true);
        AddTransaction.setResizable(false);

        jPanel36.setBackground(new java.awt.Color(44, 62, 80));

        jPanel37.setBackground(new java.awt.Color(0, 0, 139));

        jLabel27.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 215, 0));
        jLabel27.setText("Add Transactions");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27)
        );

        jLabel28.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Account type:");

        jLabel29.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Description:");

        jLabel30.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Amount:");

        transSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        transSave.setForeground(new java.awt.Color(139, 0, 0));
        transSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        transSave.setText("Save");
        transSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transSaveActionPerformed(evt);
            }
        });

        btnTransCancel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnTransCancel.setForeground(new java.awt.Color(139, 0, 0));
        btnTransCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        btnTransCancel.setText("Cancel");
        btnTransCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(acntCatName, 0, 213, Short.MAX_VALUE)
                            .addComponent(acntDescription)
                            .addComponent(tAmount)))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(transSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTransCancel)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(acntCatName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(acntDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(tAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transSave)
                    .addComponent(btnTransCancel))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddTransactionLayout = new javax.swing.GroupLayout(AddTransaction.getContentPane());
        AddTransaction.getContentPane().setLayout(AddTransactionLayout);
        AddTransactionLayout.setHorizontalGroup(
            AddTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddTransactionLayout.setVerticalGroup(
            AddTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        AddAccount.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        AddAccount.setMinimumSize(new java.awt.Dimension(442, 140));
        AddAccount.setModal(true);
        AddAccount.setUndecorated(true);
        AddAccount.setResizable(false);

        jPanel38.setBackground(new java.awt.Color(44, 62, 80));

        jPanel39.setBackground(new java.awt.Color(0, 0, 139));

        addAccntLbl.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        addAccntLbl.setForeground(new java.awt.Color(255, 215, 0));
        addAccntLbl.setText("Add Account");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addAccntLbl)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addAccntLbl)
        );

        jLabel32.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Account category:");

        jLabel33.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Account name:");

        accntCancel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        accntCancel.setForeground(new java.awt.Color(139, 0, 0));
        accntCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        accntCancel.setText("Cancel");
        accntCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accntCancelActionPerformed(evt);
            }
        });

        accntSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        accntSave.setForeground(new java.awt.Color(139, 0, 0));
        accntSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        accntSave.setText("Save");
        accntSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accntSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(accntName, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(accntSave)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accntCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accntCancel))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(accntCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(accntName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accntSave)
                    .addComponent(accntCancel)))
        );

        javax.swing.GroupLayout AddAccountLayout = new javax.swing.GroupLayout(AddAccount.getContentPane());
        AddAccount.getContentPane().setLayout(AddAccountLayout);
        AddAccountLayout.setHorizontalGroup(
            AddAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddAccountLayout.setVerticalGroup(
            AddAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        UpdateAccounts.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        UpdateAccounts.setMinimumSize(new java.awt.Dimension(442, 140));
        UpdateAccounts.setModal(true);
        UpdateAccounts.setUndecorated(true);
        UpdateAccounts.setResizable(false);

        jPanel40.setBackground(new java.awt.Color(44, 62, 80));

        jPanel41.setBackground(new java.awt.Color(0, 0, 139));

        addAccntLbl1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        addAccntLbl1.setForeground(new java.awt.Color(255, 215, 0));
        addAccntLbl1.setText("Update Accounts");

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addAccntLbl1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addAccntLbl1)
        );

        jLabel34.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Account category:");

        jLabel35.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Account name:");

        updtAccntCancel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        updtAccntCancel.setForeground(new java.awt.Color(139, 0, 0));
        updtAccntCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        updtAccntCancel.setText("Cancel");
        updtAccntCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updtAccntCancelActionPerformed(evt);
            }
        });

        accntSave1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        accntSave1.setForeground(new java.awt.Color(139, 0, 0));
        accntSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        accntSave1.setText("Save");
        accntSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accntSave1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updtAccntName, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(accntSave1)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(uaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updtAccntCancel))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(uaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(updtAccntName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accntSave1)
                    .addComponent(updtAccntCancel)))
        );

        javax.swing.GroupLayout UpdateAccountsLayout = new javax.swing.GroupLayout(UpdateAccounts.getContentPane());
        UpdateAccounts.getContentPane().setLayout(UpdateAccountsLayout);
        UpdateAccountsLayout.setHorizontalGroup(
            UpdateAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        UpdateAccountsLayout.setVerticalGroup(
            UpdateAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        UpdateTransaction.setMinimumSize(new java.awt.Dimension(401, 202));
        UpdateTransaction.setModal(true);
        UpdateTransaction.setUndecorated(true);
        UpdateTransaction.setResizable(false);

        jPanel42.setBackground(new java.awt.Color(44, 62, 80));

        jPanel43.setBackground(new java.awt.Color(0, 0, 139));

        jLabel31.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 215, 0));
        jLabel31.setText("Update Transactions");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel31)
        );

        jLabel36.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Account type:");

        jLabel37.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Description:");

        jLabel38.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Amount:");

        transSave1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        transSave1.setForeground(new java.awt.Color(139, 0, 0));
        transSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        transSave1.setText("Save");
        transSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transSave1ActionPerformed(evt);
            }
        });

        btnTransCancel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnTransCancel1.setForeground(new java.awt.Color(139, 0, 0));
        btnTransCancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        btnTransCancel1.setText("Cancel");
        btnTransCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransCancel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(upacntCatName, 0, 213, Short.MAX_VALUE)
                            .addComponent(upacntDescription)
                            .addComponent(uptAmount)))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(transSave1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTransCancel1)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(upacntCatName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(upacntDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(uptAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transSave1)
                    .addComponent(btnTransCancel1))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UpdateTransactionLayout = new javax.swing.GroupLayout(UpdateTransaction.getContentPane());
        UpdateTransaction.getContentPane().setLayout(UpdateTransactionLayout);
        UpdateTransactionLayout.setHorizontalGroup(
            UpdateTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        UpdateTransactionLayout.setVerticalGroup(
            UpdateTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Payments.setMinimumSize(new java.awt.Dimension(402, 192));
        Payments.setModal(true);
        Payments.setUndecorated(true);
        Payments.setResizable(false);

        jPanel44.setBackground(new java.awt.Color(44, 62, 80));

        jPanel45.setBackground(new java.awt.Color(0, 0, 139));

        jLabel39.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 215, 0));
        jLabel39.setText("Make Payments");

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addComponent(jLabel39)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel39)
        );

        jLabel40.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Class Name:");

        jLabel41.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Admin No:");

        jLabel42.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Amount:");

        savePayment.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        savePayment.setForeground(new java.awt.Color(139, 0, 0));
        savePayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        savePayment.setText("Save");
        savePayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePaymentActionPerformed(evt);
            }
        });

        cancelPayment.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        cancelPayment.setForeground(new java.awt.Color(139, 0, 0));
        cancelPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        cancelPayment.setText("Cancel");
        cancelPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelPaymentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(savePayment)
                            .addComponent(jLabel40))
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(cancelPayment)
                                .addGap(0, 176, Short.MAX_VALUE))
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(pupilClassItm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel41))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(amount, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                            .addComponent(admnNo))))
                .addContainerGap())
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(pupilClassItm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(admnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savePayment)
                    .addComponent(cancelPayment)))
        );

        javax.swing.GroupLayout PaymentsLayout = new javax.swing.GroupLayout(Payments.getContentPane());
        Payments.getContentPane().setLayout(PaymentsLayout);
        PaymentsLayout.setHorizontalGroup(
            PaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PaymentsLayout.setVerticalGroup(
            PaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        AddClass.setMinimumSize(new java.awt.Dimension(400, 151));
        AddClass.setModal(true);
        AddClass.setUndecorated(true);
        AddClass.setResizable(false);

        jPanel12.setBackground(new java.awt.Color(44, 62, 80));

        jLabel25.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Class Amount:");

        jPanel25.setBackground(new java.awt.Color(0, 0, 139));

        jLabel43.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 215, 0));
        jLabel43.setText("Add Class");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel43)
        );

        jLabel44.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Class Name:");

        saveClass.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        saveClass.setForeground(new java.awt.Color(139, 0, 0));
        saveClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        saveClass.setText("Save");
        saveClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveClassActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton4.setForeground(new java.awt.Color(139, 0, 0));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(51, 51, 51)
                        .addComponent(className, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(saveClass)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(classAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(className, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4)
                    .addComponent(saveClass))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddClassLayout = new javax.swing.GroupLayout(AddClass.getContentPane());
        AddClass.getContentPane().setLayout(AddClassLayout);
        AddClassLayout.setHorizontalGroup(
            AddClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddClassLayout.setVerticalGroup(
            AddClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddClassLayout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        AddPupil.setMinimumSize(new java.awt.Dimension(384, 223));
        AddPupil.setModal(true);
        AddPupil.setUndecorated(true);

        jPanel46.setBackground(new java.awt.Color(44, 62, 80));

        jLabel45.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Select class:");

        jLabel46.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Admin No:");

        jLabel47.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("First Name:");

        jLabel48.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Last Name:");

        pupilSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        pupilSave.setForeground(new java.awt.Color(139, 0, 0));
        pupilSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        pupilSave.setText("Save");
        pupilSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupilSaveActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton5.setForeground(new java.awt.Color(139, 0, 0));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton5.setText("Cancel");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel47.setBackground(new java.awt.Color(0, 0, 139));

        jLabel49.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 215, 0));
        jLabel49.setText("Add Pupil");

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel49)
        );

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47)
                            .addComponent(jLabel48))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pupilClass, 0, 236, Short.MAX_VALUE)
                            .addComponent(adminNo)
                            .addComponent(pupFName)
                            .addComponent(pupLName)))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(pupilSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(pupilClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(adminNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pupFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pupLName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pupilSave)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddPupilLayout = new javax.swing.GroupLayout(AddPupil.getContentPane());
        AddPupil.getContentPane().setLayout(AddPupilLayout);
        AddPupilLayout.setHorizontalGroup(
            AddPupilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AddPupilLayout.setVerticalGroup(
            AddPupilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddPupilLayout.createSequentialGroup()
                .addComponent(jPanel46, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        EditPayments.setMinimumSize(new java.awt.Dimension(402, 192));
        EditPayments.setModal(true);
        EditPayments.setUndecorated(true);
        EditPayments.setResizable(false);

        jPanel48.setBackground(new java.awt.Color(44, 62, 80));

        jPanel49.setBackground(new java.awt.Color(0, 0, 139));

        jLabel50.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 215, 0));
        jLabel50.setText("Edit Payments");

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addComponent(jLabel50)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel50)
        );

        pupilClassItmEdit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pupilClassItmEditItemStateChanged(evt);
            }
        });
        pupilClassItmEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupilClassItmEditActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Class Name:");

        jLabel52.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Admin No:");

        jLabel53.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Amount:");

        savePayment1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        savePayment1.setForeground(new java.awt.Color(139, 0, 0));
        savePayment1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        savePayment1.setText("Save");
        savePayment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePayment1ActionPerformed(evt);
            }
        });

        cancelPayment1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        cancelPayment1.setForeground(new java.awt.Color(139, 0, 0));
        cancelPayment1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        cancelPayment1.setText("Cancel");
        cancelPayment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelPayment1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(savePayment1)
                            .addComponent(jLabel51))
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addComponent(pupilClassItmEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(cancelPayment1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel53)
                            .addComponent(jLabel52))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(amountEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                            .addComponent(payAdmnnoEdit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(pupilClassItmEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(payAdmnnoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(amountEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savePayment1)
                    .addComponent(cancelPayment1)))
        );

        javax.swing.GroupLayout EditPaymentsLayout = new javax.swing.GroupLayout(EditPayments.getContentPane());
        EditPayments.getContentPane().setLayout(EditPaymentsLayout);
        EditPaymentsLayout.setHorizontalGroup(
            EditPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        EditPaymentsLayout.setVerticalGroup(
            EditPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        AddSupplier.setMinimumSize(new java.awt.Dimension(385, 251));
        AddSupplier.setModal(true);
        AddSupplier.setUndecorated(true);
        AddSupplier.setResizable(false);

        jPanel50.setBackground(new java.awt.Color(44, 62, 80));

        jPanel51.setBackground(new java.awt.Color(0, 0, 139));

        jLabel54.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 215, 0));
        jLabel54.setText("Add Supplier");

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel54)
        );

        jLabel55.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("First Name:");

        jLabel56.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Last Name:");

        jLabel57.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Email:");

        jLabel58.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Phone no:");

        jLabel59.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Product:");

        supBtnSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        supBtnSave.setForeground(new java.awt.Color(139, 0, 0));
        supBtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        supBtnSave.setText("Save");
        supBtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supBtnSaveActionPerformed(evt);
            }
        });

        btnSupplier.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnSupplier.setForeground(new java.awt.Color(139, 0, 0));
        btnSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        btnSupplier.setText("Cancel");
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jLabel55)
                                .addGap(18, 18, 18)
                                .addComponent(sFName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel59)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel58)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel50Layout.createSequentialGroup()
                                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel56)
                                        .addComponent(jLabel57))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(sLName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(supBtnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSupplier)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(sFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(sLName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(sEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(sPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(sProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supBtnSave)
                    .addComponent(btnSupplier))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddSupplierLayout = new javax.swing.GroupLayout(AddSupplier.getContentPane());
        AddSupplier.getContentPane().setLayout(AddSupplierLayout);
        AddSupplierLayout.setHorizontalGroup(
            AddSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AddSupplierLayout.setVerticalGroup(
            AddSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddSupplierLayout.createSequentialGroup()
                .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        EditSupplier.setMinimumSize(new java.awt.Dimension(385, 251));
        EditSupplier.setModal(true);
        EditSupplier.setUndecorated(true);
        EditSupplier.setResizable(false);

        jPanel53.setBackground(new java.awt.Color(44, 62, 80));

        jPanel54.setBackground(new java.awt.Color(0, 0, 139));

        jLabel60.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 215, 0));
        jLabel60.setText("Edit Supplier");

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel60)
        );

        jLabel61.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("First Name:");

        jLabel62.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Last Name:");

        jLabel63.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Email:");

        jLabel64.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Phone no:");

        jLabel65.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Product:");

        editsupBtnSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        editsupBtnSave.setForeground(new java.awt.Color(139, 0, 0));
        editsupBtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        editsupBtnSave.setText("Save");
        editsupBtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editsupBtnSaveActionPerformed(evt);
            }
        });

        editbtnSupplierclose.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        editbtnSupplierclose.setForeground(new java.awt.Color(139, 0, 0));
        editbtnSupplierclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        editbtnSupplierclose.setText("Cancel");
        editbtnSupplierclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editbtnSuppliercloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel53Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel53Layout.createSequentialGroup()
                                .addComponent(jLabel61)
                                .addGap(18, 18, 18)
                                .addComponent(esFName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel53Layout.createSequentialGroup()
                                    .addComponent(jLabel65)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(esProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel53Layout.createSequentialGroup()
                                    .addComponent(jLabel64)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(esPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel53Layout.createSequentialGroup()
                                    .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel62)
                                        .addComponent(jLabel63))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(esLName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(esEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel53Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(editsupBtnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editbtnSupplierclose)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addComponent(jPanel54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(esFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(esLName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(esEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(esPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(esProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editsupBtnSave)
                    .addComponent(editbtnSupplierclose))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EditSupplierLayout = new javax.swing.GroupLayout(EditSupplier.getContentPane());
        EditSupplier.getContentPane().setLayout(EditSupplierLayout);
        EditSupplierLayout.setHorizontalGroup(
            EditSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        EditSupplierLayout.setVerticalGroup(
            EditSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditSupplierLayout.createSequentialGroup()
                .addComponent(jPanel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        AddSupplies.setMinimumSize(new java.awt.Dimension(380, 152));
        AddSupplies.setModal(true);
        AddSupplies.setUndecorated(true);
        AddSupplies.setResizable(false);

        jPanel55.setBackground(new java.awt.Color(44, 62, 80));

        jPanel56.setBackground(new java.awt.Color(0, 0, 139));

        jLabel66.setBackground(new java.awt.Color(255, 215, 0));
        jLabel66.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 215, 0));
        jLabel66.setText("Add Supplies");

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel66)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel66)
        );

        jLabel67.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Select product:");

        supplyAmount.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        supplyAmount.setForeground(new java.awt.Color(255, 255, 255));
        supplyAmount.setText("Amount:");

        jButton20.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton20.setForeground(new java.awt.Color(139, 0, 0));
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        jButton20.setText("Save");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton21.setForeground(new java.awt.Color(139, 0, 0));
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton21.setText("Cancel");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton20)
                    .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel67)
                        .addComponent(supplyAmount)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(suplyAmount)
                        .addComponent(suplyProduct, 0, 212, Short.MAX_VALUE))
                    .addComponent(jButton21))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(suplyProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplyAmount)
                    .addComponent(suplyAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20)
                    .addComponent(jButton21))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddSuppliesLayout = new javax.swing.GroupLayout(AddSupplies.getContentPane());
        AddSupplies.getContentPane().setLayout(AddSuppliesLayout);
        AddSuppliesLayout.setHorizontalGroup(
            AddSuppliesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AddSuppliesLayout.setVerticalGroup(
            AddSuppliesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        EditSupplies.setMinimumSize(new java.awt.Dimension(380, 152));
        EditSupplies.setModal(true);
        EditSupplies.setUndecorated(true);
        EditSupplies.setResizable(false);

        jPanel58.setBackground(new java.awt.Color(44, 62, 80));

        jPanel59.setBackground(new java.awt.Color(0, 0, 139));

        jLabel68.setBackground(new java.awt.Color(255, 215, 0));
        jLabel68.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 215, 0));
        jLabel68.setText("Edit Supplies");

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel68)
        );

        jLabel69.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Select product:");

        supplyAmount1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        supplyAmount1.setForeground(new java.awt.Color(255, 255, 255));
        supplyAmount1.setText("Amount:");

        saveEditSupply.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        saveEditSupply.setForeground(new java.awt.Color(139, 0, 0));
        saveEditSupply.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        saveEditSupply.setText("Save");
        saveEditSupply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveEditSupplyActionPerformed(evt);
            }
        });

        jButton29.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton29.setForeground(new java.awt.Color(139, 0, 0));
        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton29.setText("Cancel");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saveEditSupply)
                    .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel69)
                        .addComponent(supplyAmount1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(editsuplyAmount)
                        .addComponent(editsuplyProduct, 0, 212, Short.MAX_VALUE))
                    .addComponent(jButton29))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addComponent(jPanel59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(editsuplyProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplyAmount1)
                    .addComponent(editsuplyAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveEditSupply)
                    .addComponent(jButton29))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EditSuppliesLayout = new javax.swing.GroupLayout(EditSupplies.getContentPane());
        EditSupplies.getContentPane().setLayout(EditSuppliesLayout);
        EditSuppliesLayout.setHorizontalGroup(
            EditSuppliesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        EditSuppliesLayout.setVerticalGroup(
            EditSuppliesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        AddEmployee.setMinimumSize(new java.awt.Dimension(880, 475));
        AddEmployee.setModal(true);
        AddEmployee.setUndecorated(true);
        AddEmployee.setResizable(false);

        jPanel60.setBackground(new java.awt.Color(44, 62, 80));
        jPanel60.setForeground(new java.awt.Color(44, 62, 80));

        jPanel61.setBackground(new java.awt.Color(0, 0, 139));

        jLabel70.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 215, 0));
        jLabel70.setText("Add Employee");

        javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
        jPanel61.setLayout(jPanel61Layout);
        jPanel61Layout.setHorizontalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel61Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel70)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel61Layout.setVerticalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel70)
        );

        jLabel71.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Designation:");

        jLabel72.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Department Name:");

        jLabel73.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("First Name:");

        jLabel74.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Last Name:");

        jLabel75.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("ID NO:");

        jLabel76.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Phone No:");

        jLabel77.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Date of Birth:");

        jLabel78.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Date of Appointment:");

        birthDate.setDateFormatString("yyyy-MM-d");

        appointDate.setDateFormatString("yyyy-MM-d");

        jLabel79.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Employee no:");

        jLabel80.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Basic salary:");

        salary.setText("0.00");

        jLabel81.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Bonusses:");

        bonus.setText("0.00");

        jLabel82.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("Allowances:");

        allowance.setText("0.00");

        jLabel83.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("Email:");

        jLabel84.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("Deductions:");

        deduction.setText("0.00");

        jLabel85.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("Postal Code:");

        jLabel86.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("Home address:");

        employeeSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        employeeSave.setForeground(new java.awt.Color(139, 0, 0));
        employeeSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        employeeSave.setText("Save");
        employeeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeSaveActionPerformed(evt);
            }
        });

        jButton26.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton26.setForeground(new java.awt.Color(139, 0, 0));
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton26.setText("Cancel");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel60Layout = new javax.swing.GroupLayout(jPanel60);
        jPanel60.setLayout(jPanel60Layout);
        jPanel60Layout.setHorizontalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel60Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addComponent(jLabel71)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                .addComponent(profName, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel60Layout.createSequentialGroup()
                                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel72)
                                            .addComponent(jLabel73)
                                            .addComponent(jLabel74)
                                            .addComponent(jLabel75)
                                            .addComponent(jLabel76)
                                            .addComponent(jLabel77))
                                        .addGap(35, 35, 35))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel60Layout.createSequentialGroup()
                                        .addComponent(jLabel78)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(departNme, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(empFName)
                                    .addComponent(empLName)
                                    .addComponent(idNo)
                                    .addComponent(phoneNo)
                                    .addComponent(birthDate, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                    .addComponent(appointDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addComponent(jLabel86)
                                .addGap(18, 18, 18)
                                .addComponent(homeAddress))
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel79)
                                    .addComponent(jLabel80)
                                    .addComponent(jLabel81)
                                    .addComponent(jLabel82)
                                    .addComponent(jLabel83)
                                    .addComponent(jLabel84))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(allowance, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                                    .addComponent(bonus, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(salary, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(email, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(empNo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(deduction)))
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addComponent(jLabel85)
                                .addGap(37, 37, 37)
                                .addComponent(postalCode))))
                    .addGroup(jPanel60Layout.createSequentialGroup()
                        .addGap(389, 389, 389)
                        .addComponent(employeeSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton26)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel60Layout.setVerticalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addComponent(jPanel61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel60Layout.createSequentialGroup()
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel71)
                            .addComponent(profName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(departNme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(empFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel74)
                            .addComponent(empLName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel75)
                            .addComponent(idNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addComponent(phoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel60Layout.createSequentialGroup()
                                .addComponent(jLabel76)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel77)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel78)
                            .addComponent(appointDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel60Layout.createSequentialGroup()
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel79)
                            .addComponent(empNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel83)
                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel80)
                            .addComponent(salary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel81)
                            .addComponent(bonus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel82)
                            .addComponent(allowance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel84)
                            .addComponent(deduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel85)
                            .addComponent(postalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel86)
                            .addComponent(homeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton26)
                    .addComponent(employeeSave)))
        );

        javax.swing.GroupLayout AddEmployeeLayout = new javax.swing.GroupLayout(AddEmployee.getContentPane());
        AddEmployee.getContentPane().setLayout(AddEmployeeLayout);
        AddEmployeeLayout.setHorizontalGroup(
            AddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddEmployeeLayout.setVerticalGroup(
            AddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        EditEmployee.setMinimumSize(new java.awt.Dimension(880, 475));
        EditEmployee.setModal(true);
        EditEmployee.setUndecorated(true);
        EditEmployee.setResizable(false);

        jPanel62.setBackground(new java.awt.Color(44, 62, 80));
        jPanel62.setForeground(new java.awt.Color(44, 62, 80));

        jPanel63.setBackground(new java.awt.Color(0, 0, 139));

        jLabel87.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(255, 215, 0));
        jLabel87.setText("Edit Employee");

        javax.swing.GroupLayout jPanel63Layout = new javax.swing.GroupLayout(jPanel63);
        jPanel63.setLayout(jPanel63Layout);
        jPanel63Layout.setHorizontalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel63Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel87)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel63Layout.setVerticalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel87)
        );

        jLabel88.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(255, 255, 255));
        jLabel88.setText("Designation:");

        jLabel89.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setText("Department Name:");

        jLabel90.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(255, 255, 255));
        jLabel90.setText("First Name:");

        jLabel91.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setText("Last Name:");

        jLabel92.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(255, 255, 255));
        jLabel92.setText("ID NO:");

        jLabel93.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("Phone No:");

        jLabel94.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(255, 255, 255));
        jLabel94.setText("Date of Birth:");

        jLabel95.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 255, 255));
        jLabel95.setText("Date of Appointment:");

        birthDate1.setDateFormatString("yyyy-MM-d");

        appointDate1.setDateFormatString("yyyy-MM-d");

        jLabel96.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("Employee no:");

        jLabel97.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("Basic salary:");

        salary1.setText("0.00");

        jLabel98.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("Bonusses:");

        bonus1.setText("0.00");

        jLabel99.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("Allowances:");

        allowance1.setText("0.00");

        jLabel100.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 255, 255));
        jLabel100.setText("Email:");

        jLabel101.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setText("Deductions:");

        deduction1.setText("0.00");

        jLabel102.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(255, 255, 255));
        jLabel102.setText("Postal Code:");

        jLabel103.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(255, 255, 255));
        jLabel103.setText("Home address:");

        employeeSave1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        employeeSave1.setForeground(new java.awt.Color(139, 0, 0));
        employeeSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        employeeSave1.setText("Save");
        employeeSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeSave1ActionPerformed(evt);
            }
        });

        jButton30.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton30.setForeground(new java.awt.Color(139, 0, 0));
        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton30.setText("Cancel");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel62Layout = new javax.swing.GroupLayout(jPanel62);
        jPanel62.setLayout(jPanel62Layout);
        jPanel62Layout.setHorizontalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel62Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel62Layout.createSequentialGroup()
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                        .addComponent(profName1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel62Layout.createSequentialGroup()
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel62Layout.createSequentialGroup()
                                .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel89)
                                    .addComponent(jLabel90)
                                    .addComponent(jLabel91)
                                    .addComponent(jLabel92)
                                    .addComponent(jLabel93)
                                    .addComponent(jLabel94))
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel62Layout.createSequentialGroup()
                                .addComponent(jLabel95)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(departNme1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(empFName1)
                            .addComponent(empLName1)
                            .addComponent(idNo1)
                            .addComponent(phoneNo1)
                            .addComponent(birthDate1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(appointDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel62Layout.createSequentialGroup()
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel96)
                            .addComponent(jLabel97)
                            .addComponent(jLabel98)
                            .addComponent(jLabel99)
                            .addComponent(jLabel100)
                            .addComponent(jLabel101))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(allowance1)
                            .addComponent(deduction1)
                            .addComponent(bonus1)
                            .addComponent(salary1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(email1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel62Layout.createSequentialGroup()
                                .addComponent(empNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 12, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel62Layout.createSequentialGroup()
                        .addComponent(jLabel103)
                        .addGap(18, 18, 18)
                        .addComponent(homeAddress1))
                    .addGroup(jPanel62Layout.createSequentialGroup()
                        .addComponent(jLabel102)
                        .addGap(37, 37, 37)
                        .addComponent(postalCode1)))
                .addContainerGap())
            .addGroup(jPanel62Layout.createSequentialGroup()
                .addGap(389, 389, 389)
                .addComponent(employeeSave1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton30)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel62Layout.setVerticalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel62Layout.createSequentialGroup()
                .addComponent(jPanel63, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel62Layout.createSequentialGroup()
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel88)
                            .addComponent(profName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel89)
                            .addComponent(departNme1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel90)
                            .addComponent(empFName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel91)
                            .addComponent(empLName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel92)
                            .addComponent(idNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel62Layout.createSequentialGroup()
                                .addComponent(phoneNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel62Layout.createSequentialGroup()
                                .addComponent(jLabel93)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel94)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel95)
                            .addComponent(appointDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel62Layout.createSequentialGroup()
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel96)
                            .addComponent(empNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel100)
                            .addComponent(email1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel97)
                            .addComponent(salary1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel98)
                            .addComponent(bonus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel99)
                            .addComponent(allowance1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel101)
                            .addComponent(deduction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel102)
                            .addComponent(postalCode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel103)
                            .addComponent(homeAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton30)
                    .addComponent(employeeSave1))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout EditEmployeeLayout = new javax.swing.GroupLayout(EditEmployee.getContentPane());
        EditEmployee.getContentPane().setLayout(EditEmployeeLayout);
        EditEmployeeLayout.setHorizontalGroup(
            EditEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        EditEmployeeLayout.setVerticalGroup(
            EditEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        AddSalary.setMinimumSize(new java.awt.Dimension(416, 119));
        AddSalary.setModal(true);
        AddSalary.setUndecorated(true);
        AddSalary.setResizable(false);

        jPanel64.setBackground(new java.awt.Color(44, 62, 80));

        jPanel65.setBackground(new java.awt.Color(0, 0, 139));

        jLabel104.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(255, 215, 0));
        jLabel104.setText("Salary Payment");

        javax.swing.GroupLayout jPanel65Layout = new javax.swing.GroupLayout(jPanel65);
        jPanel65.setLayout(jPanel65Layout);
        jPanel65Layout.setHorizontalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel65Layout.createSequentialGroup()
                .addComponent(jLabel104)
                .addGap(0, 343, Short.MAX_VALUE))
        );
        jPanel65Layout.setVerticalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel104)
        );

        jLabel105.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(255, 255, 255));
        jLabel105.setText("Select employee no:");

        salaryPayment.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        salaryPayment.setForeground(new java.awt.Color(139, 0, 0));
        salaryPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        salaryPayment.setText("Save payment");
        salaryPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salaryPaymentActionPerformed(evt);
            }
        });

        salaryCancel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        salaryCancel.setForeground(new java.awt.Color(139, 0, 0));
        salaryCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        salaryCancel.setText("Cancel");
        salaryCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salaryCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel64Layout.createSequentialGroup()
                .addComponent(jPanel65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel64Layout.createSequentialGroup()
                .addGroup(jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel64Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel105)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salEmbNo, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel64Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(salaryPayment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salaryCancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel64Layout.createSequentialGroup()
                .addComponent(jPanel65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(salEmbNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salaryPayment)
                    .addComponent(salaryCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddSalaryLayout = new javax.swing.GroupLayout(AddSalary.getContentPane());
        AddSalary.getContentPane().setLayout(AddSalaryLayout);
        AddSalaryLayout.setHorizontalGroup(
            AddSalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel64, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AddSalaryLayout.setVerticalGroup(
            AddSalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddSalaryLayout.createSequentialGroup()
                .addComponent(jPanel64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        AddPeriod.setMinimumSize(new java.awt.Dimension(373, 165));
        AddPeriod.setModal(true);
        AddPeriod.setUndecorated(true);
        AddPeriod.setResizable(false);

        jPanel67.setBackground(new java.awt.Color(44, 62, 80));

        jPanel68.setBackground(new java.awt.Color(0, 0, 139));
        jPanel68.setForeground(new java.awt.Color(0, 0, 139));

        jLabel106.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel106.setForeground(new java.awt.Color(255, 215, 0));
        jLabel106.setText("Add Budget Period");

        javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
        jPanel68.setLayout(jPanel68Layout);
        jPanel68Layout.setHorizontalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel68Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel106)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel68Layout.setVerticalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel106)
        );

        jLabel107.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(255, 255, 255));
        jLabel107.setText("Start period:");

        jLabel108.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(255, 255, 255));
        jLabel108.setText("End period:");

        addBudgetSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        addBudgetSave.setForeground(new java.awt.Color(139, 0, 0));
        addBudgetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        addBudgetSave.setText("Save");
        addBudgetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBudgetSaveActionPerformed(evt);
            }
        });

        jButton33.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton33.setForeground(new java.awt.Color(139, 0, 0));
        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton33.setText("Cancel");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel67Layout.createSequentialGroup()
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel67Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel107)
                            .addComponent(jLabel108))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(endDate, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(startDate, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel67Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(addBudgetSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton33)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel67Layout.createSequentialGroup()
                .addComponent(jPanel68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel107)
                    .addComponent(startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel108))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBudgetSave)
                    .addComponent(jButton33))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddPeriodLayout = new javax.swing.GroupLayout(AddPeriod.getContentPane());
        AddPeriod.getContentPane().setLayout(AddPeriodLayout);
        AddPeriodLayout.setHorizontalGroup(
            AddPeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AddPeriodLayout.setVerticalGroup(
            AddPeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        AddBudget.setMinimumSize(new java.awt.Dimension(513, 278));
        AddBudget.setModal(true);
        AddBudget.setUndecorated(true);
        AddBudget.setResizable(false);

        jPanel69.setBackground(new java.awt.Color(44, 62, 80));

        jPanel70.setBackground(new java.awt.Color(0, 0, 139));

        jLabel109.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(255, 215, 0));
        jLabel109.setText("Add Budget");

        javax.swing.GroupLayout jPanel70Layout = new javax.swing.GroupLayout(jPanel70);
        jPanel70.setLayout(jPanel70Layout);
        jPanel70Layout.setHorizontalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel70Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel109)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel70Layout.setVerticalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel109)
        );

        jLabel110.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(255, 255, 255));
        jLabel110.setText("Select start period:");

        jLabel111.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(255, 255, 255));
        jLabel111.setText("Select account:");

        adAcntCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adAcntComboActionPerformed(evt);
            }
        });

        jLabel112.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(255, 255, 255));
        jLabel112.setText("Select category:");

        jLabel113.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(255, 255, 255));
        jLabel113.setText("Enter amount:");

        saveBgtBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        saveBgtBtn.setForeground(new java.awt.Color(139, 0, 0));
        saveBgtBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        saveBgtBtn.setText("Save");
        saveBgtBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBgtBtnActionPerformed(evt);
            }
        });

        jButton32.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton32.setForeground(new java.awt.Color(139, 0, 0));
        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton32.setText("Cancel");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel69Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel69Layout.createSequentialGroup()
                        .addComponent(jLabel110)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bstartPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel69Layout.createSequentialGroup()
                        .addComponent(jLabel111)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(adAcntCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel69Layout.createSequentialGroup()
                        .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveBgtBtn)
                            .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel112)
                                .addComponent(jLabel113)))
                        .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel69Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(adbgtCatCompo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bgtAmnt)))
                            .addGroup(jPanel69Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton32)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel69Layout.createSequentialGroup()
                .addComponent(jPanel70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel110)
                    .addComponent(bstartPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111)
                    .addComponent(adAcntCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel112)
                    .addComponent(adbgtCatCompo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113)
                    .addComponent(bgtAmnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBgtBtn)
                    .addComponent(jButton32))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddBudgetLayout = new javax.swing.GroupLayout(AddBudget.getContentPane());
        AddBudget.getContentPane().setLayout(AddBudgetLayout);
        AddBudgetLayout.setHorizontalGroup(
            AddBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddBudgetLayout.createSequentialGroup()
                .addComponent(jPanel69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        AddBudgetLayout.setVerticalGroup(
            AddBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        EditBudget.setMinimumSize(new java.awt.Dimension(513, 278));
        EditBudget.setModal(true);
        EditBudget.setUndecorated(true);
        EditBudget.setResizable(false);

        jPanel71.setBackground(new java.awt.Color(44, 62, 80));

        jPanel72.setBackground(new java.awt.Color(0, 0, 139));

        jLabel114.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(255, 215, 0));
        jLabel114.setText("Edit Budget");

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel114)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel114)
        );

        jLabel115.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(255, 255, 255));
        jLabel115.setText("Select start period:");

        jLabel116.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(255, 255, 255));
        jLabel116.setText("Select account:");

        adAcntCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adAcntCombo1ActionPerformed(evt);
            }
        });

        jLabel117.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(255, 255, 255));
        jLabel117.setText("Select category:");

        jLabel118.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel118.setForeground(new java.awt.Color(255, 255, 255));
        jLabel118.setText("Enter amount:");

        saveBgtBtn1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        saveBgtBtn1.setForeground(new java.awt.Color(139, 0, 0));
        saveBgtBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save-xxl.png"))); // NOI18N
        saveBgtBtn1.setText("Save");
        saveBgtBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBgtBtn1ActionPerformed(evt);
            }
        });

        jButton36.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton36.setForeground(new java.awt.Color(139, 0, 0));
        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Cancel16.png"))); // NOI18N
        jButton36.setText("Cancel");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel71Layout = new javax.swing.GroupLayout(jPanel71);
        jPanel71.setLayout(jPanel71Layout);
        jPanel71Layout.setHorizontalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel71Layout.createSequentialGroup()
                        .addComponent(jLabel115)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bstartPeriod1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel71Layout.createSequentialGroup()
                        .addComponent(jLabel116)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(adAcntCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel71Layout.createSequentialGroup()
                        .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveBgtBtn1)
                            .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel117)
                                .addComponent(jLabel118)))
                        .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel71Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(adbgtCatCompo1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bgtAmnt1)))
                            .addGroup(jPanel71Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton36)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel71Layout.setVerticalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addComponent(jPanel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel115)
                    .addComponent(bstartPeriod1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel116)
                    .addComponent(adAcntCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel117)
                    .addComponent(adbgtCatCompo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel118)
                    .addComponent(bgtAmnt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBgtBtn1)
                    .addComponent(jButton36))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EditBudgetLayout = new javax.swing.GroupLayout(EditBudget.getContentPane());
        EditBudget.getContentPane().setLayout(EditBudgetLayout);
        EditBudgetLayout.setHorizontalGroup(
            EditBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditBudgetLayout.createSequentialGroup()
                .addComponent(jPanel71, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        EditBudgetLayout.setVerticalGroup(
            EditBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit30.jpg"))); // NOI18N
        jButton1.setToolTipText("Logout");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        addAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_chart_ofaccounts40.png"))); // NOI18N
        addAccount.setToolTipText("Add an account");
        addAccount.setFocusable(false);
        addAccount.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addAccount.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountActionPerformed(evt);
            }
        });
        jToolBar1.add(addAccount);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/transactions30.jpeg"))); // NOI18N
        jButton2.setToolTipText("Add transactiosn");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        addAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/man_icon.png"))); // NOI18N
        addAdmin.setFocusable(false);
        addAdmin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addAdmin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAdminActionPerformed(evt);
            }
        });
        jToolBar1.add(addAdmin);

        addClassroom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/classroom30.png"))); // NOI18N
        addClassroom.setToolTipText("Add a class");
        addClassroom.setFocusable(false);
        addClassroom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addClassroom.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addClassroom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassroomActionPerformed(evt);
            }
        });
        jToolBar1.add(addClassroom);

        addPupil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pupil40.jpeg"))); // NOI18N
        addPupil.setToolTipText("Add a pupil");
        addPupil.setFocusable(false);
        addPupil.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addPupil.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addPupil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPupilActionPerformed(evt);
            }
        });
        jToolBar1.add(addPupil);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/school_fees30.png"))); // NOI18N
        jButton11.setToolTipText("Fees Payment");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton11);

        addEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_employee30.png"))); // NOI18N
        addEmployee.setToolTipText("Add employees");
        addEmployee.setFocusable(false);
        addEmployee.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addEmployee.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeActionPerformed(evt);
            }
        });
        jToolBar1.add(addEmployee);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/supplier30.png"))); // NOI18N
        jButton7.setToolTipText("Add supplier");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/supplies35.png"))); // NOI18N
        jButton16.setToolTipText("Add supplies");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton16);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/salary-icon-2420930.png"))); // NOI18N
        jButton10.setToolTipText("Add salary");
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        sideMenu.setBackground(new java.awt.Color(47, 79, 79));
        sideMenu.setForeground(new java.awt.Color(0, 0, 128));

        lblChartsOfAccounts.setFont(new java.awt.Font("URW Gothic L", 1, 15)); // NOI18N
        lblChartsOfAccounts.setForeground(new java.awt.Color(254, 240, 219));
        lblChartsOfAccounts.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblChartsOfAccounts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/accounting.png"))); // NOI18N
        lblChartsOfAccounts.setText("Charts of Accounts");
        lblChartsOfAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblChartsOfAccountsMouseClicked(evt);
            }
        });

        lblClassLists.setFont(new java.awt.Font("URW Gothic L", 1, 15)); // NOI18N
        lblClassLists.setForeground(new java.awt.Color(254, 240, 219));
        lblClassLists.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblClassLists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/class-with-students-and-whiteboard.png"))); // NOI18N
        lblClassLists.setText("Class Lists");
        lblClassLists.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClassListsMouseClicked(evt);
            }
        });

        employeesPanelLabel.setFont(new java.awt.Font("URW Gothic L", 1, 15)); // NOI18N
        employeesPanelLabel.setForeground(new java.awt.Color(254, 240, 219));
        employeesPanelLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        employeesPanelLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/employees.png"))); // NOI18N
        employeesPanelLabel.setText("Employees");
        employeesPanelLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeesPanelLabelMouseClicked(evt);
            }
        });

        suppliersLabelPanel.setFont(new java.awt.Font("URW Gothic L", 1, 15)); // NOI18N
        suppliersLabelPanel.setForeground(new java.awt.Color(254, 240, 219));
        suppliersLabelPanel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        suppliersLabelPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hotel-supplier.png"))); // NOI18N
        suppliersLabelPanel.setText("Suppliers");
        suppliersLabelPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliersLabelPanelMouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tibetan Machine Uni", 1, 48)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("FMIS");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/admin100.jpg"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Ubuntu", 3, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Financial Management Information System");

        jLabel7.setFont(new java.awt.Font("URW Gothic L", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(254, 240, 219));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/budget(1).png"))); // NOI18N
        jLabel7.setText("Budgets");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout sideMenuLayout = new javax.swing.GroupLayout(sideMenu);
        sideMenu.setLayout(sideMenuLayout);
        sideMenuLayout.setHorizontalGroup(
            sideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideMenuLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(sideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suppliersLabelPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(employeesPanelLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblChartsOfAccounts, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblClassLists, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(sideMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(sideMenuLayout.createSequentialGroup()
                .addGroup(sideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sideMenuLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel1))
                    .addGroup(sideMenuLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel13)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sideMenuLayout.setVerticalGroup(
            sideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideMenuLayout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblChartsOfAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblClassLists, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeesPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suppliersLabelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.CardLayout());

        mainLayer.setBackground(new java.awt.Color(255, 255, 255));

        logo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Noto Sans CJK JP Bold", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(139, 0, 0));
        jLabel9.setText("P.O Box 278");

        jLabel12.setFont(new java.awt.Font("Ubuntu", 3, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(139, 0, 0));
        jLabel12.setText("Email:contacts@fairlawns.com / Call:0788888888");

        jLabel10.setFont(new java.awt.Font("Noto Sans CJK JP Bold", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(139, 0, 0));
        jLabel10.setText("Nairobi Kenya");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3978749_a92af331caf24b8b91ed604108f5c31b4.jpg"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Ubuntu", 3, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(139, 0, 0));
        jLabel11.setText("All rights reserved @ failawns puplic primary school");

        jLabel15.setFont(new java.awt.Font("Noto Sans CJK JP Bold", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(139, 0, 0));
        jLabel15.setText("Fairlawns Public Primary School");

        javax.swing.GroupLayout logoLayout = new javax.swing.GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15)
                        .addGroup(logoLayout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addComponent(jLabel10))
                        .addGroup(logoLayout.createSequentialGroup()
                            .addGap(69, 69, 69)
                            .addGroup(logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addComponent(jLabel11))))
                    .addGroup(logoLayout.createSequentialGroup()
                        .addGroup(logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(logoLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel8))
                            .addGroup(logoLayout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addComponent(jLabel9)))
                        .addGap(138, 138, 138)))
                .addContainerGap())
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainLayerLayout = new javax.swing.GroupLayout(mainLayer);
        mainLayer.setLayout(mainLayerLayout);
        mainLayerLayout.setHorizontalGroup(
            mainLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayerLayout.createSequentialGroup()
                .addGap(341, 341, 341)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(646, Short.MAX_VALUE))
        );
        mainLayerLayout.setVerticalGroup(
            mainLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayerLayout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        jPanel1.add(mainLayer, "card2");

        accountCharts.setBackground(new java.awt.Color(173, 216, 230));

        jPanel3.setBackground(new java.awt.Color(173, 216, 230));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80), 3));
        jTabbedPane1.setForeground(new java.awt.Color(44, 62, 80));
        jTabbedPane1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        accntTable.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        accntTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        accntTable.setRowHeight(20);
        accntTable.setRowMargin(2);
        jScrollPane1.setViewportView(accntTable);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18), new java.awt.Color(139, 0, 0))); // NOI18N

        accountsEdit.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        accountsEdit.setForeground(new java.awt.Color(44, 62, 80));
        accountsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-new-icon-22lg.png"))); // NOI18N
        accountsEdit.setText("Edit");
        accountsEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountsEditActionPerformed(evt);
            }
        });

        dltAccount.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        dltAccount.setForeground(new java.awt.Color(44, 62, 80));
        dltAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        dltAccount.setText("Delete");
        dltAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltAccountActionPerformed(evt);
            }
        });

        searchAccounts.setText("Search........");
        searchAccounts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchAccountsKeyReleased(evt);
            }
        });

        prntAccounts.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        prntAccounts.setForeground(new java.awt.Color(44, 62, 80));
        prntAccounts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        prntAccounts.setText("Print");
        prntAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prntAccountsActionPerformed(evt);
            }
        });

        chartsofAccountspdfButton.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        chartsofAccountspdfButton.setForeground(new java.awt.Color(44, 62, 80));
        chartsofAccountspdfButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        chartsofAccountspdfButton.setText("Generate pdf");
        chartsofAccountspdfButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chartsofAccountspdfButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(accountsEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dltAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchAccounts)
            .addComponent(prntAccounts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(chartsofAccountspdfButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(searchAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountsEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dltAccount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prntAccounts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chartsofAccountspdfButton))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Chart of Accounts", jPanel2);

        transactionTable.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        transactionTable.setRowHeight(20);
        transactionTable.setRowMargin(2);
        jScrollPane3.setViewportView(transactionTable);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18), new java.awt.Color(139, 0, 0))); // NOI18N

        jButton6.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton6.setForeground(new java.awt.Color(44, 62, 80));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-new-icon-22lg.png"))); // NOI18N
        jButton6.setText("Edit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        transactionsDelete.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        transactionsDelete.setForeground(new java.awt.Color(44, 62, 80));
        transactionsDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        transactionsDelete.setText("Delete");
        transactionsDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionsDeleteActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton8.setForeground(new java.awt.Color(44, 62, 80));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        jButton8.setText("Print table");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        transactionsReport.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        transactionsReport.setForeground(new java.awt.Color(44, 62, 80));
        transactionsReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        transactionsReport.setText("Generate pdf");
        transactionsReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionsReportActionPerformed(evt);
            }
        });

        seachTransactions.setText("Search........");
        seachTransactions.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seachTransactionsKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(transactionsDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(transactionsReport, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
            .addComponent(seachTransactions)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(seachTransactions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transactionsDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transactionsReport)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel16.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(44, 62, 80));
        jLabel16.setText("Total Transactions:");

        totalTran.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        totalTran.setForeground(new java.awt.Color(139, 0, 0));
        totalTran.setText("totalTran");

        jLabel18.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(44, 62, 80));
        jLabel18.setText("Maximum Transaction:");

        maxTran.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        maxTran.setForeground(new java.awt.Color(139, 0, 0));
        maxTran.setText("Value");

        jLabel20.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(44, 62, 80));
        jLabel20.setText("Minimum Transaction:");

        minTran.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        minTran.setForeground(new java.awt.Color(139, 0, 0));
        minTran.setText("Value");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(totalTran)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(minTran))
                    .addComponent(maxTran))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalTran)
                    .addComponent(jLabel16)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(minTran)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(maxTran))
                .addGap(0, 101, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transactions", jPanel5);

        jPanel4.setBackground(new java.awt.Color(42, 62, 80));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 215, 0));
        jLabel2.setText("Chart of Accounts");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        javax.swing.GroupLayout accountChartsLayout = new javax.swing.GroupLayout(accountCharts);
        accountCharts.setLayout(accountChartsLayout);
        accountChartsLayout.setHorizontalGroup(
            accountChartsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        accountChartsLayout.setVerticalGroup(
            accountChartsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(accountCharts, "card2");

        classLists.setBackground(new java.awt.Color(173, 216, 230));

        jPanel8.setBackground(new java.awt.Color(44, 62, 80));

        jLabel17.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 215, 0));
        jLabel17.setText("Class Lists");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17)
        );

        jPanel9.setBackground(new java.awt.Color(173, 216, 230));

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jTabbedPane2.setForeground(new java.awt.Color(44, 62, 80));
        jTabbedPane2.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        classPayments.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        classPayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        classPayments.setRowHeight(20);
        classPayments.setRowMargin(2);
        jScrollPane5.setViewportView(classPayments);

        jLabel19.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(42, 62, 80));
        jLabel19.setText("Total Fees Paid:");

        jLabel21.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(42, 62, 80));
        jLabel21.setText("Percentage paid:");

        jLabel22.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(42, 62, 80));
        jLabel22.setText("Total Balances:");

        jLabel23.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(42, 62, 80));
        jLabel23.setText("Percentage balances:");

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 24), new java.awt.Color(139, 0, 0))); // NOI18N

        printClassPayments.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        printClassPayments.setForeground(new java.awt.Color(42, 62, 80));
        printClassPayments.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        printClassPayments.setText("Print");
        printClassPayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printClassPaymentsActionPerformed(evt);
            }
        });

        printClassLists.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        printClassLists.setForeground(new java.awt.Color(42, 62, 80));
        printClassLists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        printClassLists.setText("Generate pdf");
        printClassLists.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printClassListsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printClassPayments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(printClassLists, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printClassPayments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printClassLists)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        totalFees.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        totalFees.setForeground(new java.awt.Color(139, 0, 0));
        totalFees.setText("Total Fees");

        totalBalance.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        totalBalance.setForeground(new java.awt.Color(139, 0, 0));
        totalBalance.setText("Total balances");

        pcntBalance.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        pcntBalance.setForeground(new java.awt.Color(139, 0, 0));
        pcntBalance.setText("Percentage balances");

        pcntPaid.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        pcntPaid.setForeground(new java.awt.Color(139, 0, 0));
        pcntPaid.setText("Percentage paid");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalFees, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcntPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(42, 42, 42)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pcntBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(totalFees)
                    .addComponent(jLabel22)
                    .addComponent(totalBalance))
                .addGap(33, 33, 33)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(pcntPaid)
                    .addComponent(jLabel23)
                    .addComponent(pcntBalance))
                .addGap(137, 137, 137))
        );

        jTabbedPane2.addTab("Class payments", jPanel10);

        pupilsPayments.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        pupilsPayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        pupilsPayments.setRowHeight(20);
        pupilsPayments.setRowMargin(2);
        jScrollPane6.setViewportView(pupilsPayments);

        jLabel24.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(42, 62, 80));
        jLabel24.setText("Select class:");

        selClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        selClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selClassActionPerformed(evt);
            }
        });

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 24), new java.awt.Color(139, 0, 0))); // NOI18N
        jPanel28.setForeground(new java.awt.Color(42, 62, 80));

        pupilsPaymentEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-new-icon-22lg.png"))); // NOI18N
        pupilsPaymentEdit.setText("Edit");
        pupilsPaymentEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupilsPaymentEditActionPerformed(evt);
            }
        });

        searchPupilsPayments.setText("Search........");
        searchPupilsPayments.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchPupilsPaymentsKeyReleased(evt);
            }
        });

        pupilsPayDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        pupilsPayDelete.setText("Delete");
        pupilsPayDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupilsPayDeleteActionPerformed(evt);
            }
        });

        pupilsPayPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        pupilsPayPrint.setText("Print");
        pupilsPayPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupilsPayPrintActionPerformed(evt);
            }
        });

        paymentsReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        paymentsReport.setText("Generate pdf");
        paymentsReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentsReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pupilsPaymentEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchPupilsPayments)
            .addComponent(pupilsPayDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pupilsPayPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(paymentsReport, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(searchPupilsPayments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(pupilsPaymentEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pupilsPayDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pupilsPayPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentsReport)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selClass, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(selClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(445, Short.MAX_VALUE))
                    .addComponent(jScrollPane6)))
        );

        jTabbedPane2.addTab("Pupils Payments", jPanel11);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        javax.swing.GroupLayout classListsLayout = new javax.swing.GroupLayout(classLists);
        classLists.setLayout(classListsLayout);
        classListsLayout.setHorizontalGroup(
            classListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        classListsLayout.setVerticalGroup(
            classListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classListsLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(classLists, "card2");

        employees.setBackground(new java.awt.Color(255, 255, 255));

        jPanel18.setBackground(new java.awt.Color(173, 216, 230));

        jPanel13.setBackground(new java.awt.Color(44, 62, 80));

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 215, 0));
        jLabel3.setText("Employees Panel");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3)
        );

        jPanel14.setBackground(new java.awt.Color(173, 216, 230));

        jTabbedPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80), 3));
        jTabbedPane3.setForeground(new java.awt.Color(44, 62, 80));
        jTabbedPane3.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        employeesTable.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N
        employeesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        employeesTable.setRowHeight(20);
        employeesTable.setRowMargin(2);
        jScrollPane4.setViewportView(employeesTable);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18), new java.awt.Color(42, 62, 80))); // NOI18N

        employeeEdit.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        employeeEdit.setForeground(new java.awt.Color(42, 62, 80));
        employeeEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-new-icon-22lg.png"))); // NOI18N
        employeeEdit.setText("Edit");
        employeeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeEditActionPerformed(evt);
            }
        });

        employeeDelete.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        employeeDelete.setForeground(new java.awt.Color(42, 62, 80));
        employeeDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        employeeDelete.setText("Delete");
        employeeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeDeleteActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton12.setForeground(new java.awt.Color(42, 62, 80));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        jButton12.setText("Print");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        employeesReport.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        employeesReport.setForeground(new java.awt.Color(42, 62, 80));
        employeesReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        employeesReport.setText("Generate Pdf");
        employeesReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeesReportActionPerformed(evt);
            }
        });

        searchEmployees.setText("Search.................");
        searchEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchEmployeesActionPerformed(evt);
            }
        });
        searchEmployees.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchEmployeesKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(employeeEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(employeeDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(employeesReport, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
            .addComponent(searchEmployees)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(searchEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(employeeEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeeDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeesReport)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(517, Short.MAX_VALUE))
                    .addComponent(jScrollPane4)))
        );

        jTabbedPane3.addTab("Employees List", jPanel15);

        salaryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane10.setViewportView(salaryTable);

        jPanel66.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 24), new java.awt.Color(139, 0, 0))); // NOI18N

        jButton3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton3.setForeground(new java.awt.Color(44, 62, 80));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        searchSalaries.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchSalariesKeyReleased(evt);
            }
        });

        jButton24.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton24.setForeground(new java.awt.Color(44, 62, 80));
        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        jButton24.setText("Print");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        printSalaries.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        printSalaries.setForeground(new java.awt.Color(44, 62, 80));
        printSalaries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        printSalaries.setText("Generate pdf");
        printSalaries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSalariesActionPerformed(evt);
            }
        });

        printSalaries1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        printSalaries1.setForeground(new java.awt.Color(44, 62, 80));
        printSalaries1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recipt16.png"))); // NOI18N
        printSalaries1.setText("Payslip");
        printSalaries1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSalaries1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel66Layout = new javax.swing.GroupLayout(jPanel66);
        jPanel66.setLayout(jPanel66Layout);
        jPanel66Layout.setHorizontalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchSalaries)
            .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(printSalaries, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
            .addComponent(printSalaries1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        jPanel66Layout.setVerticalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel66Layout.createSequentialGroup()
                .addComponent(searchSalaries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jButton3)
                .addGap(7, 7, 7)
                .addComponent(jButton24)
                .addGap(7, 7, 7)
                .addComponent(printSalaries)
                .addGap(7, 7, 7)
                .addComponent(printSalaries1))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(528, Short.MAX_VALUE))
                    .addComponent(jScrollPane10)))
        );

        jTabbedPane3.addTab("Salaries Payments", jPanel16);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout employeesLayout = new javax.swing.GroupLayout(employees);
        employees.setLayout(employeesLayout);
        employeesLayout.setHorizontalGroup(
            employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        employeesLayout.setVerticalGroup(
            employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(employees, "card2");

        reports.setBackground(new java.awt.Color(255, 255, 255));

        jPanel26.setBackground(new java.awt.Color(173, 216, 230));
        jPanel26.setForeground(new java.awt.Color(173, 216, 230));

        jPanel27.setBackground(new java.awt.Color(44, 62, 80));

        jLabel26.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 215, 0));
        jLabel26.setText("Reports Panel");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26)
        );

        jPanel29.setBackground(new java.awt.Color(173, 216, 230));

        jTabbedPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80), 3));
        jTabbedPane5.setForeground(new java.awt.Color(44, 62, 80));
        jTabbedPane5.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 771, Short.MAX_VALUE)
        );

        jTabbedPane5.addTab("Income Statement", jPanel30);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 771, Short.MAX_VALUE)
        );

        jTabbedPane5.addTab("Pie Charts", jPanel31);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 771, Short.MAX_VALUE)
        );

        jTabbedPane5.addTab("Bar Charts", jPanel32);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane5)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane5)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout reportsLayout = new javax.swing.GroupLayout(reports);
        reports.setLayout(reportsLayout);
        reportsLayout.setHorizontalGroup(
            reportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        reportsLayout.setVerticalGroup(
            reportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(reports, "card2");

        suppliers.setBackground(new java.awt.Color(255, 255, 255));

        jPanel19.setBackground(new java.awt.Color(173, 216, 230));
        jPanel19.setForeground(new java.awt.Color(42, 52, 80));

        jPanel20.setBackground(new java.awt.Color(42, 62, 80));

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 215, 0));
        jLabel4.setText("Suppliers Panel");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(173, 216, 230));

        jTabbedPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80), 3));
        jTabbedPane4.setForeground(new java.awt.Color(44, 62, 80));
        jTabbedPane4.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        suppliersTable.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        suppliersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        suppliersTable.setRowHeight(20);
        suppliersTable.setRowMargin(2);
        jScrollPane7.setViewportView(suppliersTable);

        jPanel52.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 24), new java.awt.Color(139, 0, 0))); // NOI18N

        searchSuppliers.setText("Seach................");
        searchSuppliers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchSuppliersKeyReleased(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton14.setForeground(new java.awt.Color(44, 62, 80));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-new-icon-22lg.png"))); // NOI18N
        jButton14.setText("Edit");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        supplierDelete.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        supplierDelete.setForeground(new java.awt.Color(44, 62, 80));
        supplierDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        supplierDelete.setText("Delete");
        supplierDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierDeleteActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton17.setForeground(new java.awt.Color(44, 62, 80));
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        jButton17.setText("Print");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton18.setForeground(new java.awt.Color(44, 62, 80));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        jButton18.setText("Generate pdf");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchSuppliers)
            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(supplierDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel52Layout.createSequentialGroup()
                .addComponent(searchSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplierDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jPanel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)))
        );

        jTabbedPane4.addTab("Suppliers Lists", jPanel22);

        suppliesTable.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        suppliesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        suppliesTable.setRowHeight(20);
        suppliesTable.setRowMargin(2);
        jScrollPane9.setViewportView(suppliesTable);

        jPanel57.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 24), new java.awt.Color(139, 0, 0))); // NOI18N

        suppliesEdit.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        suppliesEdit.setForeground(new java.awt.Color(44, 62, 80));
        suppliesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-new-icon-22lg.png"))); // NOI18N
        suppliesEdit.setText("Edit");
        suppliesEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliesEditActionPerformed(evt);
            }
        });

        suppliesBtnDelete.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        suppliesBtnDelete.setForeground(new java.awt.Color(44, 62, 80));
        suppliesBtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-xxl.png"))); // NOI18N
        suppliesBtnDelete.setText("Delete");
        suppliesBtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliesBtnDeleteActionPerformed(evt);
            }
        });

        jButton27.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton27.setForeground(new java.awt.Color(44, 62, 80));
        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print30.png"))); // NOI18N
        jButton27.setText("Print");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        suppliesReport.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        suppliesReport.setForeground(new java.awt.Color(44, 62, 80));
        suppliesReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf30.png"))); // NOI18N
        suppliesReport.setText("Generate pdf");
        suppliesReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliesReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(suppliesEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(suppliesBtnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
            .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
            .addComponent(suppliesReport, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addComponent(suppliesEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suppliesBtnDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suppliesReport))
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(323, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Supplies Lists", jPanel23);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout suppliersLayout = new javax.swing.GroupLayout(suppliers);
        suppliers.setLayout(suppliersLayout);
        suppliersLayout.setHorizontalGroup(
            suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        suppliersLayout.setVerticalGroup(
            suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(suppliers, "card2");

        budget.setBackground(new java.awt.Color(255, 255, 255));

        jPanel33.setBackground(new java.awt.Color(173, 216, 230));

        jPanel34.setBackground(new java.awt.Color(44, 62, 80));
        jPanel34.setForeground(new java.awt.Color(44, 62, 80));

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 215, 0));
        jLabel5.setText("Budget Panel");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5)
        );

        jPanel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80), 3));

        budgetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(budgetTable);

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(139, 0, 0));
        jLabel6.setText("Select period:");

        to.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        to.setForeground(new java.awt.Color(139, 0, 0));
        to.setText("to");

        addPeriod.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        addPeriod.setForeground(new java.awt.Color(44, 62, 80));
        addPeriod.setText("Add period");
        addPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPeriodActionPerformed(evt);
            }
        });

        addBudgetBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        addBudgetBtn.setForeground(new java.awt.Color(44, 62, 80));
        addBudgetBtn.setText("Add Budget");
        addBudgetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBudgetBtnActionPerformed(evt);
            }
        });

        jButton22.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton22.setForeground(new java.awt.Color(44, 62, 80));
        jButton22.setText("Edit");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton23.setForeground(new java.awt.Color(44, 62, 80));
        jButton23.setText("Delete");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton34.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton34.setForeground(new java.awt.Color(44, 62, 80));
        jButton34.setText("Print");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButton35.setForeground(new java.awt.Color(44, 62, 80));
        jButton35.setText("Generate pdf");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btsttPanCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(to)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addPeriod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addBudgetBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton35)
                .addContainerGap(419, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btsttPanCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(to)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addPeriod)
                    .addComponent(addBudgetBtn)
                    .addComponent(jButton22)
                    .addComponent(jButton23)
                    .addComponent(jButton34)
                    .addComponent(jButton35))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout budgetLayout = new javax.swing.GroupLayout(budget);
        budget.setLayout(budgetLayout);
        budgetLayout.setHorizontalGroup(
            budgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        budgetLayout.setVerticalGroup(
            budgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(budget, "card2");

        jMenu3.setText("File");
        jMenu3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit16.jpg"))); // NOI18N
        jMenuItem1.setText("Logout");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close16.png"))); // NOI18N
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenu4.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jMenuBar2.add(jMenu4);

        jMenu1.setText("Insert");
        jMenu1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jMenuItem4.setText("Department");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jMenuItem5.setText("Positions");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar2.add(jMenu1);

        jMenu5.setText("Transactions");
        jMenu5.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jMenuBar2.add(jMenu5);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Login l = new Login();
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        Login l = new Login();
        l.setVisible(true);
        chartsTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountActionPerformed
        // TODO add your handling code here:
        AddAccount.setVisible(true);
    }//GEN-LAST:event_addAccountActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        AddTransaction.setVisible(true);
        // aT.setVisible(true);
        chartsTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void lblChartsOfAccountsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChartsOfAccountsMouseClicked
        // TODO add your handling code here:
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();
        jPanel1.add(accountCharts);
        jPanel1.repaint();
        jPanel1.revalidate();
    }//GEN-LAST:event_lblChartsOfAccountsMouseClicked

    private void lblClassListsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClassListsMouseClicked
        // TODO add your handling code here:
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();
        jPanel1.add(classLists);
        jPanel1.repaint();
        jPanel1.revalidate();
    }//GEN-LAST:event_lblClassListsMouseClicked

    private void addClassroomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassroomActionPerformed
        // TODO add your handling code here:
        AddClass.setVisible(true);
    }//GEN-LAST:event_addClassroomActionPerformed

    private void addEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeActionPerformed
        // TODO add your handling code here:
        AddEmployee.setVisible(true);
    }//GEN-LAST:event_addEmployeeActionPerformed

    private void addAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAdminActionPerformed
        // TODO add your handling code here:
        AddAdmin aD = new AddAdmin();
        aD.setVisible(true);
    }//GEN-LAST:event_addAdminActionPerformed

    private void addPupilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPupilActionPerformed
        // TODO add your handling code here:
        AddPupil.setVisible(true);
    }//GEN-LAST:event_addPupilActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Payments.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        AddDepartment aD = new AddDepartment();
        aD.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        AddPosition pos = new AddPosition();
        pos.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void employeesPanelLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeesPanelLabelMouseClicked
        // TODO add your handling code here:
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();
        jPanel1.add(employees);
        jPanel1.repaint();
        jPanel1.revalidate();
    }//GEN-LAST:event_employeesPanelLabelMouseClicked

    private void suppliersLabelPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersLabelPanelMouseClicked
        // TODO add your handling code here:
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();
        jPanel1.add(suppliers);
        jPanel1.repaint();
        jPanel1.revalidate();
    }//GEN-LAST:event_suppliersLabelPanelMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();
        jPanel1.add(budget);
        jPanel1.repaint();
        jPanel1.revalidate();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void transSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transSaveActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "INSERT INTO transactions (acat_name,t_name,t_amount,t_date) VALUES(?,?,?,CURRENT_TIMESTAMP)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) acntCatName.getSelectedItem());
            pst.setString(2, acntDescription.getText());
            pst.setString(3, tAmount.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added a transaction");
            acntDescription.setText("");
            tAmount.setText("");
            transTable();
            totalTransactions();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_transSaveActionPerformed
//FILLING ACCOUNTS JDIALOG COMBOBOX

    private void FillAccountCombo() {
        try {
            String sql = "SELECT * FROM accounts";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String cat = rs.getString("acc_name");
                accntCategory.addItem(cat);
                //adAcntCombo.addItem(cat);
                //adAcntCombo1.addItem(cat);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //FILL UPDATE ACCOUNTS COMBOBOX

    private void FillUpdateAccountCombo() {
        try {
            String sql = "SELECT * FROM accounts";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String cat = rs.getString("acc_name");
                uaCombo.addItem(cat);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void btnTransCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransCancelActionPerformed
        // TODO add your handling code here:
        AddTransaction.dispose();
    }//GEN-LAST:event_btnTransCancelActionPerformed

    private void accntCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accntCancelActionPerformed
        // TODO add your handling code here:
        AddAccount.dispose();
    }//GEN-LAST:event_accntCancelActionPerformed

    private void accntSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accntSaveActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "INSERT INTO accounts_categories (acc_name,acat_name) VALUES(?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) accntCategory.getSelectedItem());
            pst.setString(2, accntName.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added an account!");
            accntName.setText("");
            chartsTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_accntSaveActionPerformed

    private void accountsEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountsEditActionPerformed
        // TODO add your handling code here:
        try {
            int row = accntTable.getSelectedRow();
            String rwId = (accntTable.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM accounts_categories WHERE acat_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                accId = rs.getString("acat_id");
                String val2 = rs.getString("acc_name");
                String val3 = rs.getString("acat_name");
                updtAccntName.setText(val3);

            } else {
                JOptionPane.showMessageDialog(null, "Value is not available");
            }
            UpdateAccounts.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_accountsEditActionPerformed

    private void updtAccntCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updtAccntCancelActionPerformed
        // TODO add your handling code here:
        UpdateAccounts.dispose();
    }//GEN-LAST:event_updtAccntCancelActionPerformed

    private void accntSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accntSave1ActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "UPDATE accounts_categories SET acc_name=?,acat_name=? WHERE acat_id=" + accId + "";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) uaCombo.getSelectedItem());
            pst.setString(2, updtAccntName.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully updated an account!");
            UpdateAccounts.dispose();
            chartsTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_accntSave1ActionPerformed

    private void dltAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltAccountActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = accntTable.getSelectedRow();
                String rwId = (accntTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM accounts_categories WHERE acat_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                chartsTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }//GEN-LAST:event_dltAccountActionPerformed

    private void prntAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prntAccountsActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL ACCOUNTS LIST");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            accntTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_prntAccountsActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL TRANSACTIONS LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            transactionTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void transactionsDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionsDeleteActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = transactionTable.getSelectedRow();
                String rwId = (transactionTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM transactions WHERE t_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                transTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }//GEN-LAST:event_transactionsDeleteActionPerformed

    private void transSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transSave1ActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "UPDATE transactions SET acat_name=?,t_name=?, t_amount=? WHERE t_id=" + transId + "";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) upacntCatName.getSelectedItem());
            pst.setString(2, upacntDescription.getText());
            pst.setString(3, uptAmount.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully updated a transaction!");
            UpdateTransaction.dispose();
            transTable();
            totalTransactions();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }//GEN-LAST:event_transSave1ActionPerformed

    private void btnTransCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransCancel1ActionPerformed
        // TODO add your handling code here:
        UpdateTransaction.dispose();
    }//GEN-LAST:event_btnTransCancel1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
       
        try {
            int row = transactionTable.getSelectedRow();
            String rwId = (transactionTable.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM transactions WHERE t_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                transId = rs.getString("t_id");
                String val2 = rs.getString("t_name");
                String val3 = rs.getString("t_amount");
                upacntDescription.setText(val2);
                uptAmount.setText(val3);
            } else {
                JOptionPane.showMessageDialog(null, "Value is not available");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         UpdateTransaction.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed
    /*    */
    private void savePaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePaymentActionPerformed
        // TODO add your handling code here:
       try{
            String amnt = amount.getText();
            Double pyd = Double.parseDouble(amnt);
            String cls = (String)pupilClassItm.getSelectedItem();
            String clamnt = "SELECT class_amount FROM classes WHERE class_name='"+cls+"'";
            pst = conn.prepareStatement(clamnt);
            rs = pst.executeQuery();
            if(rs.next()){
               classAmnt = rs.getString("class_amount");
            }
            double clasAmnt = Double.parseDouble(classAmnt);
            double bal = clasAmnt - pyd;
            String balance = Double.toString(bal);
            String sql = "INSERT INTO payments (class_name,admn_no,amount,p_date,balance) VALUES(?,?,?,CURRENT_TIMESTAMP,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) pupilClassItm.getSelectedItem());
            pst.setString(2, admnNo.getText());
            pst.setString(3, amount.getText());
            pst.setString(4, balance);
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully made a payment");
            amount.setText("");
            pupilsPayments();
            classPayments();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_savePaymentActionPerformed

    private void cancelPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelPaymentActionPerformed
        // TODO add your handling code here:
        Payments.dispose();
    }//GEN-LAST:event_cancelPaymentActionPerformed

    private void saveClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveClassActionPerformed
        try {
            // TODO add your handling code here:
            String sql = "INSERT INTO classes (class_name,class_amount) VALUES (?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, className.getText());
            pst.setString(2, classAmount.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added a class!");
            className.setText("");
            classAmount.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_saveClassActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        AddClass.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void selClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selClassActionPerformed
        // TODO add your handling code here:
        try {
            String clss = (String) selClass.getSelectedItem();
            if(clss == "All"){
               String sql = "SELECT p_id, class_name, admn_no,amount, balance AS BALANCES,p_date FROM payments ORDER BY class_name ASC";
               st = conn.createStatement();
               rs = st.executeQuery(sql);
               pupilsPayments.setModel(DbUtils.resultSetToTableModel(rs));
            }else{
                String sql = "SELECT payments.p_id AS ID , payments.class_name AS CLASS ,payments.admn_no AS ADMISSION_NO,payments.amount AS AMOUNT, payments.balance AS BALANCES,"
                    + "payments.p_date AS PAYMENT_DATE FROM payments WHERE payments.class_name = '"+clss+"'";
                  st = conn.createStatement();
                  rs = st.executeQuery(sql);
                  pupilsPayments.setModel(DbUtils.resultSetToTableModel(rs));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_selClassActionPerformed

    private void pupilSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pupilSaveActionPerformed
        // TODO add your handling code here:
        try{
            String sql = "INSERT INTO pupils (admn_no,pup_fname,pup_lname,class_name,admn_date) VALUES(?,?,?,?,CURRENT_TIMESTAMP)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, adminNo.getText());
            pst.setString(2, pupFName.getText());
            pst.setString(3, pupLName.getText());
            pst.setString(4, (String) pupilClass.getSelectedItem());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added a pupil");
            adminNo.setText("");
            pupFName.setText("");
            pupLName.setText("");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_pupilSaveActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        AddPupil.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void printClassPaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printClassPaymentsActionPerformed
        // TODO add your handling code here:
         MessageFormat header = new MessageFormat("FAIRLAWLNS PRIMARY SCHOOL CLASS PAYMENT LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            classPayments.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_printClassPaymentsActionPerformed

    private void pupilsPaymentEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pupilsPaymentEditActionPerformed
        // TODO add your handling code here:
        try {
            int row = pupilsPayments.getSelectedRow();
            String rwId = (pupilsPayments.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM payments WHERE p_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                payId = rs.getString("p_id");
                String val2 = rs.getString("amount");
                amountEdit.setText(val2);
            } else {
                JOptionPane.showMessageDialog(null, "Value is not available");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         EditPayments.setVisible(true);
    }//GEN-LAST:event_pupilsPaymentEditActionPerformed

    private void pupilClassItmEditItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pupilClassItmEditItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_pupilClassItmEditItemStateChanged

    private void pupilClassItmEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pupilClassItmEditActionPerformed
        // TODO add your handling code here:
        try{
            String payClass = (String) pupilClassItmEdit.getSelectedItem();
            String qry = "SELECT * FROM pupils WHERE class_name ='"+payClass+"'";
            pst = conn.prepareStatement(qry);
            rs = pst.executeQuery();
            if(rs.next()){
                String itm = rs.getString("admn_no");
                payAdmnnoEdit.addItem(itm);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_pupilClassItmEditActionPerformed

    private void savePayment1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePayment1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_savePayment1ActionPerformed

    private void cancelPayment1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelPayment1ActionPerformed
        // TODO add your handling code here:
        EditPayments.dispose();
    }//GEN-LAST:event_cancelPayment1ActionPerformed

    private void pupilsPayDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pupilsPayDeleteActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = pupilsPayments.getSelectedRow();
                String rwId = (pupilsPayments.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM payments WHERE p_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                pupilsPayments();
                classPayments();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_pupilsPayDeleteActionPerformed

    private void pupilsPayPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pupilsPayPrintActionPerformed
        // TODO add your handling code here:
         MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL PUPILS PAYMENT LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            pupilsPayments.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_pupilsPayPrintActionPerformed

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
        // TODO add your handling code here:
        AddSupplier.dispose();
    }//GEN-LAST:event_btnSupplierActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        AddSupplier.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void supBtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supBtnSaveActionPerformed
        // TODO add your handling code here:
        try{
            String sql = "INSERT INTO suppliers(sup_fname,sup_lname,sup_email,sup_phone,sup_product,sup_date) VALUES(?,?,?,?,?,CURRENT_TIMESTAMP)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,sFName.getText());
            pst.setString(2,sLName.getText());
            pst.setString(3,sEmail.getText());
            pst.setString(4,sPhone.getText());
            pst.setString(5,sProduct.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added a supplier!");
            suppliersTable();
            sFName.setText("");
            sLName.setText("");
            sEmail.setText("");
            sPhone.setText("");
            sProduct.setText("");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_supBtnSaveActionPerformed

    private void editsupBtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editsupBtnSaveActionPerformed
        // TODO add your handling code here:
        try{
            String sql = "UPDATE suppliers SET sup_fname=?, sup_lname=?,sup_email=?,sup_phone=?,sup_product=? WHERE sup_id ="+supplierId+"";
            pst = conn.prepareStatement(sql);
            pst.setString(1, esFName.getText());
            pst.setString(2, esLName.getText());
            pst.setString(3, esEmail.getText());
            pst.setString(4, esPhone.getText());
            pst.setString(5, esProduct.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully updated the supplier details!");
            EditSupplier.dispose();
            suppliersTable();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_editsupBtnSaveActionPerformed

    private void editbtnSuppliercloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editbtnSuppliercloseActionPerformed
        // TODO add your handling code here:
        EditSupplier.dispose();
    }//GEN-LAST:event_editbtnSuppliercloseActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        try {
            int row = suppliersTable.getSelectedRow();
            String rwId = (suppliersTable.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM suppliers WHERE sup_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                supplierId = rs.getString("sup_id");
                String val2 = rs.getString("sup_fname");
                esFName.setText(val2);
                String val3 = rs.getString("sup_lname");
                esLName.setText(val3);
                String val4 = rs.getString("sup_email");
                esEmail.setText(val4);
                String val5 = rs.getString("sup_phone");
                esPhone.setText(val5);
                String val6 = rs.getString("sup_product");
                esProduct.setText(val6);
            } else {
                JOptionPane.showMessageDialog(null, "Value is not available");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         EditSupplier.setVisible(true);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void supplierDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierDeleteActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = suppliersTable.getSelectedRow();
                String rwId = (suppliersTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM suppliers WHERE sup_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                suppliersTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_supplierDeleteActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
         MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL SUPPLIERS LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            suppliersTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        AddSupplies.dispose();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        AddSupplies.setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        String sql = "INSERT INTO supplies (sup_product,suply_amount,suply_date) VALUES (?,?,CURRENT_TIMESTAMP)";
        try{
            pst = conn.prepareStatement(sql);
            pst.setString(1,(String)suplyProduct.getSelectedItem());
            pst.setString(2, suplyAmount.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added a supply!");
            suppliesTable();
            suplyAmount.setText("");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void suppliesEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesEditActionPerformed
        // TODO add your handling code here:
        try {
            int row = suppliesTable.getSelectedRow();
            String rwId = (suppliesTable.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM supplies WHERE suply_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                suplyId = rs.getString("suply_id");
                String val2 = rs.getString("suply_amount");
                editsuplyAmount.setText(val2);
            } else {
                JOptionPane.showMessageDialog(null, "Value is not available");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        EditSupplies.setVisible(true);
    }//GEN-LAST:event_suppliesEditActionPerformed

    private void saveEditSupplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEditSupplyActionPerformed
        // TODO add your handling code here:
        try{
            String sql = "UPDATE supplies SET sup_product =?,suply_amount=? WHERE suply_id ="+suplyId+"";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)editsuplyProduct.getSelectedItem());
            pst.setString(2, editsuplyAmount.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully updated the supplies!");
            suppliesTable();
            EditSupplies.dispose();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_saveEditSupplyActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        EditSupplies.dispose();
    }//GEN-LAST:event_jButton29ActionPerformed

    private void suppliesBtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesBtnDeleteActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = suppliesTable.getSelectedRow();
                String rwId = (suppliesTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM supplies WHERE suply_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                suppliesTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_suppliesBtnDeleteActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL SUPPLIES LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            suppliesTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        AddEmployee.dispose();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void employeeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeSaveActionPerformed
        try {
            // TODO add your handling code here:
            String sql = "INSERT INTO employees (pos_name,dep_name,emp_fname,emp_lname,id_no,phone_no,date_of_birth,date_of_appointment,emp_no,emp_email,"
                    + "salary,bonusses,allowances,deductions,postal_code,home_address) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)profName.getSelectedItem());
            pst.setString(2, (String)departNme.getSelectedItem());
            pst.setString(3, empFName.getText());
            pst.setString(4, empLName.getText());
            pst.setString(5, idNo.getText());
            pst.setString(6, phoneNo.getText());
            pst.setString(7, ((JTextField)birthDate.getDateEditor().getUiComponent()).getText());
            pst.setString(8, ((JTextField)appointDate.getDateEditor().getUiComponent()).getText());
            pst.setString(9, empNo.getText());
            pst.setString(10, email.getText());
            pst.setString(11, salary.getText());
            pst.setString(12, bonus.getText());
            pst.setString(13, allowance.getText());
            pst.setString(14, deduction.getText());
            pst.setString(15, postalCode.getText());
            pst.setString(16, homeAddress.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added an employee");
            employeesTable();
            empFName.setText("");
            empLName.setText("");
            idNo.setText("");
            phoneNo.setText("");
            empNo.setText("");
            email.setText("");
            salary.setText("");
            bonus.setText("");
            allowance.setText("");
            deduction.setText("");
            postalCode.setText("");
            homeAddress.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_employeeSaveActionPerformed

    private void employeeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeEditActionPerformed
        // TODO add your handling code here:
        try {
            int row = employeesTable.getSelectedRow();
            String rwId = (employeesTable.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM employees WHERE emp_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                employeeId = rs.getString("emp_id");
                String val2 = rs.getString("emp_fname");
                empFName1.setText(val2);
                String val3 = rs.getString("emp_lname");
                empLName1.setText(val3);
                String val4 = rs.getString("id_no");
                idNo1.setText(val4);
                String val5 = rs.getString("phone_no");
                phoneNo1.setText(val5);
                Date val6 = rs.getDate("date_of_birth");
                birthDate1.setDate(val6);
                Date val7 = rs.getDate("date_of_appointment");
                appointDate1.setDate(val7);
                String val8 = rs.getString("emp_no");
                empNo1.setText(val8);
                String val9 = rs.getString("emp_email");
                email1.setText(val9);
                String val10 = rs.getString("salary");
                salary1.setText(val10);
                String val11 = rs.getString("bonusses");
                bonus1.setText(val11);
                String val12 = rs.getString("allowances");
                allowance1.setText(val12);
                String val13 = rs.getString("deductions");
                deduction1.setText(val13);
                String val14 = rs.getString("postal_code");
                postalCode1.setText(val14);
                String val15 = rs.getString("home_address");
                homeAddress1.setText(val15);
            } else {
                JOptionPane.showMessageDialog(null, "Value is not available");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        EditEmployee.setVisible(true);
    }//GEN-LAST:event_employeeEditActionPerformed

    private void employeeSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeSave1ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String sql = "UPDATE employees SET pos_name=?,dep_name=?,emp_fname=?,emp_lname=?,id_no=?,phone_no=?,date_of_birth=?,date_of_appointment=?"
                    + ",emp_no=? ,emp_email=?,salary=?,bonusses=?,allowances=?,deductions=?,postal_code=?,home_address=? WHERE emp_id="+employeeId+"";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)profName1.getSelectedItem());
            pst.setString(2, (String)departNme1.getSelectedItem());
            pst.setString(3, empFName1.getText());
            pst.setString(4, empLName1.getText());
            pst.setString(5, idNo1.getText());
            pst.setString(6, phoneNo1.getText());
            pst.setString(7, ((JTextField)birthDate1.getDateEditor().getUiComponent()).getText());
            pst.setString(8, ((JTextField)appointDate1.getDateEditor().getUiComponent()).getText());
            pst.setString(9, empNo1.getText());
            pst.setString(10, email1.getText());
            pst.setString(11, salary1.getText());
            pst.setString(12, bonus1.getText());
            pst.setString(13, allowance1.getText());
            pst.setString(14, deduction1.getText());
            pst.setString(15, postalCode1.getText());
            pst.setString(16, homeAddress1.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully updated employees details");
            employeesTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_employeeSave1ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        EditEmployee.dispose();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void employeeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeDeleteActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = employeesTable.getSelectedRow();
                String rwId = (employeesTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM employees WHERE emp_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                employeesTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_employeeDeleteActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL EMPLOYEES LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            employeesTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void salaryCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salaryCancelActionPerformed
        // TODO add your handling code here:
        AddSalary.dispose();
    }//GEN-LAST:event_salaryCancelActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        AddSalary.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void salaryPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salaryPaymentActionPerformed
        // TODO add your handling code here:
        try{
            String sql = "INSERT INTO salaries(emp_no,sal_date) VALUES(?,CURRENT_TIMESTAMP)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) salEmbNo.getSelectedItem());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successifully submitted a salary payment");
            salaryTable();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_salaryPaymentActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = salaryTable.getSelectedRow();
                String rwId = (salaryTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM salaries WHERE sal_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                salaryTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL SALIES LIST LISTS");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            salaryTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here:
        AddPeriod.dispose();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void addPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPeriodActionPerformed
        // TODO add your handling code here:
        AddPeriod.setVisible(true);
    }//GEN-LAST:event_addPeriodActionPerformed

    private void addBudgetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBudgetSaveActionPerformed
        // TODO add your handling code here:
        try{
            String sql = "INSERT INTO budget_period(start_period,end_period) VALUES(?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,((JTextField)startDate.getDateEditor().getUiComponent()).getText());
            pst.setString(2,((JTextField)endDate.getDateEditor().getUiComponent()).getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "YOu have Successfully added a budgeting period");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_addBudgetSaveActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
        AddBudget.dispose();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void addBudgetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBudgetBtnActionPerformed
        // TODO add your handling code here:
        AddBudget.setVisible(true);
    }//GEN-LAST:event_addBudgetBtnActionPerformed

    private void adAcntComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adAcntComboActionPerformed
        // TODO add your handling code here:
        String itm = (String) adAcntCombo.getSelectedItem();
        try{
            String sql = "SELECT * FROM accounts_categories WHERE acc_name='"+itm+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String val = rs.getString("acat_name");
                adbgtCatCompo.addItem(val);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_adAcntComboActionPerformed

    private void saveBgtBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBgtBtnActionPerformed
        // TODO add your handling code here:
        try{
            String sql ="INSERT INTO budgets (start_period,acc_name,acat_name,bug_amount,bug_date) VALUES(?,?,?,?,CURRENT_TIMESTAMP)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)bstartPeriod.getSelectedItem());
            pst.setString(2, (String)adAcntCombo.getSelectedItem());
            pst.setString(3, (String)adbgtCatCompo.getSelectedItem());
            pst.setString(4, bgtAmnt.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully added a budget");
             budgetTable();
            bgtAmnt.setText("");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_saveBgtBtnActionPerformed

    private void adAcntCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adAcntCombo1ActionPerformed
        // TODO add your handling code here:
        String itm = (String) adAcntCombo.getSelectedItem();
        try{
            String sql = "SELECT * FROM accounts_categories WHERE acc_name='"+itm+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                String val = rs.getString("acat_name");
                adbgtCatCompo1.addItem(val);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_adAcntCombo1ActionPerformed

    private void saveBgtBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBgtBtn1ActionPerformed
        // TODO add your handling code here:
        try{
            String sql ="UPDATE budgets SET start_period=?,acc_name=?,acat_name=?,bug_amount=? WHERE bug_id="+budgetId+"";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)bstartPeriod1.getSelectedItem());
            pst.setString(2, (String)adAcntCombo1.getSelectedItem());
            pst.setString(3, (String)adbgtCatCompo1.getSelectedItem());
            pst.setString(4, bgtAmnt1.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "You have successfully updated a budget");
            budgetTable();
            EditBudget.dispose();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_saveBgtBtn1ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
        EditBudget.dispose();
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        try{
            int row = budgetTable.getSelectedRow();
            String rwId = (budgetTable.getModel().getValueAt(row, 0).toString());
            String sql = "SELECT * FROM budgets WHERE bug_id =" + rwId + "";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
              budgetId = rs.getString("bug_id");
              String val = rs.getString("bug_amount");
              bgtAmnt1.setText(val);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        EditBudget.setVisible(true);
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recor?","Delete Record",JOptionPane.YES_NO_OPTION);
        if(x == 0){
            try {
                int row = budgetTable.getSelectedRow();
                String rwId = (budgetTable.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM budgets WHERE bug_id =" + rwId + "";
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "You have successfully deleted a record!");
                budgetTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat("FAIRLAWNS PRIMARY SCHOOL BUDGET");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            budgetTable.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void chartsofAccountspdfButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chartsofAccountspdfButtonActionPerformed
        JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Charts_of_Accounts.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT * FROM accounts_categories ORDER BY acc_name ASC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Chart of Accounts Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Chart of Accounts"));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("ID");
                table.addCell("ACCOUNTS");
                table.addCell("CATEGORY");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("acat_id"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("acc_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("acat_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
    }//GEN-LAST:event_chartsofAccountspdfButtonActionPerformed

    private void searchAccountsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchAccountsKeyReleased
        // TODO add your handling code here:
        try {
            String q = searchAccounts.getText();
            //String sql = "SELECT acat_name AS ACCOUNTS_NAMES, SUM(t_amount) AS TOTAL_AMOUNT FROM transactions GROUP BY acat_name ORDER BY SUM(t_amount) DESC";
            String sql = "SELECT acat_id AS ID, ACC_NAME AS ACCOUNTS, acat_name AS TYPE FROM accounts_categories WHERE acc_name LIKE '%"+q+"%' OR acat_name"
                    + " LIKE '%"+q+"%'";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            accntTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_searchAccountsKeyReleased

    private void seachTransactionsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seachTransactionsKeyReleased
        // TODO add your handling code here:
        try{
            String q = seachTransactions.getText();
            String sql = "SELECT t_id AS ID, acat_name AS ACCOUNT, t_name AS COMMODITY, t_amount AS AMOUNT, t_date AS DATE FROM transactions WHERE "
                    + "acat_name LIKE '%"+q+"%' OR t_name LIKE '%"+q+"%'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            transactionTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_seachTransactionsKeyReleased

    private void searchPupilsPaymentsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPupilsPaymentsKeyReleased
        // TODO add your handling code here:
        try {
            String q = searchPupilsPayments.getText();
            //String sql = "SELECT acat_name AS ACCOUNTS_NAMES, SUM(t_amount) AS TOTAL_AMOUNT FROM transactions GROUP BY acat_name ORDER BY SUM(t_amount) DESC";
            String sql = "SELECT p_id AS ID, class_name AS CLASS_NAME, admn_no AS ADMIN_NO,amount AS AMOUNT, balance AS BALANCES,p_date AS PAYMENT_DATE"
                    + " FROM payments WHERE admn_no LIKE '%"+q+"%'";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            pupilsPayments.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_searchPupilsPaymentsKeyReleased

    private void transactionsReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionsReportActionPerformed
        // TODO add your handling code here:
       JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Transactions.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT * FROM transactions ORDER BY t_date DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Transaction Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Transactions Report"));
                cell.setColspan(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("ID");
                table.addCell("ACCOUNT NAME");
                table.addCell("TRANSACTION NAME");
                table.addCell("TRANSACTION AMOUNT");
                table.addCell("TRANSACTION DATE");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("t_id"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("acat_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("t_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("t_amount"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("t_date"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_transactionsReportActionPerformed

    private void printClassListsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printClassListsActionPerformed
        // TODO add your handling code here:
     JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Class_Payment_Lists.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT class_name,SUM(amount) AS TOTAL,SUM(balance) AS BALANCES FROM payments GROUP BY class_name ORDER BY class_name ASC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Class Lists Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Class Lists Report"));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("CLASS NAME");
                table.addCell("TOTAL AMOUNT");
                table.addCell("TOTAL BALANCES");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("class_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("TOTAL"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("BALANCES"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_printClassListsActionPerformed

    private void paymentsReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentsReportActionPerformed
        // TODO add your handling code here:
        JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Pupils_Payments_Report.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT * FROM payments";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Pupils Payments Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Pupils Payments Report"));
                cell.setColspan(6);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("ID");
                table.addCell("CLASS NAME");
                table.addCell("ADMN NO");
                table.addCell("AMOUNT");
                table.addCell("BALANCES");
                table.addCell("PAYMENT DATE");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("p_id"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("class_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("admn_no"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("amount"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("balance"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("p_date"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_paymentsReportActionPerformed

    private void searchEmployeesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchEmployeesKeyReleased
        // TODO add your handling code here:
        try {
            String q = searchEmployees.getText();
            //String sql = "SELECT acat_name AS ACCOUNTS_NAMES, SUM(t_amount) AS TOTAL_AMOUNT FROM transactions GROUP BY acat_name ORDER BY SUM(t_amount) DESC";
            String sql = "SELECT emp_id AS ID,pos_name AS DESIGNATION, dep_name AS DEPARTMENT,emp_fname AS FIRST_NAME, emp_lname AS LAST_NAME,id_no AS ID_NO,phone_no" +
"                    AS PHONE_NO,emp_no AS EMPLOYEE_NO,emp_email AS EMAIL, date_of_appointment AS APPOINTMENT_DATE FROM employees WHERE emp_fname LIKE '%"+q+"%'";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            employeesTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_searchEmployeesKeyReleased

    private void employeesReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeesReportActionPerformed
        // TODO add your handling code here:
        JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Employees_Reports.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT * FROM employees";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Employees Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(10);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Employees Report"));
                cell.setColspan(10);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("EMPLOYEE NO");
                table.addCell("DESIGNATION");
                table.addCell("DEPARTMENT");
                table.addCell("FIRST NAME");
                table.addCell("LAST NAME");
                table.addCell("ID NO");
                table.addCell("PHONE NO");
                table.addCell("BIRTH DATE");
                table.addCell("APPOINTMENT DATE");
                table.addCell("EMAIL");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("emp_no"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("pos_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("dep_name"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("emp_fname"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("emp_lname"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("id_no"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("phone_no"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("date_of_birth"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("date_of_appointment"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("emp_email"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_employeesReportActionPerformed

    private void searchSalariesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSalariesKeyReleased
        // TODO add your handling code here:
        try {
            String q = searchSalaries.getText();
            //String sql = "SELECT acat_name AS ACCOUNTS_NAMES, SUM(t_amount) AS TOTAL_AMOUNT FROM transactions GROUP BY acat_name ORDER BY SUM(t_amount) DESC";
            String sql = "SELECT salaries.sal_id AS ID,employees.emp_fname AS FIRST_NAME,employees.emp_lname AS LAST_NAME,salaries.emp_no AS EMPLOYEE_NO,"
                    + "employees.salary AS BASIC_SALARY, employees.bonusses AS BONUSSES, employees.allowances AS ALLOWANCES,employees.deductions AS DEDUCTIONS,"
                    + "salaries.sal_date AS SALARY_DATE FROM salaries INNER JOIN employees ON salaries.emp_no = employees.emp_no WHERE salaries.emp_no LIKE '%"+q+"%'";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            salaryTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_searchSalariesKeyReleased

    private void printSalariesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSalariesActionPerformed
        // TODO add your handling code here:
         JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Salaries_Report.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT salaries.sal_id AS ID,employees.emp_fname AS FIRST_NAME,employees.emp_lname AS LAST_NAME,salaries.emp_no AS EMPLOYEE_NO,"
                    + "employees.salary AS BASIC_SALARY, employees.bonusses AS BONUSSES, employees.allowances AS ALLOWANCES,employees.deductions AS DEDUCTIONS,"
                    + "salaries.sal_date AS SALARY_DATE,(employees.salary + employees.bonusses + employees.allowances -employees.deductions) AS NET_SALARY "
                        + "FROM salaries INNER JOIN employees ON salaries.emp_no = employees.emp_no";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Employees Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(9);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Employees Report"));
                cell.setColspan(9);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("EMPLOYEE NO");
                table.addCell("FIRST NAME");
                table.addCell("LAST NAME");
                table.addCell("BASIC SALARY");
                table.addCell("BONUSSES");
                table.addCell("ALLOWANCES");
                table.addCell("DEDUCTIONS");
                table.addCell("NET SALARY");
                table.addCell("DATE");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("EMPLOYEE_NO"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("FIRST_NAME"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("LAST_NAME"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("BASIC_SALARY"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("BONUSSES"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("ALLOWANCES"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("DEDUCTIONS"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("NET_SALARY"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("SALARY_DATE"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_printSalariesActionPerformed

    private void printSalaries1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSalaries1ActionPerformed
        // TODO add your handling code here:
        int row = salaryTable.getSelectedRow();
        String rwId = (salaryTable.getModel().getValueAt(row, 0).toString());
        try{
            String sql = "SELECT employees.emp_fname AS FNAME,employees.emp_lname AS LNAME FROM salaries INNER JOIN employees ON "
                    + "salaries.emp_no = employees.emp_no WHERE sal_id="+rwId+"";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                firstName = rs.getString("FNAME");
                lastName = rs.getString("LNAME");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        JFileChooser dialog = new JFileChooser();
        dialog.setSelectedFile(new File(firstName+" "+lastName+"-payslip.pdf"));
        // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT employees.emp_fname AS FNAME,employees.emp_lname AS LNAME,employees.pos_name AS DESIGNATION,employees.dep_name AS"
                + " DEPARTMENT,employees.id_no AS IDNO, employees.phone_no AS PHONE,employees.date_of_birth AS BIRTH,employees.date_of_appointment AS"
                + "APPOINTMENT,employees.emp_email AS EMAIL,employees.salary AS SALARY, employees.bonusses AS BONUS, employees.allowances AS"
                + " ALLOWANCE, employees.deductions AS DEDUCTION,salaries.sal_date AS SALDATE, salaries.emp_no AS EMPNO,(employees.salary + employees.bonusses"
                + " + employees.allowances -employees.deductions) AS NET FROM salaries INNER JOIN"
                + " employees ON salaries.emp_no = employees.emp_no WHERE sal_id="+rwId+"";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document(PageSize.A5);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,10,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,10,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,10,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("PAYSLIP", FontFactory.getFont(FontFactory.TIMES_BOLD,10,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("------------------------------------------------------------------------"));
                if(rs.next()){
                    String empno = rs.getString("EMPNO");
                    String fname = rs.getString("FNAME");
                    String lname = rs.getString("LNAME");
                    String pos = rs.getString("DESIGNATION");
                    String dep = rs.getString("DEPARTMENT");
                    String id = rs.getString("IDNO");
                    String phone = rs.getString("PHONE");
                    String birth = rs.getString("BIRTH");
                    // String appoint = rs.getString("APPOINTMENT");
                    String email = rs.getString("EMAIL");
                    String sal = rs.getString("SALARY");
                    String bonus = rs.getString("BONUS");
                    String allowance = rs.getString("ALLOWANCE");
                    String deduction = rs.getString("DEDUCTION");
                    String saldate = rs.getString("SALDATE");
                    String net = rs.getString("NET");
                    document.add(new Paragraph("PERSONAL DETAILS", FontFactory.getFont(FontFactory.TIMES_BOLD,12,Font.BOLD)));
                    document.add(new Paragraph("Employee Name: "+fname+" "+lname, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Employee Phone: "+phone, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Employee NO: "+empno, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Employee ID No: "+id, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Date of Birth: "+birth, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Designation: "+pos, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Department: "+dep, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Email: "+email, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Payment Date: "+saldate, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("------------------------------------------------------------------------"));
                    document.add(new Paragraph("SALARY", FontFactory.getFont(FontFactory.TIMES_BOLD,12,Font.BOLD)));
                    document.add(new Paragraph("Email: "+email, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Basic Salary: "+sal, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Bonus: "+bonus, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Allowance: "+allowance, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("Deduction: "+deduction, FontFactory.getFont(FontFactory.TIMES_ROMAN,10)));
                    document.add(new Paragraph("NET SALARY: "+net, FontFactory.getFont(FontFactory.TIMES_BOLD,10,Font.BOLD)));
                }
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_printSalaries1ActionPerformed

    private void searchSuppliersKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSuppliersKeyReleased
        // TODO add your handling code here:
         try {
            String q = searchSuppliers.getText();
            //String sql = "SELECT acat_name AS ACCOUNTS_NAMES, SUM(t_amount) AS TOTAL_AMOUNT FROM transactions GROUP BY acat_name ORDER BY SUM(t_amount) DESC";
            String sql = "SELECT sup_id AS ID, sup_fname AS FIRST_NAME, sup_lname AS LAST_NAME,sup_email AS EMAIL,sup_phone AS PHONE, sup_product AS PRODUCT, sup_date"
                    + " AS DATE_REGISTERED FROM suppliers WHERE sup_fname LIKE '%"+q+"%' OR sup_lname LIKE '%"+q+"%'";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            suppliersTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_searchSuppliersKeyReleased

    private void searchEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchEmployeesActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_searchEmployeesActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
         // TODO add your handling code here:
       JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Suppliers_Reports.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT * FROM suppliers";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Supliers Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Suppliers Report"));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("ID");
                table.addCell("FIRST NAME");
                table.addCell("LAST NAME");
                table.addCell("EMAIL");
                table.addCell("PHONE NUMBER");
                table.addCell("PRODUCT");
                table.addCell("REGISTRATION DATE");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_id"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_fname"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_lname"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_email"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_phone"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_product"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("sup_date"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void suppliesReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesReportActionPerformed
        // TODO add your handling code here:
        JFileChooser dialog = new JFileChooser();
       dialog.setSelectedFile(new File("Supplies_Reports.pdf"));
      // File selectedFile = dialog.getSelectedFile("Chart.pdf");
        int dialogResult = dialog.showSaveDialog(null);
        if(dialogResult == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                String sql = "SELECT supplies.suply_id AS ID,supplies.suply_date AS SUPPLY_DATE, suppliers.sup_fname AS FIRST_NAME, suppliers.sup_lname AS LAST_NAME, supplies.sup_product AS PRODUCT,"
                    + "supplies.suply_amount AS AMOUNT  FROM supplies INNER JOIN suppliers ON supplies.sup_product=suppliers.sup_product";;
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // TODO add your handling code here:
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                document.add(new Paragraph("Fairlawns Primary School", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("P.O Box 278-50100", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph("Nairobi, Kenya", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                Image image = Image.getInstance("logo.jpg");
                document.add(image);
                document.add(new Paragraph("Suplies Report", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD,BaseColor.RED)));
                document.add(new Paragraph(new Date().toString()));
                document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Paragraph("Supplies Report"));
                cell.setColspan(6);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.RED);
                table.addCell(cell);
                table.addCell("ID");
                table.addCell("FIRST NAME");
                table.addCell("LAST NAME");
                table.addCell("PRODUCT");
                table.addCell("AMOUNT");
                table.addCell("SUPPLY DATE");
                while(rs.next()){
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("ID"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("FIRST_NAME"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("LAST_NAME"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("PRODUCT"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("AMOUNT"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                    table.addCell(new PdfPCell(new Paragraph(rs.getString("SUPPLY_DATE"),FontFactory.getFont(FontFactory.TIMES_ROMAN,8,Font.BOLD))));
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_suppliesReportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog AddAccount;
    private javax.swing.JDialog AddBudget;
    private javax.swing.JDialog AddClass;
    private javax.swing.JDialog AddEmployee;
    private javax.swing.JDialog AddPeriod;
    private javax.swing.JDialog AddPupil;
    private javax.swing.JDialog AddSalary;
    private javax.swing.JDialog AddSupplier;
    private javax.swing.JDialog AddSupplies;
    private javax.swing.JDialog AddTransaction;
    private javax.swing.JDialog EditBudget;
    private javax.swing.JDialog EditEmployee;
    private javax.swing.JDialog EditPayments;
    private javax.swing.JDialog EditSupplier;
    private javax.swing.JDialog EditSupplies;
    private javax.swing.JDialog Payments;
    private javax.swing.JDialog UpdateAccounts;
    private javax.swing.JDialog UpdateTransaction;
    private javax.swing.JButton accntCancel;
    private javax.swing.JComboBox<String> accntCategory;
    private javax.swing.JTextField accntName;
    private javax.swing.JButton accntSave;
    private javax.swing.JButton accntSave1;
    private javax.swing.JTable accntTable;
    private javax.swing.JPanel accountCharts;
    private javax.swing.JButton accountsEdit;
    private javax.swing.JComboBox<String> acntCatName;
    private javax.swing.JTextField acntDescription;
    private javax.swing.JComboBox<String> adAcntCombo;
    private javax.swing.JComboBox<String> adAcntCombo1;
    private javax.swing.JComboBox<String> adbgtCatCompo;
    private javax.swing.JComboBox<String> adbgtCatCompo1;
    private javax.swing.JLabel addAccntLbl;
    private javax.swing.JLabel addAccntLbl1;
    private javax.swing.JButton addAccount;
    private javax.swing.JButton addAdmin;
    private javax.swing.JButton addBudgetBtn;
    private javax.swing.JButton addBudgetSave;
    private javax.swing.JButton addClassroom;
    private javax.swing.JButton addEmployee;
    private javax.swing.JButton addPeriod;
    private javax.swing.JButton addPupil;
    private javax.swing.JTextField adminNo;
    private javax.swing.JTextField admnNo;
    private javax.swing.JTextField allowance;
    private javax.swing.JTextField allowance1;
    private javax.swing.JTextField amount;
    private javax.swing.JTextField amountEdit;
    private com.toedter.calendar.JDateChooser appointDate;
    private com.toedter.calendar.JDateChooser appointDate1;
    private javax.swing.JTextField bgtAmnt;
    private javax.swing.JTextField bgtAmnt1;
    private com.toedter.calendar.JDateChooser birthDate;
    private com.toedter.calendar.JDateChooser birthDate1;
    private javax.swing.JTextField bonus;
    private javax.swing.JTextField bonus1;
    private javax.swing.JComboBox<String> bstartPeriod;
    private javax.swing.JComboBox<String> bstartPeriod1;
    private javax.swing.JButton btnSupplier;
    private javax.swing.JButton btnTransCancel;
    private javax.swing.JButton btnTransCancel1;
    private javax.swing.JComboBox<String> btsttPanCombo;
    private javax.swing.JPanel budget;
    private javax.swing.JTable budgetTable;
    private javax.swing.JButton cancelPayment;
    private javax.swing.JButton cancelPayment1;
    private javax.swing.JButton chartsofAccountspdfButton;
    private javax.swing.JTextField classAmount;
    private javax.swing.JPanel classLists;
    private javax.swing.JTextField className;
    private javax.swing.JTable classPayments;
    private javax.swing.JTextField deduction;
    private javax.swing.JTextField deduction1;
    private javax.swing.JComboBox<String> departNme;
    private javax.swing.JComboBox<String> departNme1;
    private javax.swing.JButton dltAccount;
    private javax.swing.JButton editbtnSupplierclose;
    private javax.swing.JButton editsupBtnSave;
    private javax.swing.JTextField editsuplyAmount;
    private javax.swing.JComboBox<String> editsuplyProduct;
    private javax.swing.JTextField email;
    private javax.swing.JTextField email1;
    private javax.swing.JTextField empFName;
    private javax.swing.JTextField empFName1;
    private javax.swing.JTextField empLName;
    private javax.swing.JTextField empLName1;
    private javax.swing.JTextField empNo;
    private javax.swing.JTextField empNo1;
    private javax.swing.JButton employeeDelete;
    private javax.swing.JButton employeeEdit;
    private javax.swing.JButton employeeSave;
    private javax.swing.JButton employeeSave1;
    private javax.swing.JPanel employees;
    private javax.swing.JLabel employeesPanelLabel;
    private javax.swing.JButton employeesReport;
    private javax.swing.JTable employeesTable;
    private com.toedter.calendar.JDateChooser endDate;
    private javax.swing.JTextField esEmail;
    private javax.swing.JTextField esFName;
    private javax.swing.JTextField esLName;
    private javax.swing.JTextField esPhone;
    private javax.swing.JTextField esProduct;
    private javax.swing.JTextField homeAddress;
    private javax.swing.JTextField homeAddress1;
    private javax.swing.JTextField idNo;
    private javax.swing.JTextField idNo1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTable jTable2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblChartsOfAccounts;
    private javax.swing.JLabel lblClassLists;
    private javax.swing.JPanel logo;
    private javax.swing.JPanel mainLayer;
    private javax.swing.JLabel maxTran;
    private javax.swing.JLabel minTran;
    private javax.swing.JComboBox<String> payAdmnnoEdit;
    private javax.swing.JButton paymentsReport;
    private javax.swing.JLabel pcntBalance;
    private javax.swing.JLabel pcntPaid;
    private javax.swing.JTextField phoneNo;
    private javax.swing.JTextField phoneNo1;
    private javax.swing.JTextField postalCode;
    private javax.swing.JTextField postalCode1;
    private javax.swing.JButton printClassLists;
    private javax.swing.JButton printClassPayments;
    private javax.swing.JButton printSalaries;
    private javax.swing.JButton printSalaries1;
    private javax.swing.JButton prntAccounts;
    private javax.swing.JComboBox<String> profName;
    private javax.swing.JComboBox<String> profName1;
    private javax.swing.JTextField pupFName;
    private javax.swing.JTextField pupLName;
    private javax.swing.JComboBox<String> pupilClass;
    private javax.swing.JComboBox<String> pupilClassItm;
    private javax.swing.JComboBox<String> pupilClassItmEdit;
    private javax.swing.JButton pupilSave;
    private javax.swing.JButton pupilsPayDelete;
    private javax.swing.JButton pupilsPayPrint;
    private javax.swing.JButton pupilsPaymentEdit;
    private javax.swing.JTable pupilsPayments;
    private javax.swing.JPanel reports;
    private javax.swing.JTextField sEmail;
    private javax.swing.JTextField sFName;
    private javax.swing.JTextField sLName;
    private javax.swing.JTextField sPhone;
    private javax.swing.JTextField sProduct;
    private javax.swing.JComboBox<String> salEmbNo;
    private javax.swing.JTextField salary;
    private javax.swing.JTextField salary1;
    private javax.swing.JButton salaryCancel;
    private javax.swing.JButton salaryPayment;
    private javax.swing.JTable salaryTable;
    private javax.swing.JButton saveBgtBtn;
    private javax.swing.JButton saveBgtBtn1;
    private javax.swing.JButton saveClass;
    private javax.swing.JButton saveEditSupply;
    private javax.swing.JButton savePayment;
    private javax.swing.JButton savePayment1;
    private javax.swing.JTextField seachTransactions;
    private javax.swing.JTextField searchAccounts;
    private javax.swing.JTextField searchEmployees;
    private javax.swing.JTextField searchPupilsPayments;
    private javax.swing.JTextField searchSalaries;
    private javax.swing.JTextField searchSuppliers;
    private javax.swing.JComboBox<String> selClass;
    private javax.swing.JPanel sideMenu;
    private com.toedter.calendar.JDateChooser startDate;
    private javax.swing.JButton supBtnSave;
    private javax.swing.JTextField suplyAmount;
    private javax.swing.JComboBox<String> suplyProduct;
    private javax.swing.JButton supplierDelete;
    private javax.swing.JPanel suppliers;
    private javax.swing.JLabel suppliersLabelPanel;
    private javax.swing.JTable suppliersTable;
    private javax.swing.JButton suppliesBtnDelete;
    private javax.swing.JButton suppliesEdit;
    private javax.swing.JButton suppliesReport;
    private javax.swing.JTable suppliesTable;
    private javax.swing.JLabel supplyAmount;
    private javax.swing.JLabel supplyAmount1;
    private javax.swing.JTextField tAmount;
    private javax.swing.JButton to;
    private javax.swing.JLabel totalBalance;
    private javax.swing.JLabel totalFees;
    private javax.swing.JLabel totalTran;
    private javax.swing.JButton transSave;
    private javax.swing.JButton transSave1;
    private javax.swing.JTable transactionTable;
    private javax.swing.JButton transactionsDelete;
    private javax.swing.JButton transactionsReport;
    private javax.swing.JComboBox<String> uaCombo;
    private javax.swing.JComboBox<String> upacntCatName;
    private javax.swing.JTextField upacntDescription;
    private javax.swing.JButton updtAccntCancel;
    private javax.swing.JTextField updtAccntName;
    private javax.swing.JTextField uptAmount;
    // End of variables declaration//GEN-END:variables
}

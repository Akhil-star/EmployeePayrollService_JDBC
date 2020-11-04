package com.cg.employeedatabase;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollDBService {

    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService(){
    }

    public static EmployeePayrollDBService getInstance(){
        if(employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection(){
        String jdbcURL = "jdbc:mysql://localhost:3306/employeepayroll_service?useSSL=false";
        String userName = "root";
        String password = "Akhilkumar@1";
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Loaded!!");
        }catch (ClassNotFoundException e) {
            throw new IllegalStateException("driver not found in the classpath", e);
        }

        listDrivers();
        try {
            System.out.println("Connecting to the Database " + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection was successful");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println( "  " + driverClass.getClass().getName() );
        }
    }

    public List<EmployeePayrollData> readData(){
        String sql = "Select * from employee_payroll";
        return this.getEmployeeDataUsingDB(sql);
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingPreparedStatement( name,salary );
    }

    private int updateEmployeeDataUsingStatement(String name,double salary){
        String sql = String.format( "update employee_payroll set salary = %.2f where name = '%s';",salary,name );
        try(Connection connection =this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate( sql );
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    private int updateEmployeeDataUsingPreparedStatement(String name,double salary){
        String sql = "update employee_payroll set salary = ? where name = ?";
        try(Connection connection =this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble( 1,salary );
            preparedStatement.setString( 2,name );
            int status =preparedStatement.executeUpdate();
            return status;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.employeePayrollDataStatement == null)
            this.prepareStatementForemployeeData();
        try{
            employeePayrollDataStatement.setString( 1,name );
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData( resultSet );
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try{
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString( "name" );
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData( id,name,salary,startDate ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForemployeeData() {
        try{
            Connection connection = this.getConnection();
            String sql = "Select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement( sql );
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format( "select * from employee_payroll where start between '%s' and '%s';",
                                                                      Date.valueOf(startDate),Date.valueOf(endDate));
        return this.getEmployeeDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getEmployeeDataUsingDB(String sql) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try(Connection connection =this.getConnection()){
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery( sql );
            employeePayrollList = this.getEmployeePayrollData( resultSet );
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public Map<String, Double> getAverageSalaryByGender() {
        String query = "select gender,avg(salary) as avg_salary from employee_payroll group by gender";
        Map<String,Double> genderToAverageSalaryMap = new HashMap<>();
        try(Connection connection = this.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String gender = rs.getString( 1 );
                Double avg = rs.getDouble( 2 );
                genderToAverageSalaryMap.put( gender,avg );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }
}

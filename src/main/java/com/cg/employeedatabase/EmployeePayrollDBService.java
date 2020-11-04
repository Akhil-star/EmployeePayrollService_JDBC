package com.cg.employeedatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EmployeePayrollDBService {
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
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try(Connection connection =this.getConnection()){
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery( sql );
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString( "name" );
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id,name,salary,startDate));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }
}

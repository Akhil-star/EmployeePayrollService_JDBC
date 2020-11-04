package com.cg.employeedatabase;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_err?useSSL=false";
        String userName = "root";
        String password = "Akhilkumar@1";
        Connection connection ;
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
            System.out.println( "Driver Loaded!!" );
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException( "driver not found in the classpath", e );
        }

        listDrivers();
        try {
            System.out.println( "Connecting to the Database " + jdbcURL );
            connection = DriverManager.getConnection( jdbcURL, userName, password );
            System.out.println( "Connection was successful" +connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println( "  " + driverClass.getClass().getName() );
        }
    }
}

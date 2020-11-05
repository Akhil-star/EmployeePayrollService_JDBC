package com.cg.employeedatabase;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.cg.employeedatabase.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollserviceTest {
    
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData( DB_IO);
        Assert.assertEquals(4,employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDataBase() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData( DB_IO);
        employeePayrollService.updateEmployeeSalary("Terissa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInsyncWithDB("Terissa");
        Assert.assertTrue( result );
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData( DB_IO);
        LocalDate startDate = LocalDate.of( 2018,01,01 );
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(DB_IO,startDate,endDate );
        Assert.assertEquals( 4,employeePayrollData.size() );
    }

    @Test
    public void givenPayrollData_WhenAverageSalaryRetrieveByGender_ShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData( DB_IO);
        Map<String,Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue( averageSalaryByGender.get("M").equals( 1500000.00 ) && averageSalaryByGender.get( "F" ).equals( 2250000.00 ) );
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData( DB_IO);
        employeePayrollService.addEmployeeToPayroll("Akhil","M",5000000.00,LocalDate.now());
        boolean result = employeePayrollService.checkEmployeePayrollInsyncWithDB( "Akhil" );
        Assert.assertTrue( result );
    }

    @Test
    public void given6Employees_WhenAddedToDB_ShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                                new EmployeePayrollData( 0,"Jeff Bezos","M",100000.00,LocalDate.now() ),
                                new EmployeePayrollData( 0,"Bill Gates","M",200000.00,LocalDate.now() ),
                                new EmployeePayrollData( 0,"Mark Zuckerberg","M",300000.00,LocalDate.now() ),
                                new EmployeePayrollData( 0,"Sunder","M",600000.00,LocalDate.now() ),
                                new EmployeePayrollData( 0,"Mukesh","M",100000.00,LocalDate.now() ),
                                new EmployeePayrollData( 0,"Anil","M",200000.00,LocalDate.now() )
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData( DB_IO );
        Instant start = Instant.now();
        employeePayrollService.addEmployeesToPayroll( Arrays.asList(arrayOfEmps));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: "+ Duration.between( start,end ) );
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
        Instant threadend = Instant.now();
        System.out.println("Duration with thread: "+Duration.between( threadStart,threadend ));
        Assert.assertEquals( 16,employeePayrollService.countEntries(DB_IO) );
    }
}

package com.cg.employeedatabase;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
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
}

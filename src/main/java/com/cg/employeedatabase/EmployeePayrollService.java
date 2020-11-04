package com.cg.employeedatabase;

import java.util.List;

public class EmployeePayrollService {
    public enum IOService{DB_IO,REST_IO}

    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService(){
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService){
        if(ioService.equals( IOService.DB_IO ))
          this.employeePayrollList = new EmployeePayrollDBService().readData();
        return this.employeePayrollList;
    }
}

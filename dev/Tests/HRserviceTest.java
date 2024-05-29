package Tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import BuisnessLayer.Controller.*;
import BuisnessLayer.Workers.*;
import DTOs.Role;
import ServiceLayer.HRservice;

public class HRserviceTest {

    private HRservice hrs;
    private employeeController empC;
    private HRManager hrManager ;
    private String HRPassword;
    private List<Employee> employees ; 
    private DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    // @Before
    // private void hrBuilder() {
    //     empC = new employeeController();
    //     hrs = new HRservice(empC);
    // }

    //==================================================== init funcs : 

    private void initHRManager(){
        empC = new employeeController();
        hrManager = new HRManager(HRPassword);
        
    }

    private List<Employee> getEmployees(){
        
    }

    //==================================================== getConstrains
    // to check if constrains returned correctly : + 
    // the test will add constrain for employee and check if the HR manager can see
    // it
    @Test
    public void getConstrainsPosTest() {
        
    }


    //if employee 
    @Test
    public void getConstrainsNegTest() {

    }


}

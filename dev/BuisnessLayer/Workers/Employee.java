package BuisnessLayer.Workers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import DTOs.Role;

public class Employee {

    private String empID;
    private String empName;
    private String bankAccount;
    private int hourSalary;
    private int monthSalary;
    private List<Role> roles;
    private Date startDate;
    private Date endDate;

    public Employee(String string, String string2, String string3, int i, int j, List<Role> roles, LocalDate localDate,
                    LocalDate localDate2) {
        //TODO Auto-generated constructor stub
    }

    public String getID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getID'");
    }
}

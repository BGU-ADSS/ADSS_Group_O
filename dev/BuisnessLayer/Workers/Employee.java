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
    private LocalDate startDate;
    private LocalDate endDate;

    public Employee(String string, String string2, String string3, int i, int j, List<Role> roles, LocalDate localDate,
                    LocalDate localDate2) {
        //TODO Auto-generated constructor stub
    }

    public Employee(String string, String string2, String string3, int i, int j, List<Role> roles1, LocalDate start1,
            LocalDate end1, int storeNum) {
        //TODO Auto-generated constructor stub
    }

    public String getID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getID'");
    }

    public Object getRolesSize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRolesSize'");
    }

    public Object getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    public Object getMounthSalary() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMounthSalary'");
    }
}

package BuisnessLayer.Workers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import DTOs.Errors;
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
    private int storeNum; // notify bahaa !!!!!!!!!!!!!!!!!!

    public Employee(String string, String string2, String string3, int i, int j, List<Role> roles, LocalDate localDate,
                    LocalDate localDate2) {
        //TODO Auto-generated constructor stub
    }

    public Employee(String string, String string2, String string3, int i, int j, List<Role> roles1, LocalDate start1,
            LocalDate end1, int storeNum) {
        //TODO Auto-generated constructor stub
    }

    public String getID() {
        return empID;
    }

    public Object getRolesSize() {
        return roles.size();
    }

    public Object getName() {
        return empName;
    }

    public Object getMounthSalary() {
        return monthSalary;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public int getStoreNum(){
        return storeNum;
    }

    public boolean removeRole(Role role) {

        if ( !roles.contains(role) ) {
            throw new IllegalArgumentException("employee does not have role " + role);
        }
        roles.remove(role);
        return true;
    }

    public boolean containsRole(Role role) {
        return roles.contains(role);
    }

    public void addRole(Role role) {
        if(!containsRole(role)) roles.add(role);
    }

    public void setBankAccount(String newBankAccount) {
        if(newBankAccount==null) throw new IllegalArgumentException(Errors.bankAccountIsNull);
        bankAccount= newBankAccount;
    }
}

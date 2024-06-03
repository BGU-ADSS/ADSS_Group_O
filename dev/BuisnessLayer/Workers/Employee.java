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

   

    public Employee(String empId, String empName, String bankAccount, int monthSalary, int hourSalary, List<Role> roles, LocalDate startDate,
            LocalDate enddDate, int storeNum) {
        this.bankAccount=bankAccount;
        this.empID=empId;
        this.empName=empName;
        this.hourSalary = hourSalary;
        this.roles = roles;
        this.monthSalary=monthSalary;
        this.startDate=startDate;
        this.endDate = enddDate;
        this.storeNum=storeNum;
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

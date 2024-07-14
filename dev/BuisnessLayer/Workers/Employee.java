package BuisnessLayer.Workers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import DTOs.Errors;
import DTOs.Role;
import PresentationLayer.Logs;

public class Employee {

    private String empID;
    private String empName;
    private String bankAccount;
    private int hourSalary;
    private int monthSalary;
    private List<Role> roles;
    private LocalDate startDate;
    private LocalDate endDate;
    private int storeNum;
    private String password;// notify bahaa !!!!!!!!!!!!!!!!!!\
    private String defaultPassword = "12345678";
    private LocalDate terminatedDate;

   

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
        this.password = defaultPassword;
    }

    public Employee(String empId, String empName, String bankAccount, int monthSalary, int hourSalary, List<Role> roles, LocalDate startDate,
                    LocalDate enddDate, int storeNum, String password, String terminatedDate) {
        Logs.debug("ans red");
                        this.password = password;
        this.terminatedDate = terminatedDate==null? null: LocalDate.parse(terminatedDate);
        this.bankAccount=bankAccount;
        this.empID=empId;
        this.empName=empName;
        this.hourSalary = hourSalary;
        this.roles = roles;
        this.monthSalary=monthSalary;
        this.startDate=startDate;
        this.endDate = enddDate;
        this.storeNum=storeNum;
        Logs.debug("rami is gay");

    }

    public String getID() {
        return empID;
    }

    public Object getRolesSize() {
        return roles.size();
    }

    public String getName() {
        return empName;
    }

    public int getMounthSalary() {
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
        if(roles.size()==1) throw new IllegalArgumentException(Errors.cantRemoveTheLastRole);
        roles.remove(role);
        return true;
    }

    public boolean containsRole(Role role) {
        return roles.contains(role);
    }

    public void addRole(Role role) {
        if(!containsRole(role)) roles.add(role);
        else throw new IllegalArgumentException("employee already has role " + role);
    }

    public void setBankAccount(String newBankAccount) {
        if(newBankAccount.isEmpty() || newBankAccount == null) throw new IllegalArgumentException(Errors.bankAccountIsNull);
        bankAccount= newBankAccount;
    }

    public void setPassword(String newPassword) {

        if( newPassword == null || newPassword.isEmpty() ){
            throw new IllegalArgumentException("password is null or empty");
        }
        this.password = newPassword;
    }

    public boolean login(String password) {
        if( !this.password.equals(password) ){
            throw new IllegalArgumentException(Errors.InvalidPassword);
        }
        Logs.debug("login successs");
        return true;
    }

    public void updateSalary(int monthSalary) {
        if(monthSalary<0) {
            throw new IllegalArgumentException("salary cannot be negative");
        }
        this.monthSalary = monthSalary;
    }

    public String getProf(){

        String str = "Employee name:" + getName() +"\n";
        str += "Employee ID:" + getID() +"\n";
        str += "Employee Roles:" + roles + "\n";
        str += "Employee Salary:" + monthSalary + "\n";
        str += "Store Number:" + storeNum + "\n";
        str += "Bank Account:" + bankAccount + "\n";
        return str;
    }

    public void terminateJobInDate(LocalDate finishDate) {
        this.terminatedDate = finishDate;
    }

    public Boolean canWorkInShift(LocalDate date){
        if(terminatedDate!=null) return date.isBefore(terminatedDate);
        return true;
    }

    public String getBankAccount(){
        return bankAccount;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public String getPassword(){
        return password;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public LocalDate getTerminatedDate(){
        return terminatedDate;
    }
}

package DataAccessLayer.DTOs;

import java.time.LocalDate;
import java.util.HashMap;

import DataAccessLayer.DBs.EmployeeDB;

public class EmployeeDTO extends DTO {
    
    public String id ;
    public String name;
    public String bankAccount;
    public int monthSalary;
    public String startDate ; 
    public String endDate;
    public int storeId;
    public String password;
    public String terminationDate; 

    

    // Constructor to initialize all fields
    public EmployeeDTO(String id, String name, String bankAccount, int monthSalary, 
                       String startDate, String endDate, int storeId, 
                       String password, String terminationDate) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.monthSalary = monthSalary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.storeId = storeId;
        this.password = password;
        this.terminationDate = terminationDate;
        this.controller = new EmployeeDB();
    }
    public static void main(String[] args){
        EmployeeDTO toTest = new EmployeeDTO("212", "bhaa", "8887", 5002, LocalDate.now().toString(), LocalDate.now().plusYears(1).toString(), 0, "123", null);
        HashMap<String,Object> map = new HashMap<>();
        map.put(EmployeeDB.id_COLUMN, "212");
        toTest.controller.deleteDTO(map);
    }

}

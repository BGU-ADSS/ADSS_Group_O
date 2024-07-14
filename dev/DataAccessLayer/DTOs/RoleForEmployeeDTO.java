package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.RoleForEmployeeDB;

public class RoleForEmployeeDTO extends DTO {
    public String employeeId;
    public String Role ;

    public RoleForEmployeeDTO(String employeeID,String Role){
        this.employeeId = employeeID;
        this.Role = Role;
        this.controller = new RoleForEmployeeDB();
    }

}

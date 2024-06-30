package DataAccessLayer.DBControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataAccessLayer.DBs.AvaliableWorkerInShiftDB;
import DataAccessLayer.DBs.EmployeeDB;
import DataAccessLayer.DBs.RoleForEmployeeDB;
import DataAccessLayer.DBs.ShiftInStoreDB;
import DataAccessLayer.DBs.StoreDB;
import DataAccessLayer.DBs.WorkersInShiftDB;
import DataAccessLayer.DTOs.DTO;
import DataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.DTOs.RoleForEmployeeDTO;
import DataAccessLayer.DTOs.StoreDTO;

public class DBEmployeeController {
    private EmployeeDB employeeDBC = new EmployeeDB();
    private RoleForEmployeeDB rolesDBC = new RoleForEmployeeDB();
    private AvaliableWorkerInShiftDB avaliableWorkersDBC = new AvaliableWorkerInShiftDB();
    private WorkersInShiftDB workersInshiftsDBC = new WorkersInShiftDB();
    private StoreDB storeDBC = new StoreDB();
    private ShiftInStoreDB shiftsDBC = new ShiftInStoreDB();


    public EmployeeDTO[] getEmployeeInTheStore(int storeId){
        DTO[] DTOs= (DTO[]) (employeeDBC.getDTOsWhere(" WHERE "+EmployeeDB.storeId_COLUMN+"="+storeId)).toArray();
        EmployeeDTO[] toRet = (EmployeeDTO[])DTOs;
        return toRet;    
    }

    public RoleForEmployeeDTO[] getRolesForEmployeeDTOs(String empId){
        return (RoleForEmployeeDTO[])(DTO[]) rolesDBC.getDTOsWhere(" WHERE "+RoleForEmployeeDB.emplID_column+"="+empId).toArray();
    }

    public EmployeeDTO getEmployeeFromDB(String id){
        return (EmployeeDTO) employeeDBC.getDTOsWhere(" WHERE "+EmployeeDB.id_COLUMN+ "="+id);
    }

    public void setPasswordInDB(String empID , String newPassword){
        employeeDBC.updateSpecifecColumnForOneRow(getIdentefierMap(empID), EmployeeDB.password_COLUMN, empID, "string");
    }

    public void setBankAccount(String empId,String newBankAccount){
        employeeDBC.updateSpecifecColumnForOneRow(getIdentefierMap(empId), EmployeeDB.bankAccount_COLUMN, newBankAccount, "string");
    }

    public void addRole(String empId,String role){
        if(getEmployeeFromDB(empId)!=null){
            RoleForEmployeeDTO roleToInsert = new RoleForEmployeeDTO(empId, role);
            roleToInsert.persist();
        }
    }

    public void removeRole(String empId,String role){
        HashMap<String,Object> identefier = new HashMap<>();
        identefier.put(RoleForEmployeeDB.emplID_column, empId);
        identefier.put(RoleForEmployeeDB.role_column, role);
        rolesDBC.deleteDTO(identefier);
    }


    public void setTerminationJobToDB(String empId,String date){
        employeeDBC.updateSpecifecColumnForOneRow(getIdentefierMap(empId), EmployeeDB.terminationDate_COLUMN, date, "string");
    }


    public StoreDTO getStoreFromDB(int storeId){
        StoreDTO[] storesWithID =(StoreDTO[]) storeDBC.getDTOsWhere(" WHERE "+StoreDB.id_column+"="+storeId).toArray();
        if(storesWithID.length==0)throw new IllegalArgumentException("there are no store with id: "+storeId);
        return storesWithID[0];
    }

    public int getTheLastIdInShifts(){
        return 0;
    }

    private HashMap<String,Object> getIdentefierMap(String empID){
        HashMap<String,Object> identiferMap = new HashMap<>();
        identiferMap.put(EmployeeDB.id_COLUMN, empID);
        return identiferMap;
    }

}

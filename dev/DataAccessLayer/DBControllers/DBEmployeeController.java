package DataAccessLayer.DBControllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataAccessLayer.DBs.AvaliableWorkerInShiftDB;
import DataAccessLayer.DBs.EmployeeDB;
import DataAccessLayer.DBs.RoleForEmployeeDB;
import DataAccessLayer.DBs.ShiftInStoreDB;
import DataAccessLayer.DBs.StoreDB;
import DataAccessLayer.DBs.WorkersInShiftDB;
import DataAccessLayer.DTOs.*;

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

    public ShiftInStoreDTO[] getShiftsInStore(int storeId,LocalDate from){
        ShiftInStoreDTO minShiftId = shiftsDBC.getMinIdShiftInStore(storeId);
        String minDate = minShiftId.date;
        
        if(from.isBefore(getDateFromString(minDate) )){
            return (ShiftInStoreDTO[])(DTO[]) shiftsDBC.getDTOsWhere(" WHERE "+ShiftInStoreDB.storeId_column+"="+storeId).toArray();
        }else{
            ShiftInStoreDTO[] startShift = (ShiftInStoreDTO[])(DTO[]) shiftsDBC.getDTOsWhere(" WHERE "+ShiftInStoreDB.date_column+"="+from.toString()).toArray();
            int minId = 0;
            for(ShiftInStoreDTO shiftWithGivenDate:startShift)minId = Math.max(shiftWithGivenDate.shiftId, minId);
            return (ShiftInStoreDTO[])(DTO[]) shiftsDBC.getDTOsWhere(" WHERE "+ShiftInStoreDB.shiftId_column+">"+minId).toArray();
        }


    }

    //===========================================================================
    public int getTheLastIdInShifts(int storeId){
        return shiftsDBC.getMaxShiftId();
    }

    public boolean getIsReadyToPublish(int storeId){
        return storeDBC.getSpecifecStore(storeId).readyToPublish;
    }

    public AvaliableWorkerInShiftDTO[] getAvaliableWorkerInShifts(int ShiftId,int storeId){
        return  avaliableWorkersDBC.getAvaliableWorkers(ShiftId,storeId);
    }

    public WorkerInShiftDTO[] getWorkerInShifts(int shiftId,int storId){
        return workersInshiftsDBC.getWorkers(shiftId,storId);
    }

    // return the store that this employee belongs to and throws exception if employee id not exist
    public int getEmployeeStore(String empId){
        EmployeeDTO employee =employeeDBC.getEmployeeWithId(empId);
        return employee.storeId;
    }

    //insert a new employee
    public void intsertEmployee(EmployeeDTO employee){
        employeeDBC.insertDTO(employee);
        
    }

    //delete an employee
    public void deleteEmployeeFromDB(String empId){
        employeeDBC.deleteDTO(getIdentefierMap(empId));
        HashMap<String,Object> roleIDentefierMap = new HashMap<>();
        roleIDentefierMap.put(RoleForEmployeeDB.emplID_column, empId);
        rolesDBC.deleteDTO(roleIDentefierMap);
    }
    //update new salary
    public void updateSalaryForEmployee(String empId, int newSalary){
        employeeDBC.updateSpecifecColumnForOneRow(getIdentefierMap(empId), EmployeeDB.monthSalary_COLUMN, newSalary, "int");
    }
    // set shift for employee
    public void insertEmplInWorkerInShift(WorkerInShiftDTO worker){
        workersInshiftsDBC.insertDTO(worker);
    }
    // update ready to publish
    public void updateReadyToPublish(boolean isReady,int storeId){
        HashMap<String,Object> identefier = new HashMap<>();
        identefier.put(StoreDB.id_column, storeId);
        storeDBC.updateSpecifecColumnForOneRow(identefier, StoreDB.readyToPublih_column, isReady?1:0, "int");
    }
    // delete from available
    public void deleteFromAvailable(int shiftId, String emplI,int storeID){
        HashMap<String,Object> id = new HashMap<>();
        id.put(AvaliableWorkerInShiftDB.empId_column, emplI);
        id.put(AvaliableWorkerInShiftDB.shiftId_column, shiftId);
        id.put(AvaliableWorkerInShiftDB.storeId_column,storeID);
        avaliableWorkersDBC.deleteDTO(id);
    }
    // insert new store
    public void insertStore(StoreDTO stroe){
        storeDBC.insertDTO(stroe);
    }
    //
    public void insertShiftToDB(ShiftInStoreDTO shift){
        shiftsDBC.insertDTO(shift);
    }
    //
    public void insertWorkerAvailableInShift(AvaliableWorkerInShiftDTO shift){
        avaliableWorkersDBC.insertDTO(shift);
    }
    //============================================================================//

    // insert his roles
    public void insertRolesForEmployee(RoleForEmployeeDTO[] role){

        for(RoleForEmployeeDTO roleForEmployeeDTO:role){
            addRole(roleForEmployeeDTO.employeeId,roleForEmployeeDTO.Role);
            }
    }

    private HashMap<String,Object> getIdentefierMap(String empID){
        HashMap<String,Object> identiferMap = new HashMap<>();
        identiferMap.put(EmployeeDB.id_COLUMN, empID);
        return identiferMap;
    }

    private LocalDate getDateFromString(String date){
        int year = Integer.parseInt( date.substring(0,4));
        int mounth = Integer.parseInt( date.substring(5,7));
        int day = Integer.parseInt( date.substring(8,10));
        return LocalDate.of(year, mounth, day);
    }

}

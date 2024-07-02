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
        List<DTO> DTOs=  (employeeDBC.getDTOsWhere(" WHERE "+EmployeeDB.storeId_COLUMN+"="+storeId));
        EmployeeDTO[] toRet = new EmployeeDTO[DTOs.size()];
        for(int i=0;i<toRet.length;i++){
            toRet[i]=(EmployeeDTO)DTOs.get(i);
        }
        return toRet;    
    }

    public RoleForEmployeeDTO[] getRolesForEmployeeDTOs(String empId){
        List<DTO> dtosList =  rolesDBC.getDTOsWhere(" WHERE "+RoleForEmployeeDB.emplID_column+"="+empId);
        RoleForEmployeeDTO[] roles = new RoleForEmployeeDTO[dtosList.size()];
        for(int i=0;i<roles.length;i++) roles[i]=(RoleForEmployeeDTO)dtosList.get(i);
        return roles;
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
        List<DTO> storesWithID = storeDBC.getDTOsWhere(" WHERE "+StoreDB.id_column+"="+storeId);
        if(storesWithID.size()==0)throw new IllegalArgumentException("there are no store with id: "+storeId);
        StoreDTO toRet = (StoreDTO)storesWithID.get(0);
        return toRet;
    }

    public ShiftInStoreDTO[] getShiftsInStore(int storeId,LocalDate from){
        ShiftInStoreDTO minShiftId = shiftsDBC.getMinIdShiftInStore(storeId);
        if(minShiftId==null){
            return new ShiftInStoreDTO[0];
        }
         String minDate = minShiftId.date;
        
        if(from.isBefore(getDateFromString(minDate) )){
            return convertToShiftInStoreDTOs( shiftsDBC.getDTOsWhere(" WHERE "+ShiftInStoreDB.storeId_column+"="+storeId));
        }else{
            List<DTO> startShift = shiftsDBC.getDTOsWhere(" WHERE "+ShiftInStoreDB.date_column+"="+from.toString());
            int minId = 0;
            for(DTO shiftWithGivenDate:startShift)minId = Math.max(((ShiftInStoreDTO)shiftWithGivenDate).shiftId, minId);
            return convertToShiftInStoreDTOs( shiftsDBC.getDTOsWhere(" WHERE "+ShiftInStoreDB.shiftId_column+">"+minId));
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

    private ShiftInStoreDTO[] convertToShiftInStoreDTOs(List<DTO> dtos){
        ShiftInStoreDTO [] toRet = new ShiftInStoreDTO[dtos.size()];
        for(int index = 0; index<toRet.length;index++){
            toRet[index] = (ShiftInStoreDTO)dtos.get(index);
        }
        return toRet;
    }
}

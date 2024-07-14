package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.AvaliableWorkerInShiftDB;
import DataAccessLayer.DBs.WorkersInShiftDB;

public class WorkerInShiftDTO extends DTO {
    public String empId;
    public int shiftId ;
    public int storeId;
    public String role;

    public WorkerInShiftDTO(String empId,int shiftId,int storeId,String role){
        this.controller = new AvaliableWorkerInShiftDB();
        this.empId= empId;
        this.storeId = storeId;
        this.shiftId = shiftId;
        this.role = role;
    }
}

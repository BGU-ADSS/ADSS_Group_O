package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.AvaliableWorkerInShiftDB;
import DataAccessLayer.DBs.WorkersInShiftDB;

public class WorkerInShiftDTO extends DTO {
    public String empId;
    public int shiftId ;

    public WorkerInShiftDTO(String empId,int shiftId){
        this.controller = new AvaliableWorkerInShiftDB();
        this.empId= empId;
        this.shiftId = shiftId;
    }
}

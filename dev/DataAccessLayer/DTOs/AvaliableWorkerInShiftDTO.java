package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.AvaliableWorkerInShiftDB;

public class AvaliableWorkerInShiftDTO extends DTO{
    public String empId;
    public int shiftId;

    public AvaliableWorkerInShiftDTO(String empId,int shiftId){
        this.controller = new AvaliableWorkerInShiftDB();
        this.empId= empId;
        this.shiftId = shiftId;
    }


}

package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.AvaliableWorkerInShiftDB;

public class AvaliableWorkerInShiftDTO extends DTO{
    public String empId;
    public int shiftId;
    public int storeId;

    public AvaliableWorkerInShiftDTO(String empId,int shiftId,int storeId){
        this.controller = new AvaliableWorkerInShiftDB();
        this.empId= empId;
        this.shiftId = shiftId;
        this.storeId = storeId;
    }


}

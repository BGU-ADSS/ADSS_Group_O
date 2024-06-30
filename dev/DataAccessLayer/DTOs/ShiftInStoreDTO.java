package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.ShiftInStoreDB;

public class ShiftInStoreDTO extends DTO {
    public int storeId ;
    public String date ; 
    public String shiftTime;
    public int shiftId ; 
    
    
    public ShiftInStoreDTO(int storeId,String date,String shiftTime,int shiftId){
        this.storeId = storeId;
        this.date= date;
        this.shiftTime = shiftTime;
        this.shiftId= shiftId;
        this.controller = new ShiftInStoreDB();
    }
}

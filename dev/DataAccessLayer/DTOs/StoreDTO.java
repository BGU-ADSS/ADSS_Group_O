package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.StoreDB;

public class StoreDTO extends DTO {
    public int id;
    public String name ;
    public String address;
    public boolean readyToPublish;

    public StoreDTO(int id,String name ,String address,boolean readyToPublish){
        this.id= id;
        this.name=name;
        this.address=address;
        this.readyToPublish =readyToPublish; 
        this.controller = new StoreDB();
    }
}

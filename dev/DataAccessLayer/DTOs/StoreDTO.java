package DataAccessLayer.DTOs;

import DataAccessLayer.DBs.StoreDB;

public class StoreDTO extends DTO {
    public int id;
    public String name ;
    public String address;

    public StoreDTO(int id,String name ,String address){
        this.id= id;
        this.name=name;
        this.address=address;
        this.controller = new StoreDB();
    }
}

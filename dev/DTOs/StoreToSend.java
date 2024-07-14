package DTOs;

import java.util.HashMap;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Workers.Employee;

public class StoreToSend {
    public String name;
    public String address;
    public int storeNumber;
    public StoreToSend(String name,String address,int storeNumber){
        this.name = name;
        this.address = address;
        this.storeNumber = storeNumber;
    }
}

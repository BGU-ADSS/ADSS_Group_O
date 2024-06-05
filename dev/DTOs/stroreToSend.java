package DTOs;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Workers.Employee;

import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class stroreToSend {
    public String name;
    public String address;
    public int storeNumber;
    public LocalDate local;
    public stroreToSend(String name, String address, int storeNumber) {
        this.name = name;
        this.address = address;
        this.storeNumber = storeNumber;
        this.local = LocalDate.now();
        

    }
    public static void main(String[] args){
        stroreToSend s = new stroreToSend("momo","hifa",10);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        String str = gson.toJson(s);
        System.out.println(str);
    }
}

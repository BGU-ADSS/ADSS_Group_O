package DTOs;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Workers.Employee;
import com.google.gson.Gson;

public class stroreToSend {
    public String name;
    public String address;
    public int storeNumber;
    public stroreToSend(String name, String address, int storeNumber) {
        this.name = name;
        this.address = address;
        this.storeNumber = storeNumber;
    }
    public static void main(String[] args){
        stroreToSend s = new stroreToSend("momo","hifa",10);
        Gson gson = new Gson();
        String str = gson.toJson(s);
        System.out.println(str);
    }
}

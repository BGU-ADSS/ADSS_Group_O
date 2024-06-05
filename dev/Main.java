import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.LocalDateAdapter;
import DTOs.Role;
import DTOs.StoreToSend;
import PresentationLayer.presentationController;

public class Main {

    public static void main(String[] args) {
        LocalDate start4 = LocalDate.of(2024, 6, 1);
        LocalDate end4 = LocalDate.of(2024, 7, 1);
        List<Role> roles4 = new ArrayList<>();
        roles4.add(Role.ShiftManager);
        Employee em4 = new Employee("4", "dahleh", "777-55558", 5000, -1, roles4, start4, end4, 1);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        String str = gson.toJson(em4);
        System.out.println(str);
        // System.out.println(gson.toJson(em));
    }
}

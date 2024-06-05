import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.LocalDateAdapter;
import DTOs.Role;
import PresentationLayer.presentationController;

public class Main {

    public static void main(String[] args) {
        // new presentationController().runPresentation();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.Cashier);
        roles.add(Role.GroubManager);
        HRManager s = new HRManager("12345678");
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        String str = gson.toJson(s);
        System.out.println(str);
        // System.out.println(gson.toJson(em));
    }
}

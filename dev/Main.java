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
        new presentationController().runPresentation();
    }
}

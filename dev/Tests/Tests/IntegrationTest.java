package Tests;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import DTOs.Role;
import DTOs.ShiftTime;
import ServiceLayer.ServiceFactory;

public class IntegrationTest {

    private ServiceFactory sf ;

    @Before
    public void setUpTestWithStoreKeeper(){
        sf= new ServiceFactory(false);
        sf.addStore(0, "arraba", "arraba elbatof st");
        sf.addEmployee("1", "abo el ghanem", "123", 1000, new Role[]{Role.Storekeeper}, LocalDate.now(), LocalDate.now().plusYears(1), 0);
        sf.setShift(LocalDate.now(), ShiftTime.Day, "1", Role.Storekeeper);
    }

    @Test
    public void test_use_case_of_storekeeper_adding_category(){
        
    }
    @Test
    public void test_use_case_of_storekeeper_adding_product(){
        
    }
    @Test
    public void test_use_case_of_storekeeper_adding_item(){
        
    }


}

package BuisnessLayer.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BuisnessLayer.Schedule.Schedule;
import DataAccessLayer.DBControllers.DBEmployeeController;
import DataAccessLayer.DTOs.*;
import com.google.gson.Gson;

import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.*;
import PresentationLayer.Logs;

import com.google.gson.GsonBuilder;

public class EmployeeController {

    private HashMap<Integer, Store> stores;
    private HashMap<String, Integer> employeesStore;
    private HRManager hrManager;
    List<LocalDate> breakDays = new ArrayList<>();
    int deadLineConstrains = 0;
    int minEmployees = 0;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    private DBEmployeeController dbEmployeeController = new DBEmployeeController();

    public EmployeeController(File configFile, File dataFile) {
        this.employeesStore = new HashMap<>();
        try {
            List<String> configLines = Files.readAllLines(configFile.toPath());
            for (String line : configLines) {
                if (line.startsWith("#")) {
                    int index = getSplitIndex(line);
                    String key = line.substring(1, index);
                    String value = line.substring(index + 1);
                    switch (key) {
                        case "deadLineConstrains":
                            deadLineConstrains = Integer.parseInt(value);
                            break;
                        case "breakDayes":
                            breakDays.add(LocalDate.parse(value, formatter));
                            break;
                        case "minEmployees":
                            minEmployees = Integer.parseInt(value);
                        case "hrPassword":
                            hrManager = new HRManager(value);
                        default:
                            break;
                    }

                }
            }
            stores = new HashMap();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int getSplitIndex(String line) {
        int index = 0;
        while (line.charAt(index) != '-') {
            index++;
        }
        return index;
    }

    public EmployeeController(File configFile) {
        DBEmployeeController db= new DBEmployeeController();
        db.deleteAllData();
        this.employeesStore = new HashMap<>();
        try {
            List<String> configLines = Files.readAllLines(configFile.toPath());
            for (String line : configLines) {
                if (line.startsWith("#")) {
                    int index = getSplitIndex(line);
                    String key = line.substring(1, index);
                    String value = line.substring(index + 1);
                    switch (key) {
                        case "deadLineConstrains":
                            deadLineConstrains = Integer.parseInt(value);
                            break;
                        case "breakDayes":
                            breakDays.add(LocalDate.parse(value, formatter));
                            break;
                        case "minEmployees":
                            minEmployees = Integer.parseInt(value);
                        default:
                            break;
                    }

                }
            }
            stores = new HashMap();
            employeesStore = new HashMap<>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public EmployeeController(HRManager hrManager) {

        this.hrManager = hrManager;
        stores = new HashMap<Integer, Store>();
        employeesStore = new HashMap<String, Integer>();

    }

    public void addHRmanager(String password) {
        hrManager = new HRManager(password);

    }

    public void saveStore(Store store) {
        dbEmployeeController.insertStore(convertStroreToDTO(store));
    }

    public void loadStore(int storeId) {

        StoreDTO store = dbEmployeeController.getStoreFromDB(storeId);
        EmployeeDTO[] employeesInStore = dbEmployeeController.getEmployeeInTheStore(storeId);
        List<Employee> employees = new ArrayList<>();
        for (EmployeeDTO empl : employeesInStore) {
            RoleForEmployeeDTO[] roles = dbEmployeeController.getRolesForEmployeeDTOs(empl.id);
            List<Role> roleList = new ArrayList<>();
            for (RoleForEmployeeDTO role : roles) {
                roleList.add(Role.valueOf(role.Role));
            }
            employees.add(new Employee(empl.id, empl.name, empl.bankAccount, empl.monthSalary, -1, roleList,
                    LocalDate.parse(empl.startDate, formatter), LocalDate.parse(empl.endDate, formatter), storeId));
        }

        HashMap<LocalDate, Shift[]> shifts = new HashMap<>();
        List<Role> allRoles = new ArrayList<>();
        allRoles.add(Role.Cashier);
        allRoles.add(Role.Storekeeper);
        allRoles.add(Role.Driver);
        allRoles.add(Role.StoreManager);
        allRoles.add(Role.ShiftManager);
        allRoles.add(Role.GroubManager);
        LocalDate lastSunday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        ShiftInStoreDTO[] shiftsInStore = dbEmployeeController.getShiftsInStore(storeId, lastSunday);
        for (int i = 0; i < shiftsInStore.length; i++) {
            WorkerInShiftDTO[] workerInShift = dbEmployeeController.getWorkerInShifts(shiftsInStore[i].shiftId,
                    store.id);
            AvaliableWorkerInShiftDTO[] avaliableWorkerInShift = dbEmployeeController
                    .getAvaliableWorkerInShifts(shiftsInStore[i].shiftId, storeId);
            HashMap<Role, List<Employee>> wis = new HashMap<>();
            HashMap<Role, List<Employee>> awis = new HashMap<>();
            for (Role role : allRoles) {
                wis.put(role, getEmplInRoleW(employees, role, workerInShift));
                awis.put(role, getEmplInRoleA(employees, role, avaliableWorkerInShift));
            }
            LocalDate day = LocalDate.parse(shiftsInStore[i].date);
            if (!shifts.containsKey(day)) {
                Shift[] shiftsInDay = new Shift[2];
                shiftsInDay[0] = new Shift();
                shiftsInDay[1] = new Shift();

                shifts.put(day, shiftsInDay);
            }
            shifts.get(day)[0].loadData(day, ShiftTime.Day, shiftsInStore[i].shiftId, wis, awis);

            shifts.get(day)[1].loadData(day, ShiftTime.Day, shiftsInStore[i].shiftId, wis, awis);

        }
        Schedule schedule = new Schedule();
        schedule.loadData(shifts, lastSunday, lastSunday.plusWeeks(1), breakDays, minEmployees,
                dbEmployeeController.getIsReadyToPublish(storeId),
                dbEmployeeController.getTheLastIdInShifts(storeId) + 1);
        Store finalStore = new Store();
        finalStore.loadData(store.name, store.address, schedule, prepareEmpls(employees), store.id);
        stores.put(store.id, finalStore);
        for (EmployeeDTO empl : employeesInStore) {
            employeesStore.put(empl.id, storeId);
        }

    }

    private HashMap<String, Employee> prepareEmpls(List<Employee> empls) {
        HashMap<String, Employee> emplMap = new HashMap<>();
        for (Employee emp : empls) {
            emplMap.put(emp.getID(), emp);
        }
        return emplMap;
    }

    private List<Employee> getEmplInRoleW(List<Employee> empls, Role role, WorkerInShiftDTO[] workers) {
        List<Employee> employees = new ArrayList<>();
        for (Employee empl : empls) {
            for (WorkerInShiftDTO dto : workers) {
                if (empl.getID().equals(dto.empId) && empl.getRoles().contains(role)) {
                    employees.add(empl);
                }
            }
        }
        return employees;
    }

    private List<Employee> getEmplInRoleA(List<Employee> empls, Role role, AvaliableWorkerInShiftDTO[] workers) {
        List<Employee> employees = new ArrayList<>();
        for (Employee empl : empls) {
            for (AvaliableWorkerInShiftDTO dto : workers) {
                if (empl.getID().equals(dto.empId) && empl.getRoles().contains(role)) {
                    employees.add(empl);
                }
            }
        }
        return employees;
    }

    public void addStore(int storeNumber, String StoreName, String address) {
        if (stores.get(storeNumber) != null) {
            throw new IllegalArgumentException("store number already exist!");
        }
        Store s = new Store(StoreName, address, storeNumber, new ArrayList<>(), deadLineConstrains, minEmployees,
                breakDays);
        stores.put(storeNumber, s);
        Logs.debug("going to add store in db");
        dbEmployeeController.insertStore(convertStroreToDTO(s));
        Logs.debug("store must be added to db");
        saveShifts(s.getSchedule().getCurrentWeek(), s.getSchedule().getDayShifts(), storeNumber);

    }

    public void saveShifts(LocalDate from, HashMap<LocalDate, Shift[]> shifts, int storeId) {

        for (LocalDate day : shifts.keySet()) {
            if (day.isAfter(from) || day.isEqual(from)) {
                Shift[] shif = shifts.get(day);
                dbEmployeeController.insertShiftToDB(convertShiftToDTO(shif[0], storeId));
                dbEmployeeController.insertShiftToDB(convertShiftToDTO(shif[1], storeId));
                for (int i = 0; i < 2; i++) {
                    List<String> emplIds = new ArrayList<>();
                    for (List<Employee> emplS : shif[i].getWorkersAvailable().values()) {
                        for (Employee employee : emplS) {
                            if (!emplIds.contains(employee.getID())) {
                                emplIds.add(employee.getID());
                            }
                        }
                    }
                    for (String id : emplIds) {
                        dbEmployeeController.insertWorkerAvailableInShift(
                                new AvaliableWorkerInShiftDTO(id, shif[i].getId(), storeId));
                    }
                    emplIds = new ArrayList<>();
                    for (List<Employee> emplS : shif[i].getWorkersInShift().values()) {
                        for (Employee employee : emplS) {
                            if (!emplIds.contains(employee.getID())) {
                                emplIds.add(employee.getID());
                            }
                        }
                    }
                    for (String id : emplIds) {
                        dbEmployeeController
                                .insertEmplInWorkerInShift(new WorkerInShiftDTO(id, shif[i].getId(), storeId));
                    }

                }
            }
        }
    }

    public void setStoreForTest(String storeName, String address, Employee manager, List<Employee> employees,
                                int storeNum, int minEmps, int deadLine) {
        Store store = new Store(storeName, address, storeNum, employees, deadLine, minEmps, new ArrayList<>());
        stores.put(storeNum, store);
        for (Employee employee : employees) {
            if (storeNum == employee.getStoreNum())
                employeesStore.put(employee.getID(), storeNum);
        }
    }

    public Store getStoreForTest(int storeName) {
        return stores.get(storeName);
    }

    public boolean loginForHR(String password) {
        return hrManager.login(password);
    }

    public boolean loginForEmployee(String empId, String password) {

        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.loginForEmployee(empId, password);
    }

    public void setPassword(String password, String empId) {

        checkStore(dbEmployeeController.getEmployeeStore((empId)));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        store.setPassword(password, empId);
        dbEmployeeController.setPasswordInDB(empId, password);
    }

    public void addConstrains(String empId, LocalDate day, ShiftTime shift) {

        checkStore(dbEmployeeController.getEmployeeStore(empId));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        int shiftId = store.addConstrains(empId, day, shift);
        dbEmployeeController.deleteFromAvailable(shiftId, empId, storeNum);
    }

    public void addEmployee(Employee employee) {

        checkStore(employee.getStoreNum());
        Logs.debug(employeesStore.get(employee.getID()) + " the store returned for this employee "
                + employeesStore.size());
        if (employeesStore.get(employee.getID()) != null) {
            throw new IllegalArgumentException(Errors.EmployeeAlreadyExistInStore);
        }
        stores.get(employee.getStoreNum()).addEmployee(employee);
        dbEmployeeController.intsertEmployee(convrtEmplToDTO(employee));
        dbEmployeeController.insertRolesForEmployee(rolesToDTO(employee));
        employeesStore.put(employee.getID(), employee.getStoreNum());
        Logs.debug(employeesStore.get(employee.getID()) + " the store returned for this employee "
                + employeesStore.size());
    }

    public String getShiftHistory(LocalDate fromDate, int StoreNum) {

        checkStore(StoreNum);
        if (stores.get(StoreNum) == null) {
            throw new IllegalArgumentException("Store does not exist");
        }
        Store store = stores.get(StoreNum);
        return store.getShiftsHistory(fromDate);
    }

    public boolean terminateJobReq(String empId, LocalDate finishDate) {

        checkStore(dbEmployeeController.getEmployeeStore(empId));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        Store store = stores.get(employeesStore.get(empId));
        if (store.terminateJobReq(empId, finishDate)) {
            dbEmployeeController.setTerminationJobToDB(empId, finishDate.toString());
            return true;
        }
        return false;
    }

    public boolean removeRoleFromEmployee(String empId, Role role) {

        checkStore(dbEmployeeController.getEmployeeStore(empId));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        Store store = stores.get(employeesStore.get(empId));
        if (store.removeRoleFromEmployee(empId, role)) {
            dbEmployeeController.removeRole(empId, role.toString());
            return true;
        }
        return false;
    }

    public String getCurrentWeekSchedule(String empId) {

        checkStore(dbEmployeeController.getEmployeeStore(empId));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.getCurrentWeekSchedule();
    }

    public String getNextWeekSchedule(String empId) {

        checkStore(dbEmployeeController.getEmployeeStore(empId));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.getNextWeekSchedule();
    }

    public void startAddingConstrainsForNextWeek(int storeNum) {

        checkStore(storeNum);
        if (stores.get(storeNum) == null) {
            throw new IllegalArgumentException("Store does not exist");
        }
        Store store = stores.get(storeNum);
        store.startAddingConstrainsForNextWeek();
        saveShifts(store.getNextWeek(), store.getSchedule().getDayShifts(), storeNum);

    }

    public String getEmployeeProf(String empId) {
        System.out.println(dbEmployeeController.getEmployeeStore(empId));
        checkStore(dbEmployeeController.getEmployeeStore(empId));
        if (employeesStore.get(empId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.getEmployeeProf(empId);
    }

    public String getCurrentSchedule(int storeNumber) {

        checkStore(storeNumber);
        Store store = stores.get(storeNumber);
        if (store == null) {
            throw new IllegalArgumentException("Store does not exist");
        }
        return store.getCurrentSchedule();
    }

    // --------------------------------------------------------------------------------------------------//
    public Employee getEmployee(String empId) {
        checkStore(dbEmployeeController.getEmployeeStore(empId));
        Store store = getStoreForEmployee(empId);
        return store != null ? store.getEmployee(empId) : null;
    }

    public HashMap<Integer, Store> getStores() {
        return stores;
    }

    public Store getStore(int numOfStore) {
        return stores.get(numOfStore);
    }

    public Object[] getEmployeeRoles(String empId) {
        Store store = getStoreForEmployee(empId);
        return store.getEmployeeRoles(empId);

    }

    private Store getStoreForEmployee(String empId) {
        if (!containsEmp(empId))
            throw new IllegalArgumentException(Errors.EmployeeNotFound);
        int storeNumber = employeesStore.get(empId);
        return stores.get(storeNumber);
    }

    public Boolean containsEmp(String empId) {
        Boolean containsInEmpDict = employeesStore.get(empId) != null;
        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        Boolean contatinsInStore = store.containsEmp(empId);
        return containsInEmpDict && contatinsInStore;
    }

    public List<Shift> getAvailableDaysForEmployee(String empId) {

        int storeNum = dbEmployeeController.getEmployeeStore(empId);
        checkStore(storeNum);
        int storeNumOfEmployee = employeesStore.get(empId);
        Store storeOfEmployee = stores.get(storeNumOfEmployee);
        return storeOfEmployee.getAvailableDaysForEmployee(empId);

    }

    public boolean removeEmployee(String empId) {
        checkStore(dbEmployeeController.getEmployeeStore(empId));
        Store store = getStoreForEmployee(empId);
        store.removeEmployee(empId);
        employeesStore.remove(empId);
        dbEmployeeController.deleteEmployeeFromDB(empId);
        return true;
    }

    // to check if we have to add hour
    // public void addConstrainsDeadLine(LocalDate date){
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getEmployeeRoles'");
    // }

    //
    // addBreakDay()
    //

    public void setEmployeeInShift(LocalDate date, ShiftTime shiftTime, String empId, Role role) {
        checkStore(dbEmployeeController.getEmployeeStore(empId));
        Store store = getStoreForEmployee(empId);
        int id = store.setEmployeeInShift(date, shiftTime, empId, role);
        dbEmployeeController.insertEmplInWorkerInShift(new WorkerInShiftDTO(empId, id, store.getStoreNumber()));
    }

    public boolean addRoleForEmployee(String empId, Role role) {
        checkStore(dbEmployeeController.getEmployeeStore(empId));
        Store store = getStoreForEmployee(empId);
        Logs.debug(empId + " employee id to add in");
        store.addRoleForEmployee(empId, role);
        dbEmployeeController.addRole(empId, role.toString());
        return true;
    }

    public void setBankAccountForEmployee(String empId, String newBankAccount) {
        checkStore(dbEmployeeController.getEmployeeStore(empId));
        Store store = getStoreForEmployee(empId);
        store.setBankAccountForEmployee(empId, newBankAccount);
        dbEmployeeController.setBankAccount(empId, newBankAccount);
    }

    public void updateSalary(String emplId, int monthSalary) {
        if (monthSalary < 0)
            throw new IllegalArgumentException(Errors.cantUpdateNegativeSalary);
        if (employeesStore.get(emplId) == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        Store store = stores.get(employeesStore.get(emplId));
        store.updateSalary(emplId, monthSalary);
        dbEmployeeController.updateSalaryForEmployee(emplId, monthSalary);
    }

    public void scheduleReadyToPublish(int StoreNumber) {
        checkStore(StoreNumber);
        Store store = stores.get(StoreNumber);
        if (store == null) {
            throw new IllegalArgumentException("Store does not exist");
        }
        store.scheduleReadyToPublish();
        dbEmployeeController.updateReadyToPublish(false, StoreNumber);
    }

    public void checkStore(int StoreNumber) {

        Store store = stores.get(StoreNumber);
        if (store == null) {
            loadStore(StoreNumber);
        }
    }

    public EmployeeDTO convrtEmplToDTO(Employee employee) {
        return new EmployeeDTO(employee.getID(), employee.getName(), employee.getBankAccount(),
                employee.getMounthSalary(), employee.getStartDate().toString(), employee.getEndDate().toString(),
                employee.getStoreNum(), employee.getPassword(),
                employee.getTerminatedDate() == null ? null : employee.getTerminatedDate().toString());
    }

    public RoleForEmployeeDTO[] rolesToDTO(Employee employee) {
        RoleForEmployeeDTO[] dtos = new RoleForEmployeeDTO[employee.getRoles().size()];
        for (int i = 0; i < employee.getRoles().size(); i++) {
            dtos[i] = new RoleForEmployeeDTO(employee.getID(), employee.getRoles().get(i).toString());
        }
        return dtos;
    }

    public StoreDTO convertStroreToDTO(Store store) {
        return new StoreDTO(store.getStoreNumber(), store.getName(), store.getAddress(), store.isReadyToPublish());
    }

    public ShiftInStoreDTO convertShiftToDTO(Shift shift, int id) {
        return new ShiftInStoreDTO(id, shift.getDay().toString(), shift.getShiftTime().toString(), shift.getId());
    }
}
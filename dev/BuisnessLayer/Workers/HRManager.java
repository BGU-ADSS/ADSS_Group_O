package BuisnessLayer.Workers;

public class HRManager {

    private String password;

    public HRManager(String hRPassword) {
        if(hRPassword.isEmpty() || hRPassword == null){
            throw new IllegalArgumentException("password cannot be null or empty");
        }
        this.password = hRPassword;
    }

    public boolean login(String password) {
        if (password.equals(this.password)) {
            return true;
        }
        throw new IllegalArgumentException("incorrect password!");
    }
}

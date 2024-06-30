package DataAccessLayer.DTOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataAccessLayer.DAOs.Controller;

public  class DTO {
    
    private Controller controller ;


    public  void  persist(){
        controller.insertDTO(this);
    }

   
}

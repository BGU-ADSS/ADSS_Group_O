package DataAccessLayer.DTOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataAccessLayer.DBs.DB;

public  class DTO {
    
    protected DB controller ;


    public  void  persist(){
        controller.insertDTO(this);
    }

   
}

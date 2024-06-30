package DataAccessLayer.DBs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import DataAccessLayer.DTOs.DTO;
import DataAccessLayer.DTOs.StoreDTO;
import PresentationLayer.Logs;

public class StoreDB extends DB{

    public static final String id_column = "ID";
    public static final String adress_column = "Adress";
    public static final String name_column = "NAME";

    public StoreDB(){
        this.tableName="STORE_TABLE";
    }


    @Override
    public DTO getObjectDTOFromOneResult(ResultSet result) {
        try {
            return new StoreDTO(result.getInt(id_column), result.getString(name_column), result.getString(name_column));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert) {
        try {
            pstmt.setInt(1, ((StoreDTO)toInsert).id);
            pstmt.setString(2, ((StoreDTO)toInsert).name);
            pstmt.setString(3, ((StoreDTO)toInsert).address);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getTheRestOfInsertQuery(DTO toInsert) {
        return "(+"+id_column+","+name_column+","+adress_column+"+) VALUES (?,?,?)";
    }

    @Override
    public void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,
            PreparedStatement pstmt, int index) {
        if(toDelIdentiferMap.containsKey(id_column)){
            try {
                pstmt.setString(index, (String)toDelIdentiferMap.get(id_column));
                Logs.debug(pstmt.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new IllegalArgumentException("there is no 'ID' to the DTO");
        }
    }

    @Override
    public String buildWhereQuery() {
        return " WHERE "+id_column+"=?";
    }
    
}

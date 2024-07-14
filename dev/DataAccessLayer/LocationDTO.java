package DataAccessLayer;

import BusinessLayer.Objects.Location;

import java.time.LocalDate;

public class LocationDTO {
    private int locationID;
    private String description;
    private int section;
    private int shelf;
    public LocationDTO(int locationID, String description, int section, int shelf){
        this.locationID = locationID;
        this.description = description;
        this.section = section;
        this.shelf = shelf;
    }
    public int getLocationID(){
        return locationID;
    }
    public String getDescription(){
        return description;
    }
    public int getSection(){
        return section;
    }
    public int getShelf(){
        return shelf;
    }
}

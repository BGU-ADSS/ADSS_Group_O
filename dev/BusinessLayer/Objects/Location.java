package BusinessLayer.Objects;

public class Location {
    private String description;
    private int section;
    private int shelf;
    public Location(String description,int section,int shelf){
        this.description = description;
        this.section = section;
        this.shelf = shelf;
    }
    public String toString(){
        return this.description + " " + this.section + " " + this.shelf + "\n";
    }
}

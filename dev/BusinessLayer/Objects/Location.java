package BusinessLayer.Objects;


public class Location {
    private int id;
    private String description;
    private int section;
    private int shelf;
    public Location(int id,String description,int section,int shelf){
        this.id=id;
        this.description = description;
        this.section = section;
        this.shelf = shelf;
    }
    public String toString(){
        return this.description + " " + this.section + " " + this.shelf + "\n";
    }

    public int getId() {
        return id;
    }

}

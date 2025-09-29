package supperSolver.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_recipe")
public class MRecipe
{
    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    //User Object
    @ManyToOne
    @JoinColumn(name="user")
    private MUser user;

    //Name of Recipe
    @Column
    private String name;

    //Description of Recipe
    @Column(name="description")
    private String description;

    //Empty Constructor
    public MRecipe(){ }

    //MRecipe Object Constructor
    public MRecipe(int ID, MUser user, String name, String description)
    {
        this.ID = ID;
        this.user = user;
        this.description = description;
    }

    //Getter for ID
    public int getID(){
        return ID;
    }
    public void setID(int ID) { this.ID = ID; }

    //Getter and Setter for Name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    //Getter and Setter for User Object
    public MUser getUser() {
        return user;
    }
    public void setUser(MUser user){
        this.user = user;
    }

    //Getter and Setter for Description
    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
package supperSolver.Models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import supperSolver.Repositories.RRecipe;
import supperSolver.Repositories.RUser;

@Entity
@Table(name = "tbl_ingredient")
public class MIngredient
{
    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;

    //User Object
    @ManyToOne
    @JoinColumn(name = "user")
    public MUser user;

    //Recipe Object
    @ManyToOne
    @JoinColumn(name = "recipe")
    public MRecipe recipe;

    //Name Value
    @Column(name = "name")
    public String name;

    //Quantity Value
    @Column(name = "quantity")
    public double quantity;

    //Empty Constructor
    public MIngredient() { }

    //Ingredient Constructor
    public MIngredient(MUser user, MRecipe recipe, String name, double quantity)
    {
        this.user = user;
        this.recipe = recipe;
        this.name = name;
        this.quantity = quantity;
    }

    //Getter for ID
    public int getId() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    //Getter and Setter for User Object
    public MUser getUser() { return user; }
    public void setUser(MUser user) { this.user = user; }

    //Getter and Setter for Recipe Object
    public MRecipe getRecipe() { return recipe; }
    public void setRecipe(MRecipe recipe) { this.recipe = recipe; }

    //Getter and Setter for Name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    //Getter and Setter for Quantity
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}
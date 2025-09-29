package supperSolver.Models;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_rating")

public class MRating
{
    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    //Rating value (1-5)
    @Column(name = "rating")
    private int rating;

    //User Object
    @ManyToOne
    @JoinColumn(name = "user")
    private MUser user;

    //Recipe Object
    @ManyToOne
    @JoinColumn(name = "recipe")
    private MRecipe recipe;

    //Empty Constructor
    public MRating() { }

    //Rating Object Constructor
    public MRating(MUser user, int rating, MRecipe recipe)
    {
        this.rating = rating;
        this.user = user;
        this.recipe = recipe;
    }

    //Getter for ID
    public int getID(){
        return ID;
    }
    public void setID(int ID) { this.ID = ID; }

    //Getter and Setter for Rating
    public int getRating(){
        return rating;
    }
    public void setRating(int rating){
        if(rating < 1 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    //Getter and Setter for Recipe Object
    public MRecipe getRecipe() {
        return recipe;
    }
    public void setRecipe(MRecipe recipe) { this.recipe = recipe; }

    //Getter and Setter for User Object
    public MUser getUser(){
        return user;
    }
    public void setUser(MUser user){
        this.user = user;
    }
}
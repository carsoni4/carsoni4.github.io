package supperSolver.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_comment")
public class MComment
{
    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;

    //Recipe Object Joining
    @ManyToOne
    @JoinColumn(name = "recipe", nullable = false)
    public MRecipe recipe;

    //User Object Joining
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    public MUser user;

    //Actual comment text
    @Column(name = "comment")
    public String comment;

    //Empty Constructor
    public MComment() { }

    //Comment Object with full Constructor
    public MComment(MRecipe recipe, MUser user, String comment)
    {
        this.recipe = recipe;
        this.user = user;
        this.comment = comment;
    }

    //Getter for ID
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    //Getter and Setter for Recipe
    public MRecipe getRecipe() { return recipe; }
    public void setRecipe(MRecipe recipe) { this.recipe = recipe; }

    //Getter and Setter for User
    public MUser getUser() { return user; }
    public void setUser(MUser user) { this.user = user; }

    //Getter and Setter for comment text
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
package supperSolver.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_image")
public class MImage
{

    //Auto Generate ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    //Image URl
    @Column(name = "imgUrl")
    private String imgUrl;

    //Image Position in Recipe/User
    @Column(name = "imagePos")
    private int imagePos;

    //RecipeID if in a recipe (0 if otherwise)
    @Column(name = "recipeID")
    private int recipeID;

    //userID if for a user (0 if otherwise)
    @Column(name = "userID")
    private int userID;

    //Empty Constructor
    public MImage(){}

    //Constructor for MImage Object
    public MImage(int ID, String imgUrl, int imagePos, int recipeID, int userID)
    {
        this.ID = ID;
        this.imagePos = imagePos;
        this.imgUrl = imgUrl;
        this.recipeID = recipeID;
        this.userID = userID;
    }

    //Getter for ID
    public int getID(){return ID;}

    //Getter and Setter for the Image URL
    public String getImgUrl() {return imgUrl;}
    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}

    //Getter and Setter for Image Position
    public int getImagePos() {return imagePos;}
    public void setImagePos(int imagePos) {this.imagePos = imagePos;}

    //Getter and Setter for RecipeID
    public int getRecipeID() {return recipeID;}
    public void setRecipeID(int recipeID) {this.recipeID = recipeID;}

    //Getter and Setter for UserID
    public int getUserID(){return userID;}
    public void setUserID(int userID){this.userID = userID;}

    public void setID(int imageID) { this.ID = imageID;}
}
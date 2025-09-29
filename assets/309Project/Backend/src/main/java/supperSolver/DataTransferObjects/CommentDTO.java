package supperSolver.DataTransferObjects;

public class CommentDTO
{
    private int recipeID;
    private int userID;
    private String comment;

    public int getRecipeID() { return recipeID; }
    public void setRecipeID(int recipeID) { this.recipeID = recipeID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

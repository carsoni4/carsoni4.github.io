package supperSolver.DataTransferObjects;

public class RatingDTO
{
    private int rating;
    private int userID;
    private int recipeID;

    public void setRating(int ratingIN) { this.rating = ratingIN; }
    public void setUserID(int userIN) { this.userID = userIN; }
    public void setRecipeID(int recipeIN) { this.recipeID = recipeIN; }
    public int getRating() { return rating; }
    public int getUserID() { return userID; }
    public int getRecipeID() { return recipeID; }
}

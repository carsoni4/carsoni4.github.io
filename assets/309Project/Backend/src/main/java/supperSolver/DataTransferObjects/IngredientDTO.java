package supperSolver.DataTransferObjects;

public class IngredientDTO
{
    private int userID;
    private int recipeID;
    private String name;
    private double quantity;

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getRecipeID() { return recipeID; }
    public void setRecipeID(int recipeID) { this.recipeID = recipeID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}

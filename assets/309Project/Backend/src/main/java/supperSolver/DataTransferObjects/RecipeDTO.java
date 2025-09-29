package supperSolver.DataTransferObjects;

public class RecipeDTO
{
    private String description;
    private String name;
    private int userID;

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public int getUserID() { return this.userID; }
    public void setUserID(int userID) { this.userID = userID; }
}

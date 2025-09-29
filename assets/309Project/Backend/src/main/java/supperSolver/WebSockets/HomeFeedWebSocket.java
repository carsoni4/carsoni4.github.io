package supperSolver.WebSockets;

import java.io.IOException;
import java.util.*;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import supperSolver.Controllers.AdvertisementController;
import supperSolver.Controllers.RecipeController;
import supperSolver.Models.MAdvertisement;
import supperSolver.Models.MRecipe;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RUser;
import supperSolver.Controllers.ImageController;
import supperSolver.Models.MImage;
import supperSolver.Repositories.RImage;

@Controller
@ServerEndpoint("/wshomefeed/{userID}")
public class HomeFeedWebSocket
{
    private static RecipeController recipeController;
    private static RUser rUser;
    private static ImageController imageController;
    private static RImage rImage;
    private static AdvertisementController advertisementController;

    @Autowired
    public void setRecipeController(RecipeController recipeController)
    {
        this.recipeController = recipeController;
    }

    @Autowired
    public void setUserRepository(RUser rUser)
    {
        this.rUser = rUser;
    }

    @Autowired
    public void setImageRepository(RImage rImage)
    {
        this.rImage = rImage;
    }

    @Autowired
    public void setImageController(ImageController imageController)
    {
        this.imageController = imageController;
    }

    @Autowired
    public void setAdvertisementController(AdvertisementController advertisementController) { this.advertisementController = advertisementController; }

    private static final Logger logger = LoggerFactory.getLogger(HomeFeedWebSocket.class);
    private static final Map<Integer, List<MRecipe>> userRecipes = new Hashtable<>();
    private static final Map<Integer, List<MAdvertisement>> userAdvertisements = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userID") int userID) throws IOException
    {
        logger.info("[onOpen] Connection opened for userID: " + userID);
        userRecipes.putIfAbsent(userID, new ArrayList<>());
        userAdvertisements.putIfAbsent(userID, new ArrayList<>());
        nextRecipe(userID, session);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userID") int userID) throws IOException
    {
        logger.info("[onMessage] Message from userID " + userID + ": " + message);

        if ("scrolled".equals(message))
            nextRecipe(userID, session);

        if ("advertisement".equals(message))
            nextAdvertisement(userID, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userID") int userID)
    {
        logger.info("[onClose] Connection closed for userID: " + userID);
        userRecipes.remove(userID);
        userAdvertisements.remove(userID);
    }

    // Helper method to fetch more recipes for the user
    private void nextRecipe(int userID, Session session) throws IOException
    {
        List<MRecipe> recipes = userRecipes.get(userID);

        MRecipe recipe = recipeController.recipeAlgorithm(userID, recipes);
        sendRecipe(session, recipe);

        recipes.add(recipe);
        userRecipes.put(userID, recipes);
    }

    // Helper method to fetch another advertisement
    private void nextAdvertisement(int userID, Session session) throws IOException
    {
        List<MAdvertisement> currentAdvertisements = userAdvertisements.get(userID);
        List<MAdvertisement> allAdvertisements = advertisementController.getAdvertisements();

        boolean sentAdvertisement = false;

        for (MAdvertisement advertisement : allAdvertisements)
        {
            if (currentAdvertisements.stream().noneMatch(a -> a.getID() == advertisement.getID()))
            {
                sentAdvertisement = true;
                sendAdvertisement(session, advertisement);
                currentAdvertisements.add(advertisement);
                userAdvertisements.put(userID, currentAdvertisements);
                break;
            }
        }

        if (!sentAdvertisement)
            sendAdvertisement(session, null);
    }

    // Helper method to send recipe to the client
    private void sendRecipe(Session session, MRecipe recipe) throws IOException
    {
        if(recipe == null)
            session.getBasicRemote().sendText("No more recipes in database");
        else
        {
            String recipeJson = convertRecipeToJson(recipe);
            session.getBasicRemote().sendText(recipeJson);
        }
    }

    // Helper method to send advertiser to client
    private void sendAdvertisement(Session session, MAdvertisement advertisement) throws IOException
    {
        if(advertisement == null)
            session.getBasicRemote().sendText("No more advertisements in database");
        else
        {
            String advertisementJson = convertAdvertisementToJson(advertisement);
            session.getBasicRemote().sendText(advertisementJson);
        }
    }

    // Helper method to convert the list of recipes to JSON
    private String convertRecipeToJson(MRecipe recipe) throws IOException
    {
        StringBuilder json = new StringBuilder();

        json.append("{");
        json.append("\"ID\":").append(recipe.getID()).append(",");
        json.append("\"name\":\"").append(recipe.getName()).append("\",");
        json.append("\"description\":\"").append(recipe.getDescription()).append("\",");
        json.append("\"user\":").append(convertUserToJson(recipe.getUser())).append(",");
        json.append("\"imgURL\":\"").append(getRecipeImage(recipe)).append("\",");
        json.append("\"type\":\"recipe\"");
        json.append("}");

        return json.toString();
    }

    private String convertAdvertisementToJson(MAdvertisement advertisement)
    {
        StringBuilder json = new StringBuilder();

        json.append("{");
        json.append("\"ID\":").append(advertisement.getID()).append(",");
        json.append("\"imgURL\":\"").append(advertisement.getImageURL()).append("\",");
        json.append("\"user\":").append(convertUserToJson(advertisement.getAdvertiser())).append(",");
        json.append("\"description\":\"").append(advertisement.getDescription()).append("\",");
        json.append("\"type\":\"advertisement\"");
        json.append("}");

        return json.toString();
    }

    // Helper method to convert user to JSON
    private String convertUserToJson(MUser user)
    {

        StringBuilder json = new StringBuilder();

        json.append("{");
        json.append("\"ID\": ").append(user.getID()).append(",");
        json.append("\"name\": \"").append(user.getName()).append("\",");
        json.append("\"username\": \"").append(user.getUsername()).append("\",");
        json.append("\"password\": \"").append(user.getPassword()).append("\",");
        json.append("\"isAdmin\": ").append(user.getIsAdmin()).append(",");
        json.append("\"isAdvertiser\": ").append(user.getIsAdvertiser()).append(",");
        json.append("\"bio\": \"").append(user.getBio()).append("\",");
        //TODO MAKE SURE IT IS FINE I REMOVED THIS (User no have image id anymore)
        // json.append("\"imageID\": ").append(user.getImage()).append(",");
        json.append("\"groupID\": ").append(user.getGroupID());
        json.append("}");

        return json.toString();
    }

    // Helper method to get the front image for a recipe
    private String getRecipeImage(MRecipe recipe)
    {
        List<MImage> images = imageController.findByRecipe(recipe.getID());

        for(MImage image : images)
        {
            if(image.getImagePos() == 0)
                return image.getImgUrl();
        }
        return "";
    }
}

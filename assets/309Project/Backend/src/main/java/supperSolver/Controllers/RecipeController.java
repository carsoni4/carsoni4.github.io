package supperSolver.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import supperSolver.Models.MRating;
import supperSolver.Models.MRecipe;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RRating;
import supperSolver.Repositories.RRecipe;
import supperSolver.DataTransferObjects.RecipeDTO;
import supperSolver.Repositories.RUser;

import java.util.*;

@RestController
public class RecipeController
{

    @Autowired
    private RRating rRating;

    @Autowired
    private RRecipe rRecipe;

    @Autowired
    private RUser rUser;

    @Autowired
    private CommentController commentController;

    @Autowired
    private ImageController imageController;

    @Autowired
    private IngredientController ingredientController;

    @Autowired
    private RatingController ratingController;

    // Algorithm that gets 6 recipes based off the ingredients the user has
    public MRecipe recipeAlgorithm(int userID, List<MRecipe> recipeArray)
    {
        MUser mUser = rUser.findById(userID).get();
        List<Integer> recipeIDs = rRecipe.algorithm(mUser);

        // First try to fill the array with 6 recipes based on the algorithm
        for (Integer recipeID : recipeIDs)
        {
            // Ensure the recipe isn't already in recipeArray
            Optional<MRecipe> recipeOpt = getRecipe(recipeID);
            if (recipeOpt.isPresent())
            {
                MRecipe newRecipe = recipeOpt.get();
                boolean isDuplicate = recipeArray.stream().anyMatch(r -> r.getID() == newRecipe.getID());

                if (!isDuplicate)
                    return rRecipe.findById(recipeID).get();
            }
        }

        // If we still need more recipes, add some random ones from the database
        List<MRecipe> randomRecipes = rRecipe.findAll();
        for (MRecipe randomRecipe : randomRecipes)
        {
            boolean isDuplicate = recipeArray.stream().anyMatch(r -> r.getID() == randomRecipe.getID());

            if (!isDuplicate)
                return randomRecipe;
        }

        return null;
    }

    @Operation(summary = "Gets List of Recipes created by a given User")
    @GetMapping("/recipe/usr/{userID}")
    public @ResponseBody List<MRecipe> getRecipeByUserID(@PathVariable int userID)
    {
        return rRecipe.findByUserID(userID);
    }

    @Operation(summary = "Returns a list of all recipes")
    @GetMapping("recipes")
    public @ResponseBody List<MRecipe> getAllRecipes()
    {
        return rRecipe.findAll();
    }

    @Operation(summary = "Returns a recipe given a recipeID")
    @GetMapping("/recipe/{recipeID}")
    public @ResponseBody Optional<MRecipe> getRecipe(@PathVariable int recipeID)
    {
       return rRecipe.findById(recipeID);
    }

    @Operation(summary = "Gets a list of recipes by searching for Recipes with a name similar to the name parameter in the MySQL server")
    @GetMapping("/recipe/searchByName/{name}")
    public @ResponseBody List<MRecipe> searchForRecipeByName(@PathVariable String name)
    {
        return rRecipe.findByNameLike(name);
    }

    @Operation(summary = "Gets a list of recipes by searching for Recipes with a name similar to the name parameter in the MySQL server then sorts them by average rating")
    @GetMapping("/recipe/searchByAvgRating/{name}")
    public @ResponseBody List<MRecipe> searchSortedByRating(@PathVariable String name)
    {
        List<MRecipe> list = rRecipe.findByNameLike(name);
        Map<MRecipe, Integer> recipeRatings = new HashMap<>();

        for(MRecipe m : list)
        {
            List<MRating> allRatingsForRecipe = rRating.findByRecipeID(m.getID());
            int avg = 0;

            //loops through and adds all the ratings
            if(!allRatingsForRecipe.isEmpty())
            {
                for (MRating mRating : allRatingsForRecipe)
                    avg += mRating.getRating();

                //divides by size of list to get average
                avg /= allRatingsForRecipe.size();
            }

            recipeRatings.put(m, avg);
        }

        list.sort((r1,r2) -> recipeRatings.get(r2) - recipeRatings.get(r1));

        return list;
    }

    @Operation(summary = "Creates a new recipe object on Post")
    @PostMapping("/recipe")
    public @ResponseBody MRecipe createRecipe(@RequestBody RecipeDTO recipeDTO)
    {
        MUser user = rUser.findById(recipeDTO.getUserID()).get();

        MRecipe recipe = new MRecipe();
        recipe.setName(recipeDTO.getName());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setUser(user);

        return rRecipe.save(recipe);
    }

    @Operation(summary = "Updates an existing recipe by unique ID")
    @PutMapping("/recipe/{recipeID}")
    public MRecipe updateRecipe(@PathVariable int recipeID, @RequestBody MRecipe r)
    {
        Optional<MRecipe> u = rRecipe.findById(recipeID);

        if (u.isPresent())
        {
            MRecipe oldR = u.get();
            oldR.setDescription(r.getDescription());
            oldR.setUser(r.getUser());
            oldR.setName(r.getName());

            return rRecipe.save(oldR);
        }
        else
        {
            throw new IllegalArgumentException("Recipe ID " + recipeID + " not found");
        }
    }

    @Operation(summary = "Deletes a recipe by its unique ID")
    @DeleteMapping("/recipe/{recipeID}")
    public String deleteRecipe(@PathVariable int recipeID)
    {
        Optional<MRecipe> recipe = rRecipe.findById(recipeID);
        if (recipe.isPresent())
        {
            // Delete everything belonging to recipe
            commentController.deleteCommentsByRecipe(recipeID);
            imageController.deleteImagesByRecipe(recipeID);
            ingredientController.deleteIngredientsByRecipe(recipeID);
            ratingController.deleteRatingsByRecipe(recipeID);

            rRecipe.deleteById(recipeID);
            return "Recipe: " + recipeID + " has been deleted!";
        }
        return "Recipe: " + recipeID + " does not exist.";
    }

    @Operation(summary = "Deletes recipes by userID")
    @DeleteMapping("recipes/userID/{userID}")
    public void deleteRecipesByUser(@PathVariable("userID") int userID)
    {
        List<MRecipe> recipes = rRecipe.findByUserID(userID);
        if (recipes != null && !recipes.isEmpty())
        {
            for (MRecipe recipe : recipes)
                deleteRecipe(recipe.getID());
        }
    }
}
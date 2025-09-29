package supperSolver.Controllers;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import supperSolver.Models.MComment;
import supperSolver.Models.MIngredient;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RIngredient;
import supperSolver.Repositories.RRecipe;
import supperSolver.Repositories.RUser;
import supperSolver.Repositories.RIngredient;
import supperSolver.DataTransferObjects.IngredientDTO;

@RestController
public class IngredientController
{
    @Autowired
    RIngredient rIngredient;

    @Autowired
    RUser rUser;

    @Autowired
    RRecipe rRecipe;

    @Autowired
    private GroupController groupController;

    @Operation(summary = "Gets ingredients by groupID")
    @GetMapping("ingredients/groupID/{groupID}")
    public List<MIngredient> GetIngredientsByGroup(@PathVariable int groupID)
    {
        Map<String, MIngredient> ingredientMap = new HashMap<>();
        List<MUser> users = groupController.getGroupUsers(groupID);

        for (MUser user : users)
        {
            List<MIngredient> userIngredients = GetIngredientsByUser(user.getID());

            for (MIngredient ingredient : userIngredients)
            {
                if (ingredientMap.containsKey(ingredient.getName()))
                {
                    MIngredient existingIngredient = ingredientMap.get(ingredient.getName());
                    existingIngredient.setQuantity(existingIngredient.getQuantity() + ingredient.getQuantity());
                }
                else
                {
                    ingredientMap.put(ingredient.getName(), ingredient);
                }
            }
        }

        return new ArrayList<>(ingredientMap.values());
    }

    @Operation(summary = "Gets list of ingredients based on userID")
    @GetMapping("/ingredients/user/{userID}")
    public List<MIngredient> GetIngredientsByUser(@PathVariable int userID)
    {
        return rIngredient.findByUserID(userID).get();
    }

    @Operation(summary = "Gets list of ingredients based on RecipeID")
    @GetMapping("/ingredients/recipe/{recipeID}")
    public List<MIngredient> GetIngredientsByRecipe(@PathVariable int recipeID)
    {
        return rIngredient.findByRecipeID(recipeID).get();
    }

    @Operation(summary = "Gets a single ingredient by its unique ID")
    @GetMapping("/ingredients/{id}")
    public MIngredient GetIngredientByID(@PathVariable("id") int id)
    {
        return rIngredient.findById(id).get();
    }

    @Operation(summary = "Creates a new ingredient object on Post")
    @PostMapping("/ingredients")
    public MIngredient CreateIngredient(@RequestBody IngredientDTO ingredient)
    {
        MIngredient newIngredient = new MIngredient();

        if(ingredient.getUserID() == -1)
            newIngredient.setUser(null);
        else
            newIngredient.setUser(rUser.findById(ingredient.getUserID()).get());

        if(ingredient.getRecipeID() == -1)
            newIngredient.setRecipe(null);
        else
            newIngredient.setRecipe(rRecipe.findById(ingredient.getRecipeID()).get());

        newIngredient.setName(ingredient.getName());
        newIngredient.setQuantity(ingredient.getQuantity());
        
        return rIngredient.save(newIngredient);
    }

    @Operation(summary = "Updates an existing ingredient object")
    @PutMapping("/ingredients/{id}")
    public ResponseEntity<MIngredient> UpdateIngredient(@PathVariable("id") int id, @RequestBody MIngredient ingredient)
    {
        MIngredient existingIngredient = rIngredient.findById(id).get();

        existingIngredient.setUser(ingredient.getUser());
        existingIngredient.setRecipe(ingredient.getRecipe());
        existingIngredient.setName(ingredient.getName());
        existingIngredient.setQuantity(ingredient.getQuantity());

        rIngredient.save(existingIngredient);
        return ResponseEntity.ok(existingIngredient);
    }

    @Operation(summary = "Deletes ingredients by their unique ID")
    @DeleteMapping("/ingredients/{id}")
    public void DeleteIngredient(@PathVariable("id") int id)
    {
        Optional<MIngredient> ingredient = rIngredient.findById(id);
        if (ingredient.isPresent())
            rIngredient.deleteById(id);
    }

    @Operation(summary = "Deletes ingredients by userID")
    @DeleteMapping("ingredients/userID/{userID}")
    public void deleteIngredientsByUser(@PathVariable("userID") int userID)
    {
        List<MIngredient> ingredients = rIngredient.findByUserID(userID).orElse(null);
        if (ingredients != null && !ingredients.isEmpty())
            rIngredient.deleteAll(ingredients);
    }

    @Operation(summary = "Deletes ingredients by recipeID")
    @DeleteMapping("ingredients/recipeID/{recipeID}")
    public void deleteIngredientsByRecipe(@PathVariable("recipeID") int recipeID)
    {
        List<MIngredient> ingredients = rIngredient.findByRecipeID(recipeID).orElse(null);
        if (ingredients != null && !ingredients.isEmpty())
            rIngredient.deleteAll(ingredients);
    }
}

package supperSolver.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supperSolver.DataTransferObjects.RatingDTO;
import supperSolver.Models.MRating;
import supperSolver.Repositories.RRating;
import supperSolver.Repositories.RUser;
import supperSolver.Repositories.RRecipe;

import java.util.List;
import java.util.Optional;

@RestController
public class RatingController
{
    @Autowired
    private RRating rRating;

    @Autowired
    private RUser rUser;

    @Autowired
    private RRecipe rRecipe;

    @Operation(summary = "Gets rating by its unique ID")
    @GetMapping(path = "/rating/{ID}")
    public MRating GetRatingByID(@PathVariable("ID") int ID)
    {
        if(rRating.findById(ID).isPresent())
        return rRating.findById(ID).get();
        else throw new IllegalArgumentException("Rating is not there");
    }

    @Operation(summary = "Gets list of ratings by recipeID")
    @GetMapping(path = "/rating/byrecipe/{recipeID}")
    public List<MRating> GetRatingsByRecipeID(@PathVariable("recipeID") int recipeID)
    {
        return rRating.findByRecipeID(recipeID);
    }

    @Operation(summary = "Returns an integer of the average rating of a given recipe")
    @GetMapping(path = "/rating/avg/{recipeID}")
    public int avgRating(@PathVariable("recipeID") int recipeID)
    {
        //grabs list of all the ratings for the recipe
        List<MRating> allRatingsForRecipe = rRating.findByRecipeID(recipeID);
        int avg = 0;
        //loops through and adds all the ratings
        for (MRating mRating : allRatingsForRecipe) {
            avg += mRating.getRating();
        }
        //divides by size of list to get average
        avg /= allRatingsForRecipe.size();
        return avg;
    }

    @Operation(summary = "Gets a list of ratings created by a given user")
    @GetMapping(path = "/rating/byuserid/{userID}")
    public List<MRating> GetRatingsByUserID(@PathVariable("userID") int userID)
    {
        return rRating.findByUserID(userID);
    }

    @Operation(summary = "Has user rated recipe before")
    @GetMapping("rating/{userID}/{recipeID}")
    public boolean RatingBeenHad(@PathVariable("userID") int userID, @PathVariable("recipeID") int recipeID)
    {
        return rRating.existsByUserIDAndRecipeID(userID, recipeID);
    }

    @Operation(summary = "Creates a rating object on Post")
    @PostMapping(path = "/rating")
    public MRating CreateRating(@RequestBody RatingDTO rating)
    {
        if(rating.getRating() > 5 || rating.getRating() < 0)
            throw new IllegalArgumentException("Rating must be less than 5 or greater than -1");
        else
        {
            MRating newRating = new MRating();

            newRating.setRating(rating.getRating());
            newRating.setUser(rUser.findById(rating.getUserID()).get());
            newRating.setRecipe(rRecipe.findById(rating.getRecipeID()).get());

            return rRating.save(newRating);
        }
    }

    @Operation(summary = "Updates an existing Rating by its Unique ID")
    @PutMapping(path = "/rating/{ID}")
    public ResponseEntity<MRating> updateRating(@PathVariable int ID, @RequestBody MRating rating) {

        MRating existingRating = rRating.findById(ID).get();

        existingRating.setUser(rating.getUser());
        existingRating.setRating(rating.getRating());
        existingRating.setRecipe(rating.getRecipe());

        rRating.save(existingRating);
        return ResponseEntity.ok(existingRating);
    }

    @Operation(summary = "Deletes ratings by their unique ID")
    @DeleteMapping(path = "/rating/{ID}")
    public String DeleteRating(@PathVariable int ID)
    {
        Optional<MRating> rating = rRating.findById(ID);
        if (rating.isPresent())
        {
            rRating.deleteById(ID);
            return "Rating ID: " + ID + " has been deleted";
        }
        return "Rating ID: " + ID + " does not exist";
    }

    @Operation(summary = "Deletes ratings by userID")
    @DeleteMapping("ratings/userID/{userID}")
    public void deleteRatingsByUser(@PathVariable("userID") int userID)
    {
        List<MRating> ratings = rRating.findByUserID(userID);
        if (ratings != null && !ratings.isEmpty())
            rRating.deleteAll(ratings);
    }

    @Operation(summary = "Deletes ratings by recipeID")
    @DeleteMapping("ratings/recipeID/{recipeID}")
    public void deleteRatingsByRecipe(@PathVariable("recipeID") int recipeID)
    {
        List<MRating> ratings = rRating.findByRecipeID(recipeID);
        if (ratings != null && !ratings.isEmpty())
            rRating.deleteAll(ratings);
    }
}
package supperSolver.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import net.bytebuddy.build.Plugin;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supperSolver.DataTransferObjects.LoginRequestDTO;
import supperSolver.Models.*;
import supperSolver.Repositories.RComment;
import supperSolver.Repositories.RIngredient;
import supperSolver.Repositories.RUser;
import supperSolver.Repositories.RImage;
import supperSolver.Repositories.RFriend;
import supperSolver.Repositories.RIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController
{
    @Autowired
    private RUser rUser;

    @Autowired
    private RFriend rFriend;

    @Autowired
    private CommentController commentController;

    @Autowired
    private AdvertisementController advertisementController;

    @Autowired
    private ImageController imageController;

    @Autowired
    private IngredientController ingredientController;

    @Autowired
    private RatingController ratingController;

    @Autowired
    private GroupController groupController;

    @Autowired
    private RecipeController recipeController;

    @Operation(summary = "Gets a list of all users")
    @GetMapping("users")
    public List<MUser> getUsers()
    {
        return rUser.findAll();
    }

    @Operation(summary = "Returns a single user by its unique ID")
    @GetMapping("/users/{ID}")
    public Optional<MUser> GetUserByID(@PathVariable int ID)
    {
        return rUser.findById(ID);
    }

    @Operation(summary = "Returns user ID given their username and password")
    @GetMapping("/userID/{username}/{password}")
    public String GetUserID(@PathVariable String username, @PathVariable String password)
    {
        Optional<MUser> user = rUser.findByUsernameAndPassword(username, password);
        return user.map(mUser -> Integer.toString(mUser.getID())).orElse("-1");
    }

    @Operation(summary = "Returns a list of users with similar usernames to the parameter given")
    @GetMapping("/users/searchByUsername/{username}")
    public List<MUser> searchByUsername(@PathVariable String username)
    {
        return rUser.findByUsernameLike(username);
    }

    @Operation(summary = "Return a list of all the users friends")
    @GetMapping("users/getFriends/{ID}")
    public List<MUser> getFriendsByID(@PathVariable int ID)
    {
        MUser user;
        if(rUser.findById(ID).isPresent())
        {
            user = rUser.findById(ID).get();
            return rFriend.findFriends(user);
        }
        else throw new IllegalArgumentException("User Is Not Found");
    }

    @Operation(summary = "Adds a friend to the users friends list")
    @PostMapping("/users/addFriend/{ID}/{friendID}")
    public void addFriend(@PathVariable int ID, @PathVariable int friendID)
    {
        MUser user = rUser.findById(ID).get();
        MUser friend = rUser.findById(friendID).get();

        MFriend addFriend = new MFriend(user, friend);
        rFriend.save(addFriend);
    }

    @Operation(summary = "Creates a user object on post")
    @PostMapping("/users")
    public @ResponseBody MUser createUser(@RequestBody MUser r)
    {
        //need to check for duplicate usernames if its duplicate throw exception
        if(rUser.findByUsername(r.getUsername()).isPresent()){
            throw new IllegalArgumentException("Duplicate username: " + r.getUsername() + " please make a unique username!");
        }
        else {
            return rUser.save(r);
        }
    }

    @Operation(summary = "Updates a user object given their ID")
    @PutMapping(path = "/users/{ID}")
    public ResponseEntity<MUser> updateUser(@PathVariable int ID, @RequestBody MUser user)
    {
        if (rUser.findByUsername(user.getUsername()).isPresent())
            throw new IllegalArgumentException("Duplicate username: " + user.getUsername() + " please try a unique username");
        else
        {
            MUser existingUser = rUser.findById(ID).get();

            existingUser.setIsAdmin(user.getIsAdmin());
            existingUser.setIsAdvertiser(user.getIsAdvertiser());
            existingUser.setName(user.getName());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setBio(user.getBio());
            existingUser.setGroupID(user.getGroupID());

            rUser.save(existingUser);
            return ResponseEntity.ok(existingUser);
        }
    }

    @Operation(summary = "Updates a user to be an advertiser")
    @PutMapping("/users/userToAdvertiser/{ID}")
    public ResponseEntity<MUser> userToAdvertiser(@PathVariable int ID)
    {
        MUser existingUser = rUser.findById(ID).get();
        existingUser.setIsAdvertiser(true);
        rUser.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }

    @Operation(summary = "Returns a boolean result, true if there is a matching pair of username and password in the database, otherwise false")
    @PostMapping("/login")
    public MUser login(@RequestBody LoginRequestDTO loginRequest) {
        // Try to find the user by username and password
        Optional<MUser> user = rUser.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());

        if (user.isPresent())
        {// Credentials are valid, return success response
            return user.get();
        }
        else
        {
            // Invalid credentials, return error response
            return null;
        }
    }

    @Operation(summary = "Deletes all friendships by userID")
    @DeleteMapping("users/friends/{ID}")
    public void deleteFriendshipsByUser(@PathVariable("ID") int ID)
    {
        List<MFriend> friends = rFriend.findByMainUserID(ID);
        friends.addAll(rFriend.findByFriendAddedID(ID));
        rFriend.deleteAll(friends);
    }

    @Operation(summary = "Deletes a user based on their unique ID")
    @DeleteMapping("/users/{ID}")
    public void DeleteUser(@PathVariable int ID)
    {
        Optional<MUser> user = rUser.findById(ID);
        if (user.isPresent())
        {
            // Delete everything belonging to user
            advertisementController.deleteAdvertisementsByUser(ID);
            commentController.deleteCommentsByUser(ID);
            groupController.removeUserFromGroup(ID);
            imageController.deleteImagesByUser(ID);
            ingredientController.deleteIngredientsByUser(ID);
            ratingController.deleteRatingsByUser(ID);
            recipeController.deleteRecipesByUser(ID);
            deleteFriendshipsByUser(ID);

            rUser.deleteById(ID);
        }
    }
}
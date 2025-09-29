package supperSolver.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import supperSolver.DataTransferObjects.ArrayListDTO;
import supperSolver.Models.MIngredient;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RIngredient;
import supperSolver.Repositories.RUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class GroupController {

    @Autowired
    private RUser rUser;

    @Autowired
    private RIngredient rIngredient;

    @Operation(summary = "returns if a user is in a group or not")
    @GetMapping("/group/userInGroup/{userID}")
    public boolean checkUserInGroup(@PathVariable int userID){

        if(rUser.findById(userID).isPresent()){
            MUser currentUser = rUser.findById(userID).get();
            return currentUser.getGroupID() != 0;
        }
        else{
            throw new IllegalArgumentException("User not found");
        }
    }

    @Operation(summary = "returns users groupID")
    @GetMapping("/group/userGroupID/{userID}")
    public String getGroupID(@PathVariable int userID){
        if(rUser.findById(userID).isPresent()){
            MUser currentUser = rUser.findById(userID).get();
            return String.valueOf(currentUser.getGroupID());
        }
        else{
            throw new IllegalArgumentException("User not found");
        }
    }

    @Operation(summary = "returns a list of all ingredients shared by groupID adds quantity if multiple of the same")
    @GetMapping("/group/getAllIngredients/{groupID}")
    public List<MIngredient> getGroupIngredients(@PathVariable int groupID){
        //if group exist
        if(rUser.findByGroupID(groupID).isPresent()){
            List<MUser> groupUsers =  rUser.findByGroupID(groupID).get();
            List<MIngredient> groupIngredients = new ArrayList<>(List.of());
            for(MUser user : groupUsers){
                //Getting list by user
                List<MIngredient> userList = rIngredient.findByUserID(user.getID()).get();
                for(MIngredient ing : userList) {
                    if (groupIngredients.stream().noneMatch(i -> i.getName().equals(ing.getName()))){
                        groupIngredients.add(ing);
                    }
                    else
                    {
                        MIngredient existingIngredient = groupIngredients.stream()
                                .filter(i -> i.getName().equals(ing.getName()))
                                .findFirst()
                                .orElse(null);
                        if(existingIngredient != null){
                            existingIngredient.setQuantity(existingIngredient.getQuantity() + ing.getQuantity());
                        }
                        else {
                            System.out.println("Hit null ing");
                        }
                    }
                }
            }
            return groupIngredients;
        }
        else throw new IllegalArgumentException("Group Does not exist");
    }

    
    @Operation(summary = "Creates a group (Finds highest ID + 1) assigns current user and adds all users provided in array list")
    @PostMapping("/group/{currentUserID}")
    public List<MUser> createAndAssignGroup(@PathVariable int currentUserID, @RequestBody ArrayListDTO array){

        //Getting max of current group column + 1 for new group
        int newGroupID = rUser.findMaxGroupID() + 1;
        if(rUser.findById(currentUserID).isPresent())
        {
            MUser currentUser = rUser.findById(currentUserID).get();
            currentUser.setGroupID(newGroupID);
            rUser.save(currentUser);
            //Setting all users groupID
            for(String uname : array.getArray()){
                MUser userToAdd = null;
                Optional<MUser> check = rUser.findByUsername(uname);
                if (check.isPresent()) {
                    userToAdd = check.get();
                    userToAdd.setGroupID(newGroupID);
                    rUser.save(userToAdd);
                }
            }
            if(rUser.findByGroupID(newGroupID).isPresent()) {
                return rUser.findByGroupID(newGroupID).get();
            }
            else throw new IllegalArgumentException("No Group Found");
        }
        else throw new IllegalArgumentException("User ID not found");
    }

    @Operation(summary = "returns list of all users with group id")
    @GetMapping("/group/getAllUsers/{groupID}")
    public List<MUser> getGroupUsers(@PathVariable int groupID){
        if(rUser.findByGroupID(groupID).isPresent()){
            return rUser.findByGroupID(groupID).get();
        }
        else{
            throw new IllegalArgumentException("Group does not exist");
        }
    }

    @Operation(summary = "adds a users to a group")
    @PutMapping("/group/addUser/{groupID}")
    public MUser addUserToGroup(@PathVariable int groupID, @RequestBody ArrayListDTO usersToAdd){
        List<String> notFound = new ArrayList<>();

        for(String username : usersToAdd.getArray()) {
            if (rUser.findByUsername(username).isPresent()) {
                MUser currentUser = rUser.findByUsername(username).get();
                currentUser.setGroupID(groupID);
                rUser.save(currentUser);
            }
            else{
                notFound.add(username);
            }
        }

        if (!notFound.isEmpty()){
            throw new IllegalArgumentException("user no be real" + notFound);
        }
        throw new IllegalArgumentException("user does not exist");
    }

    @Operation(summary = "Removes User from their current group")
    @PutMapping("/group/removeUser/{userID}")
    public MUser removeUserFromGroup(@PathVariable int userID)
    {
        if(rUser.findById(userID).isPresent())
        {
            MUser currentUser = rUser.findById(userID).get();
            currentUser.setGroupID(0);
            rUser.save(currentUser);
            return currentUser;
        }
        else throw new IllegalArgumentException("User does not exist");
    }

    @Operation(summary = "Removes all users from group, or deletes group by groupID")
    @DeleteMapping("/group/{groupID}")
    public String deleteGroup(@PathVariable int groupID){

        if(rUser.findByGroupID(groupID).isPresent()) {
            List<MUser> groupUsers = rUser.findByGroupID(groupID).get();
            for(MUser currentUser : groupUsers){
                //Settings all users in group gid to 0 "removing them
                //and basically deleting group"
                currentUser.setGroupID(0);
                rUser.save(currentUser);
            }
            return "Group " + groupID + " has been deleted";
        }
        else throw new IllegalArgumentException("No Users Found With groupID " + groupID);
    }
}


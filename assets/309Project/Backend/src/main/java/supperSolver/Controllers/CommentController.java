package supperSolver.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import supperSolver.DataTransferObjects.CommentDTO;
import supperSolver.Models.MComment;
import supperSolver.Repositories.RComment;
import supperSolver.Repositories.RUser;
import supperSolver.Repositories.RRecipe;

@RestController
public class CommentController
{
    @Autowired
    private RComment rComment;

    @Autowired
    private RUser rUser;

    @Autowired
    private RRecipe rRecipe;

    @Operation(summary = "Gets a comment by its ID")
    @GetMapping("/comments/{id}")
    public MComment getCommentByID(@PathVariable("id") int id)
    {
        return rComment.findById(id).get();
    }

    @Operation(summary = "Creates a new comment")
    @PostMapping("/comments")
    public MComment createComment(@RequestBody CommentDTO comment)
    {
        MComment newComment = new MComment();

        newComment.setUser(rUser.findById(comment.getUserID()).get());
        newComment.setRecipe(rRecipe.findById(comment.getRecipeID()).get());
        newComment.setComment(comment.getComment());

        return rComment.save(newComment);
    }

    @Operation(summary = "Gets specific comment by userID, recipeID, and comment string")
    @GetMapping("/comments/getSpecific/{userID}/{recipeID}/{commentString}")
    public String getSpecificCommentID(@PathVariable int userID, @PathVariable int recipeID, @PathVariable String commentString)
    {
        List<MComment> allRecipeComments = findCommentByRecipeID(recipeID);
        for(MComment c : allRecipeComments){
            if((c.getUser().getID() == userID) && (c.getComment().equals(commentString))){
                return String.valueOf(c.getID());
            }
        }
        //if the comment is not found returns 0
        return String.valueOf(0);
    }

    @Operation(summary = "Gets all comments by a user")
    @GetMapping("/comments/byUser/{userID}")
    public List<MComment> findCommentByUserID(@PathVariable int userID){
        return rComment.findByUserID(userID);
    }

    @Operation(summary = "Gets all comments by a recipe")
    @GetMapping("/comments/byRecipe/{recipeID}")
    public List<MComment> findCommentByRecipeID(@PathVariable int recipeID)
    {
        return rComment.findByRecipeID(recipeID);
    }

    @Operation(summary = "Gets a list of all comment's words of a recipe")
    @GetMapping("/comments/strings/{recipeID}")
    public ArrayList<String> findCommentString(@PathVariable int recipeID)
    {
        List<MComment> listOfComments = rComment.findByRecipeID(recipeID);
        ArrayList<String> finalCommentList = new ArrayList<>();
        for(MComment c : listOfComments){
            finalCommentList.add(c.comment);
        }
        return finalCommentList;
    }

    @Operation(summary = "Gets a list of all comment's words of a user")
    @GetMapping("/comments/userIDs/{recipeID}")
    public ArrayList<String> findUserIDString(@PathVariable int recipeID)
    {
        List<MComment> listOfComments = rComment.findByRecipeID(recipeID);
        ArrayList<String> finalUserIDList = new ArrayList<>();
        for(MComment c : listOfComments){
            finalUserIDList.add(String.valueOf(c.user.getID()));
        }
        return finalUserIDList;
    }

    @Operation(summary = "Gets a list of the usernames of comments made on a recipe")
    @GetMapping("/comments/usernames/{recipeID}")
    public ArrayList<String> findUsernameString(@PathVariable int recipeID)
    {
        List<MComment> listOfComments = rComment.findByRecipeID(recipeID);
        ArrayList<String> finalUsernameList = new ArrayList<>();
        for(MComment c : listOfComments){
            finalUsernameList.add(c.user.getUsername());
        }
        return finalUsernameList;
    }

    @Operation(summary = "Updates a comment")
    @PutMapping("/comments/{id}")
    public ResponseEntity<MComment> updateComment(@PathVariable("id") int id, @RequestBody CommentDTO comment)
    {
        if(rComment.findById(id).isPresent()) {
            MComment existingComment = rComment.findById(id).get();

            existingComment.setRecipe(rRecipe.findById(comment.getRecipeID()).get());
            existingComment.setUser(rUser.findById(comment.getUserID()).get());
            existingComment.setComment(comment.getComment());

            rComment.save(existingComment);
            return ResponseEntity.ok(existingComment);
        }
        else throw new IllegalArgumentException("Comment does not exist");
    }

    @Operation(summary = "Deletes comment by its unique ID")
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable("id") int id)
    {
        Optional<MComment> comment = rComment.findById(id);
        if (comment.isPresent())
            rComment.deleteById(id);
    }

    @Operation(summary = "Deletes comments by userID")
    @DeleteMapping("/comments/userID/{userID}")
    public void deleteCommentsByUser(@PathVariable("userID") int userID)
    {
        List<MComment> comments = rComment.findByUserID(userID);
        if (comments != null && !comments.isEmpty())
            rComment.deleteAll(comments);
    }

    @Operation(summary = "Deletes comments by recipeID")
    @DeleteMapping("/comments/recipeID/{recipeID}")
    public void deleteCommentsByRecipe(@PathVariable("recipeID") int recipeID)
    {
        List<MComment> comments = rComment.findByRecipeID(recipeID);
        if (comments != null && !comments.isEmpty())
            rComment.deleteAll(comments);
    }
}

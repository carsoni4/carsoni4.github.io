package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.*;
import supperSolver.DataTransferObjects.RecipeDTO;
import supperSolver.Models.MRating;
import supperSolver.Models.MRecipe;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RRating;
import supperSolver.Repositories.RRecipe;
import supperSolver.Repositories.RUser;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class TRecipe
{
    private MockMvc mockMvc;

    @Mock
    private RRating rRating;

    @Mock
    private RRecipe rRecipe;

    @Mock
    private CommentController commentController;

    @Mock
    private ImageController imageController;

    @Mock
    private IngredientController ingredientController;

    @Mock
    private RatingController ratingController;

    @InjectMocks
    private RecipeController recipeController;

    private MRecipe sampleRecipe;
    private MUser sampleUser;
    private MRating sampleRating;

    @BeforeEach
    public void setUp()
    {
        sampleUser = new MUser();
        sampleUser.setID(1);

        sampleRecipe = new MRecipe();
        sampleRecipe.setID(1);
        sampleRecipe.setName("Sample Recipe");
        sampleRecipe.setDescription("A sample recipe");
        sampleRecipe.setUser(sampleUser);

        sampleRating = new MRating();
        sampleRating.setID(1);
        sampleRating.setRecipe(sampleRecipe);
        sampleRating.setRating(5);

        when(rRecipe.findById(1)).thenReturn(Optional.of(sampleRecipe));
        when(rRecipe.findAll()).thenReturn(List.of(sampleRecipe));
        when(rRecipe.findByUserID(1)).thenReturn(List.of(sampleRecipe));
        when(rRecipe.findByNameLike("Sample")).thenReturn(List.of(sampleRecipe));
        when(rRating.findByRecipeID(1)).thenReturn(List.of(sampleRating));

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void getRecipeByUserID() throws Exception
    {
        mockMvc.perform(get("/recipe/usr/{userID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Recipe"));
    }

    @Test
    public void getAllRecipes() throws Exception
    {
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Recipe"));
    }

    @Test
    public void getRecipeByID() throws Exception
    {
        mockMvc.perform(get("/recipe/{recipeID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Recipe"));
    }

    @Test
    public void searchForRecipeByName() throws Exception
    {
        mockMvc.perform(get("/recipe/searchByName/{name}", "Sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Recipe"));
    }

    @Test
    public void updateRecipe() throws Exception
    {
        MRecipe updatedRecipe = new MRecipe();
        updatedRecipe.setName("Updated Recipe");
        updatedRecipe.setDescription("An updated recipe");
        updatedRecipe.setUser(sampleUser);

        when(rRecipe.save(any(MRecipe.class))).thenReturn(updatedRecipe);

        mockMvc.perform(put("/recipe/{recipeID}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Recipe\", \"description\":\"An updated recipe\", \"user\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Recipe"))
                .andExpect(jsonPath("$.description").value("An updated recipe"));
    }

    @Test
    public void deleteRecipe() throws Exception
    {
        doNothing().when(rRecipe).deleteById(1);

        mockMvc.perform(delete("/recipe/{recipeID}", 1))
                .andExpect(status().isOk());

        verify(rRecipe, times(1)).deleteById(1);
    }
}

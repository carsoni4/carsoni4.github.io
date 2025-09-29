package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.RatingController;
import supperSolver.DataTransferObjects.RatingDTO;
import supperSolver.Models.MRating;
import supperSolver.Models.MUser;
import supperSolver.Models.MRecipe;
import supperSolver.Repositories.RRating;
import supperSolver.Repositories.RUser;
import supperSolver.Repositories.RRecipe;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
public class TRating {

    private MockMvc mockMvc;

    @Mock
    private RUser rUser;

    @Mock
    private RRecipe rRecipe;

    @Mock
    private RRating rRating;

    @InjectMocks
    private RatingController ratingController;

    private MUser sampleUser;
    private MRecipe sampleRecipe;
    private MRating sampleRating;

    @BeforeEach
    public void setUp() {
        sampleUser = new MUser();
        sampleUser.setID(1);

        sampleRecipe = new MRecipe();
        sampleRecipe.setID(1);
        sampleRecipe.setName("Sample Recipe");

        sampleRating = new MRating();
        sampleRating.setID(1);
        sampleRating.setUser(sampleUser);
        sampleRating.setRecipe(sampleRecipe);
        sampleRating.setRating(4);

        when(rUser.findById(1)).thenReturn(Optional.of(sampleUser));
        when(rRecipe.findById(1)).thenReturn(Optional.of(sampleRecipe));
        when(rRating.findById(1)).thenReturn(Optional.of(sampleRating));
        when(rRating.findByRecipeID(1)).thenReturn(List.of(sampleRating));
        when(rRating.findByUserID(1)).thenReturn(List.of(sampleRating));

        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    public void getRatingByID() throws Exception {
        mockMvc.perform(get("/rating/{ID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.recipe.id").value(1));
    }

    @Test
    public void getRatingsByRecipeID() throws Exception {
        mockMvc.perform(get("/rating/byrecipe/{recipeID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[0].user.id").value(1))
                .andExpect(jsonPath("$[0].recipe.id").value(1));
    }

    @Test
    public void avgRating() throws Exception {
        mockMvc.perform(get("/rating/avg/{recipeID}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    public void getRatingsByUserID() throws Exception {
        mockMvc.perform(get("/rating/byuserid/{userID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[0].user.id").value(1))
                .andExpect(jsonPath("$[0].recipe.id").value(1));
    }

    @Test
    public void createRating() throws Exception {
        // Create a RatingDTO for the test
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRating(5);
        ratingDTO.setUserID(1); // Corresponds to sampleUser ID
        ratingDTO.setRecipeID(1); // Corresponds to sampleRecipe ID

        // Create the expected MRating that would be saved
        MRating createdRating = new MRating();
        createdRating.setID(2);
        createdRating.setUser(sampleUser);
        createdRating.setRecipe(sampleRecipe);
        createdRating.setRating(5);

        // Mocking the repository save method to return the created rating
        when(rRating.save(any(MRating.class))).thenReturn(createdRating);

        // Perform the POST request with RatingDTO
        mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 5, \"userID\": 1, \"recipeID\": 1}"))  // Use the DTO JSON structure
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.recipe.id").value(1));

        // Verify save method was called once
        verify(rRating, times(1)).save(any(MRating.class));
    }

    @Test
    public void updateRating() throws Exception {
        MRating updatedRating = new MRating();
        updatedRating.setID(1);
        updatedRating.setUser(sampleUser);
        updatedRating.setRecipe(sampleRecipe);
        updatedRating.setRating(3);

        when(rRating.findById(1)).thenReturn(Optional.of(sampleRating));

        mockMvc.perform(put("/rating/{ID}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"rating\": 3, \"user\": {\"id\": 1}, \"recipe\": {\"id\": 1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(3))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.recipe.id").value(1));

        verify(rRating, times(1)).save(any(MRating.class));
    }

    @Test
    public void deleteRating() throws Exception {
        mockMvc.perform(delete("/rating/{ID}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating ID: 1 has been deleted"));

        verify(rRating, times(1)).deleteById(1);
    }

    @Test
    void createRating_InvalidRating() {
        // Ensure the invalid rating is set properly and triggers the exception
        assertThrows(IllegalArgumentException.class, () -> {
            MRating rating = new MRating();
            rating.setUser(sampleUser);
            rating.setRecipe(sampleRecipe);
            rating.setRating(0); // Invalid rating, expect exception
        });
    }
}

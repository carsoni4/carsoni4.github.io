package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.IngredientController;
import supperSolver.DataTransferObjects.IngredientDTO;
import supperSolver.Models.MIngredient;
import supperSolver.Models.MRecipe;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RIngredient;
import supperSolver.Repositories.RRecipe;
import supperSolver.Repositories.RUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
public class TIngredient
{
    private MockMvc mockMvc;

    @Mock
    private RIngredient rIngredient;

    @Mock
    private RUser rUser;

    @Mock
    private RRecipe rRecipe;

    @InjectMocks
    private IngredientController ingredientController;

    private MIngredient sampleIngredient;
    private MUser sampleUser;
    private MRecipe sampleRecipe;

    @BeforeEach
    public void setUp()
    {
        sampleUser = new MUser();
        sampleUser.setID(1);

        sampleRecipe = new MRecipe();
        sampleRecipe.setID(1);

        sampleIngredient = new MIngredient();
        sampleIngredient.setID(1);
        sampleIngredient.setUser(sampleUser);
        sampleIngredient.setRecipe(sampleRecipe);
        sampleIngredient.setName("Sample Ingredient");
        sampleIngredient.setQuantity(100);

        when(rIngredient.findById(1)).thenReturn(Optional.of(sampleIngredient));
        when(rIngredient.findByUserID(1)).thenReturn(Optional.of(List.of(sampleIngredient)));
        when(rIngredient.findByRecipeID(1)).thenReturn(Optional.of(List.of(sampleIngredient)));
        when(rUser.findById(1)).thenReturn(Optional.of(sampleUser));
        when(rRecipe.findById(1)).thenReturn(Optional.of(sampleRecipe));

        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void getIngredientsByUser() throws Exception
    {
        mockMvc.perform(get("/ingredients/user/{userID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Ingredient"));
    }

    @Test
    public void getIngredientsByRecipe() throws Exception
    {
        mockMvc.perform(get("/ingredients/recipe/{recipeID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Ingredient"));
    }

    @Test
    public void getIngredientById() throws Exception
    {
        mockMvc.perform(get("/ingredients/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Ingredient"));
    }

    @Test
    public void createIngredient() throws Exception
    {
        IngredientDTO newIngredientDTO = new IngredientDTO();
        newIngredientDTO.setUserID(1);
        newIngredientDTO.setRecipeID(1);
        newIngredientDTO.setName("New Ingredient");
        newIngredientDTO.setQuantity(200);

        when(rIngredient.save(any(MIngredient.class))).thenReturn(sampleIngredient);

        mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userID\":1, \"recipeID\":1, \"name\":\"New Ingredient\", \"quantity\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Ingredient"));
    }

    @Test
    public void updateIngredient() throws Exception
    {
        MIngredient updatedIngredient = new MIngredient();
        updatedIngredient.setUser(sampleUser);
        updatedIngredient.setRecipe(sampleRecipe);
        updatedIngredient.setName("Updated Ingredient");
        updatedIngredient.setQuantity(150);

        when(rIngredient.save(any(MIngredient.class))).thenReturn(updatedIngredient);

        mockMvc.perform(put("/ingredients/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\":{\"id\":1}, \"recipe\":{\"id\":1}, \"name\":\"Updated Ingredient\", \"quantity\":150}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Ingredient"))
                .andExpect(jsonPath("$.quantity").value(150));
    }

    @Test
    public void deleteIngredient() throws Exception
    {
        int id = 1;

        doNothing().when(rIngredient).deleteById(id);

        mockMvc.perform(delete("/ingredients/{id}", id))
                .andExpect(status().isOk());

        verify(rIngredient, times(1)).deleteById(id);
    }
}

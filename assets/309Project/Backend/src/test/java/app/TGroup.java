package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.GroupController;
import supperSolver.Models.MIngredient;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RIngredient;
import supperSolver.Repositories.RUser;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
public class TGroup {

    private MockMvc mockMvc;

    @Mock
    private RUser rUser;

    @Mock
    private RIngredient rIngredient;

    @InjectMocks
    private GroupController groupController;

    private MUser sampleUser;
    private MIngredient sampleIngredient;

    @BeforeEach
    public void setUp() {
        sampleUser = new MUser();
        sampleUser.setID(1);
        sampleUser.setGroupID(1);


        sampleIngredient = new MIngredient();
        sampleIngredient.setID(1);
        sampleIngredient.setName("Sample Ingredient");
        sampleIngredient.setQuantity(100);

        when(rUser.findByGroupID(1)).thenReturn(Optional.of(List.of(sampleUser)));
        when(rIngredient.findByUserID(1)).thenReturn(Optional.of(List.of(sampleIngredient)));
        when(rUser.findById(1)).thenReturn(Optional.of(sampleUser));
        when(rUser.findMaxGroupID()).thenReturn(1);

        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    public void getGroupIngredients() throws Exception {
        mockMvc.perform(get("/group/getAllIngredients/{groupID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sample Ingredient"))
                .andExpect(jsonPath("$[0].quantity").value(100));
    }

    @Test
    public void createAndAssignGroup() throws Exception {
        // Arrange: Set up mock users and behavior
        MUser user1 = new MUser();
        user1.setID(1);
        user1.setUsername("currentUser");
        user1.setGroupID(0);  // User starts with no group

        MUser user2 = new MUser();
        user2.setID(2);
        user2.setUsername("user2");
        user2.setGroupID(0);  // User starts with no group

        // Mock user lookup by ID and username
        when(rUser.findById(1)).thenReturn(Optional.of(user1)); // Lookup for current user by ID
        when(rUser.findByUsername("user2")).thenReturn(Optional.of(user2)); // Lookup for user2 by username
        when(rUser.findMaxGroupID()).thenReturn(1); // Max group ID is 1, so new group ID will be 2

        // Mock the return value after group assignment
        MUser updatedUser1 = new MUser();
        updatedUser1.setID(1);
        updatedUser1.setUsername("currentUser");
        updatedUser1.setGroupID(2);

        MUser updatedUser2 = new MUser();
        updatedUser2.setID(2);
        updatedUser2.setUsername("user2");
        updatedUser2.setGroupID(2);

        when(rUser.findByGroupID(2)).thenReturn(Optional.of(List.of(updatedUser1, updatedUser2)));

        // Act & Assert: Perform the test
        mockMvc.perform(post("/group/{currentUserID}", 1) // Use currentUserID in URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"array\" : [\"user2\"]}"))  // Adding user 2 to the group
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].groupID").value(2))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].groupID").value(2));

        // Verify that users are saved with the new group ID
        verify(rUser, times(1)).save(user1);  // Verify save for current user
        verify(rUser, times(1)).save(user2);  // Verify save for user2
    }


    @Test
    public void getGroupUsers() throws Exception {
        mockMvc.perform(get("/group/getAllUsers/{groupID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].groupID").value(1));
    }

    @Test
    public void getGroupIngredients_WithDuplicateIngredients() throws Exception {
        // Arrange: Set up mock users and duplicate ingredients
        MUser user1 = new MUser();
        user1.setID(1);
        user1.setGroupID(1);

        MUser user2 = new MUser();
        user2.setID(2);
        user2.setGroupID(1);

        MIngredient ingredient1 = new MIngredient();
        ingredient1.setName("carrot");
        ingredient1.setQuantity(3);
        ingredient1.setUser(user1);

        MIngredient ingredient2 = new MIngredient();
        ingredient2.setName("carrot");
        ingredient2.setQuantity(5);
        ingredient2.setUser(user2);

        MIngredient ingredient3 = new MIngredient();
        ingredient3.setName("potato");
        ingredient3.setQuantity(2);
        ingredient3.setUser(user2);

        // Mock repository behavior
        when(rUser.findByGroupID(1)).thenReturn(Optional.of(List.of(user1, user2)));
        when(rIngredient.findByUserID(1)).thenReturn(Optional.of(List.of(ingredient1)));
        when(rIngredient.findByUserID(2)).thenReturn(Optional.of(List.of(ingredient2, ingredient3)));

        // Act & Assert: Perform the test
        mockMvc.perform(get("/group/getAllIngredients/{groupID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("carrot"))
                .andExpect(jsonPath("$[0].quantity").value(8)) // Combined quantity of "carrot"
                .andExpect(jsonPath("$[1].name").value("potato"))
                .andExpect(jsonPath("$[1].quantity").value(2)); // Quantity of "potato"
    }

}
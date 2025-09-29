package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.*;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RUser;
import supperSolver.Repositories.RFriend;
import supperSolver.Repositories.RImage;
import supperSolver.Repositories.RIngredient;
import supperSolver.DataTransferObjects.LoginRequestDTO;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

public class TUser {

    @Mock
    private RUser rUser;

    @Mock
    private RFriend rFriend;

    @Mock
    private AdvertisementController advertisementController;

    @Mock
    private CommentController commentController;

    @Mock
    private GroupController groupController;

    @Mock
    private ImageController imageController;

    @Mock
    private IngredientController ingredientController;

    @Mock
    private RatingController ratingController;

    @Mock
    private RecipeController recipeController;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByID() throws Exception {
        MUser user = new MUser();
        user.setID(1);  // Ensure correct setter is used
        user.setUsername("no");
        when(rUser.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))  // Match the field name in MUser
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    public void testGetUserID() throws Exception {
        MUser user = new MUser();
        user.setID(1);
        user.setUsername("test");
        user.setPassword("password");
        when(rUser.findByUsernameAndPassword("test", "password")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/userID/test/password"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testSearchByUsername() throws Exception {
        MUser user = new MUser();
        user.setUsername("test");
        when(rUser.findByUsernameLike("test")).thenReturn(List.of(user));

        mockMvc.perform(get("/users/searchByUsername/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("test"));
    }

    @Test
    public void testAddFriend() throws Exception {
        MUser user = new MUser();
        user.setID(1);
        MUser friend = new MUser();
        friend.setID(2);
        when(rUser.findById(1)).thenReturn(Optional.of(user));
        when(rUser.findById(2)).thenReturn(Optional.of(friend));

        mockMvc.perform(post("/users/addFriend/1/2"))
                .andExpect(status().isOk());

        verify(rFriend, times(1)).save(any());
    }

    @Test
    public void testCreateUser() throws Exception {
        MUser newUser = new MUser();
        newUser.setName("John Doe");
        newUser.setUsername("johndoe");
        newUser.setPassword("password");
        newUser.setIsAdmin(false);
        newUser.setIsAdvertiser(false);
        newUser.setBio("Bio here");
        newUser.setGroupID(1);

        // Mock repository to simulate saving the user
        when(rUser.save(any(MUser.class))).thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"username\":\"johndoe\",\"password\":\"password\",\"isAdmin\":false,\"isAdvertiser\":false,\"bio\":\"Bio here\",\"groupID\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }


    @Test
    public void testUpdateUser() throws Exception {
        MUser existingUser = new MUser();
        existingUser.setID(1);
        existingUser.setUsername("test");

        when(rUser.findById(1)).thenReturn(Optional.of(existingUser));
        when(rUser.save(existingUser)).thenReturn(existingUser);

        mockMvc.perform(put("/users/1")
                        .contentType("application/json")
                        .content("{\"username\":\"test\",\"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    public void testUserToAdvertiser() throws Exception {
        MUser user = new MUser();
        user.setID(1);
        when(rUser.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/userToAdvertiser/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAdvertiser").value(true));
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("test");
        loginRequest.setPassword("password");

        MUser user = new MUser();
        user.setUsername("test");
        user.setPassword("password");

        when(rUser.findByUsernameAndPassword("test", "password")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{\"username\":\"test\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        MUser user = new MUser();
        user.setID(1);
        when(rUser.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(rUser, times(1)).deleteById(1);
    }
}

package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.ImageController;
import supperSolver.Models.MImage;
import supperSolver.Repositories.RImage;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
public class TImage {
    private MockMvc mockMvc;

    @Mock
    private RImage rImage;

    @InjectMocks
    private ImageController imageController;

    private MImage sampleImage;

    @BeforeEach
    public void setUp() {
        sampleImage = new MImage();
        sampleImage.setID(1);
        sampleImage.setImgUrl("images/1_sample.jpg");
        sampleImage.setRecipeID(1);
        sampleImage.setUserID(1);
        sampleImage.setImagePos(0);

        // Mocking repository calls
        when(rImage.findById(1)).thenReturn(java.util.Optional.of(sampleImage));
        when(rImage.findByRecipeID(1)).thenReturn(java.util.List.of(sampleImage));
        when(rImage.findByUserID(1)).thenReturn(sampleImage);
        when(rImage.findByRecipeIDAndImagePos(1, 0)).thenReturn(sampleImage);

        // Setup MockMvc with the ImageController
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void getImageByRecipe() throws Exception {
        // Test retrieving image by recipeID
        mockMvc.perform(get("/image/byrecipe/{recipeID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imgUrl").value("images/1_sample.jpg"));
    }

    @Test
    public void getImageByUserID() throws Exception {
        // Test retrieving image by userID
        mockMvc.perform(get("/image/user/{userID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imgUrl").value("images/1_sample.jpg"));
    }

    @Test
    public void getFirstImage() throws Exception {
        // Test retrieving the first image of a recipe
        mockMvc.perform(get("/image/first/{recipeID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imgUrl").value("images/1_sample.jpg"));
    }
}

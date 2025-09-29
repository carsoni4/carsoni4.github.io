package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supperSolver.Controllers.CommentController;
import supperSolver.DataTransferObjects.CommentDTO;
import supperSolver.Models.MComment;
import supperSolver.Models.MRecipe;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RComment;
import supperSolver.Repositories.RRecipe;
import supperSolver.Repositories.RUser;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class TComment
{
    private MockMvc mockMvc;

    @Mock
    private RComment rComment;

    @Mock
    private RUser rUser;

    @Mock
    private RRecipe rRecipe;

    @InjectMocks
    private CommentController commentController;

    private MComment sampleComment;
    private MUser sampleUser;
    private MRecipe sampleRecipe;

    @BeforeEach
    public void setUp()
    {
        sampleUser = new MUser();
        sampleUser.setID(1);

        sampleRecipe = new MRecipe();
        sampleRecipe.setID(1);

        sampleComment = new MComment();
        sampleComment.setID(1);
        sampleComment.setUser(sampleUser);
        sampleComment.setRecipe(sampleRecipe);
        sampleComment.setComment("Sample comment");

        when(rComment.findById(1)).thenReturn(java.util.Optional.of(sampleComment));
        when(rUser.findById(1)).thenReturn(java.util.Optional.of(sampleUser));
        when(rRecipe.findById(1)).thenReturn(java.util.Optional.of(sampleRecipe));

        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void getCommentById() throws Exception
    {
        mockMvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comment").value("Sample comment"));
    }

    @Test
    public void createComment() throws Exception
    {
        CommentDTO newCommentDTO = new CommentDTO();
        newCommentDTO.setUserID(1);
        newCommentDTO.setRecipeID(1);
        newCommentDTO.setComment("New comment");

        when(rComment.save(any(MComment.class))).thenReturn(sampleComment);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userID\":1, \"recipeID\":1, \"comment\":\"New comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comment").value("Sample comment"));
    }

    @Test
    public void updateComment() throws Exception
    {
        CommentDTO updatedCommentDTO = new CommentDTO();
        updatedCommentDTO.setUserID(1);
        updatedCommentDTO.setRecipeID(1);
        updatedCommentDTO.setComment("Updated comment");

        when(rComment.save(any(MComment.class))).thenReturn(sampleComment);

        mockMvc.perform(put("/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userID\":1, \"recipeID\":1, \"comment\":\"Updated comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comment").value("Updated comment"));
    }

    @Test
    public void deleteComment() throws Exception
    {
        int id = 1;

        doNothing().when(rComment).deleteById(id);

        mockMvc.perform(delete("/comments/{id}", id))
                .andExpect(status().isOk());

        verify(rComment, times(1)).deleteById(id);
    }
}

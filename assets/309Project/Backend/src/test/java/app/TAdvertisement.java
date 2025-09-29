package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import supperSolver.Controllers.AdvertisementController;
import supperSolver.DataTransferObjects.AdvertisementDTO;
import supperSolver.Models.MAdvertisement;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RAdvertisement;
import supperSolver.Repositories.RUser;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdvertisementController.class)
public class TAdvertisement
{
    private MockMvc mockMvc;

    @Mock
    private RAdvertisement rAdvertisement;

    @Mock
    private RUser rUser;

    @InjectMocks
    private AdvertisementController advertisementController;

    private MAdvertisement sampleAdvertisement;
    private MUser sampleUser;

    @BeforeEach
    public void setUp()
    {
        sampleUser = new MUser();
        sampleUser.setID(1);
        sampleUser.setIsAdvertiser(true);

        sampleAdvertisement = new MAdvertisement();
        sampleAdvertisement.setID(1);
        sampleAdvertisement.setImageURL("http://example.com/image.jpg");
        sampleAdvertisement.setDescription("Sample Advertisement");
        sampleAdvertisement.setAdvertiser(sampleUser);

        when(rAdvertisement.findById(1)).thenReturn(java.util.Optional.of(sampleAdvertisement));
        when(rUser.findById(1)).thenReturn(java.util.Optional.of(sampleUser));

        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController).build();
    }

    @Test
    public void getAdvertisements() throws Exception
    {
        when(rAdvertisement.findAll()).thenReturn(java.util.List.of(sampleAdvertisement));

        mockMvc.perform(get("/advertisements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Sample Advertisement"));
    }

    @Test
    public void getAdvertisementById() throws Exception
    {
        mockMvc.perform(get("/advertisements/ID/{ID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Sample Advertisement"));
    }

    @Test
    public void createAdvertisement() throws Exception
    {
        AdvertisementDTO newAdvertisementDTO = new AdvertisementDTO();
        newAdvertisementDTO.setImageURL("http://newimage.com/image.jpg");
        newAdvertisementDTO.setDescription("New Advertisement");
        newAdvertisementDTO.setAdvertiserID(1);

        when(rAdvertisement.save(any(MAdvertisement.class))).thenReturn(sampleAdvertisement);

        mockMvc.perform(post("/advertisements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"imageURL\":\"http://newimage.com/image.jpg\", \"description\":\"New Advertisement\", \"advertiserID\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Sample Advertisement"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void updateAdvertisement() throws Exception
    {
        AdvertisementDTO updatedAdvertisementDTO = new AdvertisementDTO();
        updatedAdvertisementDTO.setImageURL("http://updatedimage.com/image.jpg");
        updatedAdvertisementDTO.setDescription("Updated Advertisement");
        updatedAdvertisementDTO.setAdvertiserID(1);

        when(rAdvertisement.save(any(MAdvertisement.class))).thenReturn(sampleAdvertisement);

        mockMvc.perform(put("/advertisements/{ID}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"imageURL\":\"http://updatedimage.com/image.jpg\", \"description\":\"Updated Advertisement\", \"advertiserID\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Advertisement"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void deleteAdvertisement() throws Exception
    {
        int id = 1;

        doNothing().when(rAdvertisement).deleteById(id);

        mockMvc.perform(delete("/advertisements/{ID}", id))
                .andExpect(status().isOk());

        verify(rAdvertisement, times(1)).deleteById(id);
    }
}
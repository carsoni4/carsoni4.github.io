package supperSolver.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import supperSolver.DataTransferObjects.AdvertisementDTO;
import supperSolver.Models.MAdvertisement;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RAdvertisement;
import supperSolver.Repositories.RUser;
import java.util.List;
import java.util.Optional;

@RestController
public class AdvertisementController
{
    @Autowired
    RAdvertisement rAdvertisement;

    @Autowired
    RUser rUser;

    @Operation(summary = "Gets all advertisements")
    @GetMapping("advertisements")
    public List<MAdvertisement> getAdvertisements()
    {
        return rAdvertisement.findAll();
    }

    @Operation(summary = "Gets an advertisement by ID")
    @GetMapping("advertisements/ID/{ID}")
    public MAdvertisement getAdvertisementById(@PathVariable int ID)
    {
        return rAdvertisement.findById(ID).get();
    }

    @Operation(summary = "Creates a new advertisement")
    @PostMapping("advertisements")
    public MAdvertisement createAdvertisement(@RequestBody AdvertisementDTO advertisement)
    {
        MAdvertisement newAdvertisement = new MAdvertisement();
        newAdvertisement.setImageURL(advertisement.getImageURL());
        newAdvertisement.setDescription(advertisement.getDescription());

        MUser user = rUser.findById(advertisement.getAdvertiserID()).get();

        if(!user.getIsAdvertiser())
            return null;

        newAdvertisement.setAdvertiser(user);
        return rAdvertisement.save(newAdvertisement);
    }

    @Operation(summary = "Updates an existing advertisement")
    @PutMapping("advertisements/{ID}")
    public MAdvertisement updateAdvertisement(@PathVariable("ID") int ID, @RequestBody AdvertisementDTO advertisement)
    {
        MAdvertisement oldAdvertisement = rAdvertisement.findById(ID).get();
        oldAdvertisement.setImageURL(advertisement.getImageURL());
        oldAdvertisement.setDescription(advertisement.getDescription());

        MUser user = rUser.findById(advertisement.getAdvertiserID()).get();

        if(!user.getIsAdvertiser())
            return null;

        oldAdvertisement.setAdvertiser(user);
        return rAdvertisement.save(oldAdvertisement);
    }

    @Operation(summary = "Deletes an advertisement")
    @DeleteMapping("advertisements/{ID}")
    public void deleteAdvertisement(@PathVariable int ID)
    {
        Optional<MAdvertisement> advertisement = rAdvertisement.findById(ID);
        if (advertisement.isPresent())
            rAdvertisement.deleteById(ID);
    }

    @Operation(summary = "Deletes advertisements by userID")
    @DeleteMapping("advertisements/userID/{userID}")
    public void deleteAdvertisementsByUser(@PathVariable("userID") int userID)
    {
        List<MAdvertisement> advertisements = rAdvertisement.findByAdvertiserID(userID);
        if (advertisements != null && !advertisements.isEmpty())
            rAdvertisement.deleteAll(advertisements);
    }
}

package supperSolver.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_advertisement")
public class MAdvertisement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Column
    private String imageURL;

    @ManyToOne
    private MUser advertiser;

    @Column
    private String description;

    public MAdvertisement() { }

    public MAdvertisement(String imageURL, MUser advertiser, String description)
    {
        this.imageURL = imageURL;
        this.advertiser = advertiser;
        this.description = description;
    }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public MUser getAdvertiser() { return advertiser; }
    public void setAdvertiser(MUser advertiser) { this.advertiser = advertiser; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}


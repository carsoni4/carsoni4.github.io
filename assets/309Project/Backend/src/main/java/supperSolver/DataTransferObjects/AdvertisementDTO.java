package supperSolver.DataTransferObjects;

public class AdvertisementDTO
{
    private String imageURL;
    private int advertiserID;
    private String description;

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public int getAdvertiserID() { return advertiserID; }
    public void setAdvertiserID(int advertiserID) { this.advertiserID = advertiserID; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

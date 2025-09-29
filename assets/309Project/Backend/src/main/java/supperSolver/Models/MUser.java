package supperSolver.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_user")
public class MUser
{
    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    //Name Value
    @Column(name = "name")
    private String name;

    //Username Value
    @Column(name = "username")
    private String username;

    //Password Value
    @Column(name = "password")
    private String password;

    //Is Admin Boolean
    @Column(name = "isadmin")
    private boolean isAdmin;

    //Is Advertiser Boolean
    @Column(name = "isadvertiser")
    private boolean isAdvertiser;

    //Bio of User
    @Column(name = "bio")
    private String bio;

    //Group ID of User
    @Column(name = "groupID")
    private int groupID;

    //Empty Constructor
    public MUser() { }

    //Constructor of MUser Object
    public MUser(int ID, String name, String username, String password, boolean isAdmin, boolean isAdvertiser, String bio, int groupID)
    {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isAdvertiser = isAdvertiser;
        this.bio = bio;
        this.groupID = groupID;
    }

    //Getter of ID
    public int getID(){
        return ID;
    }
    public void setID(int ID) { this.ID = ID; }

    //Getter Setter of Name
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    //Getter Setter of Username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    //Getter Setter of Password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    //Getter Setter of Admin Boolean
    public boolean getIsAdmin(){
        return isAdmin;
    }
    public void setIsAdmin(boolean admin){
        this.isAdmin = admin;
    }

    //Getter Setter of Advertiser Boolean
    public boolean getIsAdvertiser(){
        return isAdvertiser;
    }
    public void setIsAdvertiser(boolean advertiser) {
        isAdvertiser = advertiser;
    }

    //Getter Setter of Bio Value
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    //Getter Setter of GroupID
    public int getGroupID() { return groupID; }
    public void setGroupID(int groupID) { this.groupID = groupID; }
}
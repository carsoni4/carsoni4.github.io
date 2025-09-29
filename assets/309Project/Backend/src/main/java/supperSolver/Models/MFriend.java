package supperSolver.Models;


import jakarta.persistence.*;

@Entity
@Table(name = "tbl_friend")
public class MFriend
{
    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    //Main user
    @ManyToOne
    @JoinColumn(name = "mainUser")
    private MUser mainUser;

    //Person they added
    @ManyToOne
    @JoinColumn(name = "friendAdded")
    private MUser friendAdded;

    //Empty Constructor
    public MFriend() { }

    //Friend Object Constructor
    public MFriend(MUser mainUser, MUser friendAdded)
    {
        this.mainUser = mainUser;
        this.friendAdded = friendAdded;
    }
    //Getter for ID
    public int getID() { return ID; }

    //Getter and Setter for mainUser
    public MUser getMainUser() { return mainUser; }
    public void setMainUser(MUser mainUser) { this.mainUser = mainUser; }

    //Getter and Setter for friendAdded
    public MUser getFriendAdded() { return friendAdded; }
    public void setFriendAdded(MUser friendAdded) { this.friendAdded = friendAdded; }

}

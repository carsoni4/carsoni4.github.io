package supperSolver.Models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "tbl_message")
@Data
public class MMessage {

    //Auto Generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Username Column
    @Column
    private String userName;

    //Content Of Message
    @Lob
    private String content;

    //Timestamp of Message
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    //Destination User (Null if to group)
    @Column
    private String destUser;

    //Destination GroupID (0 if to user)
    @Column
    private int destGroupID;

    public MMessage() {};

    public MMessage(String userName, String content, String destUser, int destGroupID) {
        this.userName = userName;
        this.content = content;
        this.destUser = destUser;
        this.destGroupID = destGroupID;
    }

    //Getter and Setter for ID
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    //Getter and Setter for Username
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    //Getter Setter for Content
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    //Getter Setter for Timestamp
    public Date getSent() {
        return sent;
    }
    public void setSent(Date sent) {
        this.sent = sent;
    }

    //Getter and Setter for DestUser (Pretty sure this uses username)
    public void setDestUser(String destUser) {this.destUser = destUser;}
    public String getDestUser(){return this.destUser;}

    //Getter and Setter for DestGroupID
    public void setDestGroupID(int destGroupID){this.destGroupID = destGroupID;}
    public int getDestGroupID(){return this.destGroupID;}


}

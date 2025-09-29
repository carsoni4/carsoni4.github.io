package supperSolver.DataTransferObjects;

public class LoginRequestDTO
{
    private String username;

    private String password;

    //Login Request Constructor
    public LoginRequestDTO(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public LoginRequestDTO() {}

    //Getter and Setter for Password
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    //Getter and Setter for Username
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
}

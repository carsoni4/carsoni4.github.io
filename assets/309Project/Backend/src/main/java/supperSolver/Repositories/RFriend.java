package supperSolver.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import supperSolver.Models.MFriend;
import supperSolver.Models.MImage;
import org.springframework.stereotype.Repository;
import supperSolver.Models.MUser;

import java.util.List;

@Repository
public interface RFriend extends JpaRepository<MFriend, Integer>
{
    // Finds all users that are friends with user
    @Query("SELECT friendAdded FROM MFriend WHERE mainUser = :user UNION SELECT mainUser FROM MFriend WHERE friendAdded = :user")
    List<MUser> findFriends(@Param("user") MUser user);

    List<MFriend> findByMainUserID(int id);
    List<MFriend> findByFriendAddedID(int id);
}

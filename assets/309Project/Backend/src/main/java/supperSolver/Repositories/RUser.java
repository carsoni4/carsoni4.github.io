package supperSolver.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import supperSolver.Models.MRecipe;
import org.springframework.stereotype.Repository;
import supperSolver.Models.MUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
public interface RUser extends JpaRepository<MUser, Integer>
{
    Optional<MUser> findByUsernameAndPassword(String username, String password);
    Optional<MUser> findByUsername(String username);
    Optional<List<MUser>> findByGroupID(int groupID);


    // Searching by username
    @Query("SELECT u FROM MUser AS u WHERE u.username LIKE %:username%")
    List<MUser> findByUsernameLike(@Param("username") String username);

    // Finding the maximum group ID
    @Query("SELECT COALESCE(MAX(u.groupID), 0) FROM MUser u")
    int findMaxGroupID();
}
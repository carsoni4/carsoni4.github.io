package supperSolver.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import supperSolver.Models.MRecipe;
import org.springframework.stereotype.Repository;
import java.util.List;
import supperSolver.Models.MUser;

@Repository
public interface RRecipe extends JpaRepository<MRecipe, Integer>
{
    List<MRecipe> findByUserID(@NonNull Integer userID);

    // Algorithm for home feed
    @Query("SELECT DISTINCT r.recipe FROM MIngredient AS r JOIN MIngredient AS u ON r.name = u.name WHERE u.user = :user AND r.recipe IS NOT NULL")
    List<Integer> algorithm(@Param("user") MUser user);

    // Searching by recipe name
    @Query("SELECT r FROM MRecipe AS r WHERE r.name LIKE %:name%")
    List<MRecipe> findByNameLike(@Param("name") String name);
}


package supperSolver.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import supperSolver.Models.MRating;
import java.util.List;

@Repository
public interface RRating extends JpaRepository<MRating, Integer> {
    List<MRating> findByUserID(@NonNull Integer userID);
    List<MRating> findByRecipeID(@NonNull Integer recipeID);

    boolean existsByUserIDAndRecipeID(int userID, int recipeID);
}
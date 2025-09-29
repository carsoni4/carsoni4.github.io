package supperSolver.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import supperSolver.Models.MImage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RImage extends JpaRepository<MImage, Integer>
{
    List<MImage> findByRecipeID(@NonNull int recipeID);
    MImage findByRecipeIDAndImagePos(@NonNull int recipeID, int imagePos);
    MImage findByUserID(@NonNull int userID);
    MImage findByUserIDAndImagePos(@NonNull int userID, int imagePos);
}

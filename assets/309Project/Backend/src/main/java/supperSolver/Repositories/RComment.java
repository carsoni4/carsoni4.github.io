package supperSolver.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import supperSolver.Models.MComment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import supperSolver.Models.MRating;

import java.util.List;

@Repository
public interface RComment extends JpaRepository<MComment, Integer>
{
    List<MComment> findByUserID(int userID);
    List<MComment> findByRecipeID(int recipeID);
}

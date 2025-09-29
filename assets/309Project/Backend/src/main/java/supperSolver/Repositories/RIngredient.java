package supperSolver.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import supperSolver.Models.MComment;
import supperSolver.Models.MIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface RIngredient extends JpaRepository<MIngredient, Integer>
{
    Optional<List<MIngredient>> findByRecipeID(int recipeID);
    Optional<List<MIngredient>> findByUserID(int userID);
}

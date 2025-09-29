package supperSolver.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import supperSolver.Models.MAdvertisement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RAdvertisement extends JpaRepository<MAdvertisement, Integer>
{

    List<MAdvertisement> findByAdvertiserID(int userID);
}

package supperSolver.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import supperSolver.Models.MMessage;


import java.util.List;
import java.util.Optional;

public interface RMessage extends JpaRepository<MMessage, Long>
{
    Optional<List<MMessage>> findByDestUserAndUserName(String destUser, String userName);
    Optional<List<MMessage>> findByDestGroupID(int destGroupID);
}

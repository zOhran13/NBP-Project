package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name IN :roleNames")
    List<User> findAllByRoleNames(@org.springframework.data.repository.query.Param("roleNames") List<String> roleNames);

}

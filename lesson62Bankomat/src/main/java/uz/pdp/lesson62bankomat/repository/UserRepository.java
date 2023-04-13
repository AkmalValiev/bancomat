package uz.pdp.lesson62bankomat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}

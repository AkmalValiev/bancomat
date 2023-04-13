package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.Card;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsByCardNumber(String cardNumber);
    Optional<Card> findByCardNumber(String cardNumber);

}

package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.TypeCard;

public interface TypeCardRepository extends JpaRepository<TypeCard, Integer> {

    boolean existsByName(String name);
    boolean existsByNumber(String number);

}

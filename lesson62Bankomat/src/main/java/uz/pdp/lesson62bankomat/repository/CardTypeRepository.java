package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.TypeCard;

public interface CardTypeRepository extends JpaRepository<TypeCard, Integer> {
}

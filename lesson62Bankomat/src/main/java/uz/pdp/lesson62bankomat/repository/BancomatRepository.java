package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.Bancomat;

public interface BancomatRepository extends JpaRepository<Bancomat, Integer> {
}

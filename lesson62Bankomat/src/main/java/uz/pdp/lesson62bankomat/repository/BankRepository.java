package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {

    boolean existsByName(String name);
    boolean existsByNumber(String number);

}

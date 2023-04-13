package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.BankTransferInKirimlar;

import java.util.UUID;

public interface BankTransferInKirimlarRepository extends JpaRepository<BankTransferInKirimlar, UUID> {
}

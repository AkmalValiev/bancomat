package uz.pdp.lesson62bankomat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson62bankomat.entity.BankTransferInKirimlar;
import uz.pdp.lesson62bankomat.entity.User;
import uz.pdp.lesson62bankomat.entity.enums.RoleName;
import uz.pdp.lesson62bankomat.repository.BankTransferInKirimlarRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankTransferInKirimlarService {
    @Autowired
    BankTransferInKirimlarRepository bankTransferInKirimlarRepository;
    public List<BankTransferInKirimlar> getTransfers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.DIRECTOR)){
          return bankTransferInKirimlarRepository.findAll();
        }
        return new ArrayList<>();
    }
}

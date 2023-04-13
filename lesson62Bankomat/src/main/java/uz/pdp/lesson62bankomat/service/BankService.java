package uz.pdp.lesson62bankomat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson62bankomat.entity.Bank;
import uz.pdp.lesson62bankomat.entity.User;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.repository.BankRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BankService {
    @Autowired
    BankRepository bankRepository;
    public ApiResponse addBank(String bankName) {
        boolean existsByName = bankRepository.existsByName(bankName);
        if (existsByName)
            return new ApiResponse("Bunday bank mavjud!", false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals("DIRECTOR")) {
            String number = "";
            while (true){
                int num = ThreadLocalRandom.current().nextInt(100000, 1000000);
                boolean existsByNumber = bankRepository.existsByNumber(String.valueOf(num));
                if (!existsByNumber) {
                    number = String.valueOf(num);
                    break;
                }
            }
            Bank bank = new Bank();
            bank.setName(bankName);
            bank.setNumber(number);
            bankRepository.save(bank);
            return new ApiResponse("Bank qo'shildi!", true);
        }
        return new ApiResponse("Sizda bank qo'shish huquqi mavjud emas!", false);
    }

    public List<Bank> getBanks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals("DIRECTOR")) {
            return bankRepository.findAll();
        }
        return new ArrayList<>();
    }

    public Bank getBank(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals("DIRECTOR")) {
            Optional<Bank> optionalBank = bankRepository.findById(id);
            if (optionalBank.isPresent())
                return optionalBank.get();
            return new Bank();
        }
        return new Bank();
    }
}

package uz.pdp.lesson62bankomat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson62bankomat.entity.Bancomat;
import uz.pdp.lesson62bankomat.entity.Bank;
import uz.pdp.lesson62bankomat.entity.TypeCard;
import uz.pdp.lesson62bankomat.entity.User;
import uz.pdp.lesson62bankomat.entity.enums.RoleName;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.BancomatDto;
import uz.pdp.lesson62bankomat.payload.BancomatLoadMoneyDto;
import uz.pdp.lesson62bankomat.repository.BancomatRepository;
import uz.pdp.lesson62bankomat.repository.BankRepository;
import uz.pdp.lesson62bankomat.repository.TypeCardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BancomatService {
    @Autowired
    BancomatRepository bancomatRepository;
    @Autowired
    TypeCardRepository typeCardRepository;
    @Autowired
    BankRepository bankRepository;
    public List<Bancomat> getAllBancomat() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.DIRECTOR) || user.getRoles().get(0).getRoleName().equals(RoleName.MANAGER)){
            return bancomatRepository.findAll();
        }
        return new ArrayList<>();
    }

    public ApiResponse addBancomat(BancomatDto bancomatDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.DIRECTOR)) {

            Optional<TypeCard> optionalTypeCard = typeCardRepository.findById(bancomatDto.getTypeCardId());
            if (!optionalTypeCard.isPresent())
                return new ApiResponse("Kiritilgan id bo'yicha typeCard topilmadi!", false);

            Optional<Bank> optionalBank = bankRepository.findById(bancomatDto.getBankId());
            if (!optionalBank.isPresent())
                return new ApiResponse("Kiritilgan id bo'yicha bank topilmadi!", false);

            Bancomat bancomat = new Bancomat();
            bancomat.setTypeCard(optionalTypeCard.get());
            bancomat.setBank(optionalBank.get());
            bancomatRepository.save(bancomat);
            return new ApiResponse("Bancomat qo'shildi!", true);

        }
        return new ApiResponse("Sizda bancomat qo'shish imkoni mavjud emas!", false);
    }

    public ApiResponse loadMoney(BancomatLoadMoneyDto bancomatLoadMoneyDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.MANAGER) && user.getUsername().startsWith("Bancomat")){
            Optional<Bancomat> optionalBancomat = bancomatRepository.findById(bancomatLoadMoneyDto.getBancomatId());
            if (!optionalBancomat.isPresent())
                return new ApiResponse("Bancomat topilmadi!", false);
            Bancomat bancomat = optionalBancomat.get();
            bancomat.setBalance(bancomat.getBalance()+bancomatLoadMoneyDto.getAmountMoney());
            bancomatRepository.save(bancomat);
            return new ApiResponse("Bankomatga pul to'ldirildi!", true);
        }
        return new ApiResponse("Sizda bancomatga pul solish huquqi yo'q!", false);
    }
}

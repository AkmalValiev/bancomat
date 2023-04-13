package uz.pdp.lesson62bankomat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson62bankomat.entity.TypeCard;
import uz.pdp.lesson62bankomat.entity.User;
import uz.pdp.lesson62bankomat.entity.enums.RoleName;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.TypeCardDto;
import uz.pdp.lesson62bankomat.repository.TypeCardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeCardService {
    @Autowired
    TypeCardRepository typeCardRepository;
    public ApiResponse addTypeCard(TypeCardDto typeCardDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.MANAGER)) {
            if (typeCardRepository.existsByName(typeCardDto.getName()))
                return new ApiResponse("Bunday turli karta mavjud!", false);

            if (typeCardRepository.existsByNumber(typeCardDto.getNumber()))
                return new ApiResponse("Bunday raqamli cardtype mavjud!", false);

            TypeCard typeCard = new TypeCard();
            typeCard.setName(typeCardDto.getName());
            typeCard.setNumber(typeCardDto.getNumber());
            typeCardRepository.save(typeCard);
            return new ApiResponse("Card type qo'shildi!", true);

        }
        return new ApiResponse("Sizda cardtype qo'shish imkoni mavjud emas!", false);

    }

    public List<TypeCard> getAllCardType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.MANAGER)) {
            List<TypeCard> all = typeCardRepository.findAll();
            return all;
        }
        return new ArrayList<>();
    }
}

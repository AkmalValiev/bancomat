package uz.pdp.lesson62bankomat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson62bankomat.entity.*;
import uz.pdp.lesson62bankomat.entity.enums.RoleName;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.CardDto;
import uz.pdp.lesson62bankomat.payload.TransferDto;
import uz.pdp.lesson62bankomat.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CardService {
    int block = 0;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    CardTypeRepository cardTypeRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    BancomatRepository bancomatRepository;
    @Autowired
    BankTransferInKirimlarRepository bankTransferInKirimlarRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;

    public ApiResponse addCard(CardDto cardDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals("MANAGER")) {
            Optional<Bank> optionalBank = bankRepository.findById(cardDto.getBankId());
            if (!optionalBank.isPresent())
                return new ApiResponse("Kiritilgan id bo'yicha bank topilmadi!", false);
            Optional<TypeCard> optionalTypeCard = cardTypeRepository.findById(cardDto.getTypeCardId());
            if (!optionalTypeCard.isPresent())
                return new ApiResponse("Kiritilgan id bo'yicha typeCard topilmadi!", false);
            Bank bank = optionalBank.get();
            TypeCard typeCard = optionalTypeCard.get();

            String number = "";
            while (true) {
                int num = ThreadLocalRandom.current().nextInt(100000, 1000000);
                String number1 = typeCard.getNumber() + bank.getNumber() + num;
                boolean existsByCardNumber = cardRepository.existsByCardNumber(number1);
                if (!existsByCardNumber) {
                    number = number1;
                    break;
                }
            }

            String cvvCode = "";
            int cvvCode1 = ThreadLocalRandom.current().nextInt(100, 1000);
            cvvCode = String.valueOf(cvvCode1);

            String date = LocalDate.now().toString().substring(0, 7);
            int date2 = Integer.parseInt(date.substring(0, 4)) + 4;
            String expireDate = date2 + date.substring(4);

            Card card = new Card();
            card.setCardNumber(number);
            card.setBank(bank);
            card.setCvvCode(cvvCode);
            card.setFullNameOfOwner(cardDto.getFullNameOfOwner());
            card.setExpireDate(expireDate);
            card.setPinCode(cardDto.getPinCode());
            card.setTypeCard(typeCard);
            cardRepository.save(card);
            return new ApiResponse("Kard qo'shildi!", true);
        }
        return new ApiResponse("Sizda card qo'shish imkoniyati mavjud emas!", false);
    }

    public ApiResponse switchCard(Integer id, boolean onOrOff) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals("MANAGER")) {
            Optional<Card> optionalCard = cardRepository.findById(id);
            if (!optionalCard.isPresent())
                return new ApiResponse("Card topilmadi!", false);
            Card card = optionalCard.get();
            card.setActive(onOrOff);
            cardRepository.save(card);
            String switchCard = "";
            if (onOrOff) {
                switchCard = "Yoqildi!";
            } else {
                switchCard = "O'chirildi!";
            }
            return new ApiResponse("Karta " + switchCard, true);
        }
        return new ApiResponse("Kartani o'chirish yoki yo'qish imkoniyati sizda yo'q!", false);
    }

    public ApiResponse transfer(TransferDto transferDto) {
        Optional<Bancomat> optionalBancomat = bancomatRepository.findById(transferDto.getBankomatId());
        if (!optionalBancomat.isPresent())
            return new ApiResponse("Bankomat topilmadi!", false);
        Bancomat bancomat = optionalBancomat.get();

        Optional<Bank> optionalBank = bankRepository.findById(bancomat.getBank().getId());
        if (!optionalBank.isPresent())
            return new ApiResponse("Bank topilmadi!", false);
        Bank bank = optionalBank.get();

        Optional<Card> optionalCard = cardRepository.findByCardNumber(transferDto.getCardNumber());
        if (!optionalCard.isPresent())
            return new ApiResponse("Card topilmadi!", false);
        Card card = optionalCard.get();

        if (!bancomat.getTypeCard().getName().equals(card.getTypeCard().getName()))
            return new ApiResponse("Karta turi to'g'ri kelmadi!", false);

        if (!card.isActive())
            return new ApiResponse("Karta blok xolatida, bankga murojat qiling!", false);

        if (card.getPinCode()!=transferDto.getPinCode() && block<1) {
            block++;
            return new ApiResponse("Pin code xato! Yana bir marta xato kiritsangiz karta blokga tushadi!", false);
        }else if (card.getPinCode()!=transferDto.getPinCode() && block<2){
            card.setActive(false);
            cardRepository.save(card);
            block=0;
            return new ApiResponse("Pin code xato! Karta blokga tushdi, bankga murojat qiling!", false);
        }

        String expireDate = card.getExpireDate();
        int yearCard = Integer.parseInt(expireDate.substring(0,4));
        int monthCard = Integer.parseInt(expireDate.substring(5));
        String date = LocalDate.now().toString().substring(0, 7);
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5));
        if (year>yearCard || (year==yearCard && month>monthCard))
            return new ApiResponse("Kartaning muddati tugagan!", false);

        double resultAmount=0.0;
        if (bank.getName().equals(card.getBank().getName())) {
            resultAmount = (transferDto.getAmountIn() * bancomat.getCommissionAmount1())/100;
        }else {
            resultAmount = (transferDto.getAmountIn() * bancomat.getCommissionAmount2())/100;
        }
        bank.setBalance(bank.getBalance()+transferDto.getAmountIn());
        bank.setAmountIn(bank.getAmountIn()+resultAmount);
        bankRepository.save(bank);

        bancomat.setBalance(bancomat.getBalance()+transferDto.getAmountIn());
        bancomatRepository.save(bancomat);

        BankTransferInKirimlar bankTransferInKirimlar = new BankTransferInKirimlar();
        bankTransferInKirimlar.setAddAmountIn("+"+resultAmount);
        bankTransferInKirimlar.setAddAmountBalance("+"+transferDto.getAmountIn());
        bankTransferInKirimlar.setCardNumber(card.getCardNumber());
        bankTransferInKirimlar.setFullNameOfOwner(card.getFullNameOfOwner());
        bankTransferInKirimlar.setBancomat(bancomat);
        bankTransferInKirimlarRepository.save(bankTransferInKirimlar);

        card.setBalance(card.getBalance()+(transferDto.getAmountIn()-resultAmount));
        cardRepository.save(card);
        return new ApiResponse("Kartaga "+transferDto.getAmountIn()+" pul qo'shildi! "+resultAmount+" komissiya olindi!", true);
    }

    public ApiResponse transferOut(TransferDto transferDto) {
        Optional<Bancomat> optionalBancomat = bancomatRepository.findById(transferDto.getBankomatId());
        if (!optionalBancomat.isPresent())
            return new ApiResponse("Bankomat topilmadi!", false);
        Bancomat bancomat = optionalBancomat.get();

        Optional<Bank> optionalBank = bankRepository.findById(bancomat.getBank().getId());
        if (!optionalBank.isPresent())
            return new ApiResponse("Bank topilmadi!", false);
        Bank bank = optionalBank.get();

        Optional<Card> optionalCard = cardRepository.findByCardNumber(transferDto.getCardNumber());
        if (!optionalCard.isPresent())
            return new ApiResponse("Card topilmadi!", false);
        Card card = optionalCard.get();

        if (!bancomat.getTypeCard().getName().equals(card.getTypeCard().getName()))
            return new ApiResponse("Karta turi to'g'ri kelmadi!", false);

        if (!card.isActive())
            return new ApiResponse("Karta blok xolatida, bankga murojat qiling!", false);

        if (card.getPinCode()!=transferDto.getPinCode() && block<1) {
            block++;
            return new ApiResponse("Pin code xato! Yana bir marta xato kiritsangiz karta blokga tushadi!", false);
        }else if (card.getPinCode()!=transferDto.getPinCode() && block<2){
            card.setActive(false);
            cardRepository.save(card);
            block=0;
            return new ApiResponse("Pin code xato! Karta blokga tushdi, bankga murojat qiling!", false);
        }

        String expireDate = card.getExpireDate();
        int yearCard = Integer.parseInt(expireDate.substring(0,4));
        int monthCard = Integer.parseInt(expireDate.substring(5));
        String date = LocalDate.now().toString().substring(0, 7);
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5));
        if (year>yearCard || (year==yearCard && month>monthCard))
            return new ApiResponse("Kartaning muddati tugagan!", false);

        double resultAmount=0.0;
        if (bank.getName().equals(card.getBank().getName())) {
            resultAmount = (transferDto.getAmountIn() * bancomat.getCommissionAmount1())/100;
        }else {
            resultAmount = (transferDto.getAmountIn() * bancomat.getCommissionAmount2())/100;
        }

        if (bancomat.getBalance()>transferDto.getAmountIn()){
            if (card.getBalance()>(transferDto.getAmountIn()+resultAmount)){
                bancomat.setBalance(bancomat.getBalance()-transferDto.getAmountIn());
                card.setBalance(card.getBalance()-(transferDto.getAmountIn()+resultAmount));
                bank.setAmountOut(bank.getAmountOut()+resultAmount);
                bancomatRepository.save(bancomat);
                cardRepository.save(card);
                bankRepository.save(bank);

                BankTransferInKirimlar bankTransferInKirimlar = new BankTransferInKirimlar();
                bankTransferInKirimlar.setAddAmountOut("+"+resultAmount);
                bankTransferInKirimlar.setAddAmountBalance("-"+transferDto.getAmountIn());
                bankTransferInKirimlar.setCardNumber(card.getCardNumber());
                bankTransferInKirimlar.setFullNameOfOwner(card.getFullNameOfOwner());
                bankTransferInKirimlar.setBancomat(bancomat);
                bankTransferInKirimlarRepository.save(bankTransferInKirimlar);

                if (bancomat.getMinSumAmount()<bancomat.getBalance()){
                    for (User user : userRepository.findAll()) {
                        if (user.getUsername().startsWith("Bancomat")){
                            String email = user.getUsername().substring(9);
                            sendEmail(email, bancomat);
                        }
                    }

                }
                return new ApiResponse("Kartadan "+transferDto.getAmountIn()+" pulechildi, "+resultAmount+" komissiya olindi!", true);
            }else {
                return new ApiResponse("Kartada etarli mablag' mavjud emas!", false);
            }
        }else {
            return new ApiResponse("Bancomatda pul etishmadi!", false);
        }
    }

    public boolean sendEmail(String sendingEmail, Bancomat bancomat){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Bankomatda pul kamaydi!");
            mailMessage.setText(bancomat.getId()+" id li bankomatda pul miqdori kamaydi!");
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<Card> getAllCard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.DIRECTOR)){
           return cardRepository.findAll();
        }
        return new ArrayList<>();
    }
}

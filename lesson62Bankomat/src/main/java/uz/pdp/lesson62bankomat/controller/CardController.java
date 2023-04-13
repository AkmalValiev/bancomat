package uz.pdp.lesson62bankomat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson62bankomat.entity.Card;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.CardDto;
import uz.pdp.lesson62bankomat.payload.TransferDto;
import uz.pdp.lesson62bankomat.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping
    public HttpEntity<?> addCard(@RequestBody CardDto cardDto){
        ApiResponse apiResponse = cardService.addCard(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    //kartani yo'qish yoki o'chirish
    @PutMapping("/switchCard/{id}")
    public HttpEntity<?> switchCard(@PathVariable Integer id, boolean onOrOff){
        ApiResponse apiResponse = cardService.switchCard(id, onOrOff);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    //kartaga pul to'ldirish
    @PostMapping("/transfer")
    public HttpEntity<?> transfer(@RequestBody TransferDto transferDto){
        ApiResponse apiResponse = cardService.transfer(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    //kartadan pul echish
    @PostMapping("/transferOut")
    public HttpEntity<?> transferOut(@RequestBody TransferDto transferDto){
        ApiResponse apiResponse = cardService.transferOut(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllCard(){
        List<Card> cards = cardService.getAllCard();
        return ResponseEntity.ok(cards);
    }

}

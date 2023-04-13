package uz.pdp.lesson62bankomat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson62bankomat.entity.TypeCard;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.TypeCardDto;
import uz.pdp.lesson62bankomat.service.TypeCardService;

import java.util.List;

@RestController
@RequestMapping("/api/typeCard")
public class TypeCardController {

    @Autowired
    TypeCardService typeCardService;

    @PostMapping
    public HttpEntity<?> addCard(@RequestBody TypeCardDto typeCardDto){
        ApiResponse apiResponse = typeCardService.addTypeCard(typeCardDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllCardType(){
        List<TypeCard> typeCards = typeCardService.getAllCardType();
        return ResponseEntity.ok(typeCards);
    }
}

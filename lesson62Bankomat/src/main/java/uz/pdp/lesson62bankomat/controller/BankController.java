package uz.pdp.lesson62bankomat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson62bankomat.entity.Bank;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.service.BankService;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    @Autowired
    BankService bankService;

    @PostMapping
    public HttpEntity<?> addBank(String bankName){
        ApiResponse apiResponse = bankService.addBank(bankName);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getBanks(){
       List<Bank> banks = bankService.getBanks();
       return ResponseEntity.ok(banks);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getBank(@PathVariable Integer id){
        Bank bank = bankService.getBank(id);
        return ResponseEntity.ok(bank);
    }

}

package uz.pdp.lesson62bankomat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson62bankomat.entity.Bancomat;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.BancomatDto;
import uz.pdp.lesson62bankomat.payload.BancomatLoadMoneyDto;
import uz.pdp.lesson62bankomat.service.BancomatService;

import java.util.List;

@RestController
@RequestMapping("/api/bancomat")
public class BancomatController {

    @Autowired
    BancomatService bancomatService;

    @GetMapping
    public HttpEntity<?> getAllBancomat(){
        List<Bancomat> bancomats = bancomatService.getAllBancomat();
        return ResponseEntity.ok(bancomats);
    }

    @PostMapping
    public HttpEntity<?> addBancomat(@RequestBody BancomatDto bancomatDto){
       ApiResponse apiResponse = bancomatService.addBancomat(bancomatDto);
       return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PostMapping("/loadMoneyToBancomat")
    public HttpEntity<?> loadMoney(@RequestBody BancomatLoadMoneyDto bancomatLoadMoneyDto){
        ApiResponse apiResponse = bancomatService.loadMoney(bancomatLoadMoneyDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

}

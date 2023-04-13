package uz.pdp.lesson62bankomat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.lesson62bankomat.entity.BankTransferInKirimlar;
import uz.pdp.lesson62bankomat.service.BankTransferInKirimlarService;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class BankTransferInKirimlarController {

    @Autowired
    BankTransferInKirimlarService bankTransferInKirimlarService;

    @GetMapping
    public HttpEntity<?> getTransfers(){
        List<BankTransferInKirimlar> bankTransferInKirimlars = bankTransferInKirimlarService.getTransfers();
        return ResponseEntity.ok(bankTransferInKirimlars);
    }

}

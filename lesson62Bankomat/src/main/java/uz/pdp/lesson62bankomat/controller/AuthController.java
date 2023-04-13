package uz.pdp.lesson62bankomat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson62bankomat.entity.User;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.LoginDto;
import uz.pdp.lesson62bankomat.payload.RegisterDto;
import uz.pdp.lesson62bankomat.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto){
       ApiResponse apiResponse = authService.register(registerDto);
       return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/users")
    public HttpEntity<?> getAllUser(){
        List<User> users = authService.getAllUser();
        return ResponseEntity.ok(users);
    }

}

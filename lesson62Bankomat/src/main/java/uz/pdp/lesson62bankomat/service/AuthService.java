package uz.pdp.lesson62bankomat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson62bankomat.entity.User;
import uz.pdp.lesson62bankomat.entity.enums.RoleName;
import uz.pdp.lesson62bankomat.payload.ApiResponse;
import uz.pdp.lesson62bankomat.payload.LoginDto;
import uz.pdp.lesson62bankomat.payload.RegisterDto;
import uz.pdp.lesson62bankomat.repository.RoleRepository;
import uz.pdp.lesson62bankomat.repository.UserRepository;
import uz.pdp.lesson62bankomat.security.JwtProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    public ApiResponse register(RegisterDto registerDto) {
        boolean existsByUsername = userRepository.existsByUsername(registerDto.getUsername());
        if (existsByUsername)
            return new ApiResponse("Bunday user mavjud!", false);
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 =(User) authentication.getPrincipal();
        if (user1.getRoles().get(0).getRoleName().equals(RoleName.DIRECTOR)) {
            user.setRoles(Collections.singletonList(roleRepository.findByRoleName(RoleName.MANAGER)));
            userRepository.save(user);
            return new ApiResponse("Manager qo'shildi!", true);
        }else if (user1.getRoles().get(0).getRoleName().equals(RoleName.MANAGER)){
            user.setRoles(Collections.singletonList(roleRepository.findByRoleName(RoleName.USER)));
            userRepository.save(user);
            return new ApiResponse("User qo'shildi!", true);
        }else {
            return new ApiResponse("Sizda user qo'shish imkoniyati mavjud emas!", false);
        }
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse(token, true);
        }catch (Exception e){
            return new ApiResponse("Login yoki parol xato!", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User topilmadi!"));
    }

    public List<User> getAllUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().get(0).getRoleName().equals(RoleName.DIRECTOR))
        return userRepository.findAll();
        return new ArrayList<>();
    }
}

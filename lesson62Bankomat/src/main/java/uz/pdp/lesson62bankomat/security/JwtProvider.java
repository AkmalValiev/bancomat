package uz.pdp.lesson62bankomat.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.lesson62bankomat.entity.Role;

import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private static final long expireTime =1000*60*60*20;
    private static final String secretKey = "1234567890";

    public String generateToken(String username, List<Role> roles){
        Date expireDate = new Date(System.currentTimeMillis()+expireTime);

        String token = Jwts
                .builder()
                .claim("role", roles)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }

    public String getUsernameFromToken(String token){
        try {
            String username = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return username;
        }catch (Exception e){
            return null;
        }
    }


}

package uz.pdp.lesson62bankomat.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    private String password;

}

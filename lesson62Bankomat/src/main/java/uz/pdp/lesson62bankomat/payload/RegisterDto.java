package uz.pdp.lesson62bankomat.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @Size(min = 3, max = 50)
    @NotNull
    private String firstName;

    @Size(min = 3, max = 50)
    @NotNull
    private String lastName;

    @Size(min = 3, max = 50)
    @NotNull
    private String username;

    @NotNull
    private String password;

}

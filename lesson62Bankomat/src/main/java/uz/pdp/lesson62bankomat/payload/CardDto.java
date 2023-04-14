package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @NotNull
    private Integer bankId;

    @Size(min = 3, max = 50)
    @NotNull
    private String fullNameOfOwner;

    @Size(min = 4, max = 4)
    @NotNull
    private Integer pinCode;

    @NotNull
    private Integer typeCardId;

}

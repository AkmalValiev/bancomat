package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @NotNull
    private Integer bankId;

    @NotNull
    private String fullNameOfOwner;

    @NotNull
    private Integer pinCode;

    @NotNull
    private Integer typeCardId;

}

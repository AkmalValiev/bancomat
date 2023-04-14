package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {

    @Size(min = 16, max = 16)
    @NotNull
    private String cardNumber;

    @Size(min = 4, max = 4)
    @NotNull
    private Integer pinCode;
    @NotNull
    private Integer bankomatId;
    private Double amountIn=0.0;//kartaga tashlamoqchi bo'lgan pul miqdori yoki echmoqchi bo'lgan pul miqdori!

}

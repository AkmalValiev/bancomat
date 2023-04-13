package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {

    @NotNull
    private String cardNumber;
    @NotNull
    private Integer pinCode;
    @NotNull
    private Integer bankomatId;
    private Double amountIn=0.0;//kartaga tashlamoqchi bo'lgan pul miqdori yoki echmoqchi bo'lgan pul miqdori!

}

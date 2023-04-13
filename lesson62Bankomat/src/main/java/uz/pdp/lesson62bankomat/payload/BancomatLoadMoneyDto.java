package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BancomatLoadMoneyDto {

    @NotNull
    private Integer bancomatId;

    @NotNull
    private Double amountMoney;

}

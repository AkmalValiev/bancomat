package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BancomatDto {

    @NotNull
    private Integer typeCardId;

    @NotNull
    public Integer bankId;

}

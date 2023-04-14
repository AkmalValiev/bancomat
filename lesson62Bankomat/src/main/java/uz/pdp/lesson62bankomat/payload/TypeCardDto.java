package uz.pdp.lesson62bankomat.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeCardDto {

    @Size(min = 3, max = 50)
    @NotNull
    private String name;

    @Size(min = 4, max = 4)
    @NotNull
    private String number;

}

package uz.pdp.lesson62bankomat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bancomats")
public class Bancomat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private TypeCard typeCard;

    @ManyToOne
    private Bank bank;

    private Double balance=0.0;

    @Column(nullable = false)
    private Double maxBorder=1000000.0;//Bankomatdan maksimum echiladigan pul miqdori

    private Integer commissionAmount1=1;//Shu bankga tegishli kartalar uchun foiz(%) miqdorda beriladi
    private Integer commissionAmount2=2;//Shu bankga tegishli bo'lmagan kartalar uchun foiz(%) miqdorda beriladi
    private Double minSumAmount=10000000.0;//Bankomatda qancha pul qolganda, bankamatga masul shaxsga emailiga xat yuborish


}

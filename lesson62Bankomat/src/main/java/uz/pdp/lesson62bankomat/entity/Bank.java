package uz.pdp.lesson62bankomat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true, nullable =false)
    private String number;//Bankning 6 xonali raqami

    private Double amountIn=0.0;//kartaga bankomat orqali pul solganda tushadigan foyda
    private Double amountOut=0.0;//kartadan bankomat orqali pul echilganda tushadigan foyda

    private Double balance=0.0;

}

package uz.pdp.lesson62bankomat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BankTransferInKirimlar {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private Timestamp createdAt;

    private String addAmountIn;
    private String addAmountOut;
    private String addAmountBalance;
    private String cardNumber;
    private String fullNameOfOwner;
    @ManyToOne
    private Bancomat bancomat;

}

package uz.pdp.lesson62bankomat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    private Double balance=0.0;

    @ManyToOne
    private Bank bank;

    @Column(nullable = false, unique = true)
    private String cvvCode;

    @Column(nullable = false)
    private String fullNameOfOwner;

    @Column(nullable = false)
    private String expireDate;

    @Column(nullable = false)
    private Integer pinCode;

    @ManyToOne
    private TypeCard typeCard;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;

    private boolean active=true;
}

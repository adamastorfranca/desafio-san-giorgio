package br.com.desafio.domain;

import br.com.desafio.domain.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE payments SET deleted_at = current_timestamp WHERE id=? AND version=?")
public class Payment extends AbstractAuditingEntityCustom {

    @Id
    @GeneratedValue
    @Column(name = "payment_id", nullable = false, updatable = false, unique = true)
    private UUID paymentId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "payment_value", nullable = false, precision = 8, scale = 2)
    private BigDecimal paymentValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

}

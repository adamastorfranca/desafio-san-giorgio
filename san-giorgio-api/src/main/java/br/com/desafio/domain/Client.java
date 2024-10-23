package br.com.desafio.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE clients SET deleted_at = current_timestamp WHERE id=? AND version=?")
public class Client extends AbstractAuditingEntityCustom {

    @Id
    @GeneratedValue
    @Column(name = "client_id", nullable = false, updatable = false, unique = true)
    private UUID clientId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Payment> payments;

}

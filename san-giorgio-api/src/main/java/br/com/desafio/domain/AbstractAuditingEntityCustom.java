package br.com.desafio.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntityCustom {

     @CreatedDate
     @Column(name = "created_at")
     private LocalDateTime createdAt;

     @LastModifiedDate
     @Column(name = "updated_at")
     private LocalDateTime updatedAt;

     @Column(name = "deleted_at")
     private LocalDateTime deletedAt;

     @Version
     @Column(name = "version")
     private Long version;

}

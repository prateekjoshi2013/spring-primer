package com.prateek.web.springrestdemo.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;
import org.hibernate.type.SqlTypes;

import com.prateek.web.springrestdemo.model.BeerStyle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeerAudit {
    @Id
    // new in hibernate 6:
    @UuidGenerator(style = Style.TIME)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID auditId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    private Integer version;

    @Column(length = 50) // hibernate uses default varchar column size to 255 unless length is specified
    private String beerName;

    @JdbcTypeCode(value = SqlTypes.INTEGER)
    private BeerStyle beerStyle;

    @Size(min = 1, max = 10)
    private String upc;

    private Integer quantityOnHand;

    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    @CreationTimestamp
    private LocalDateTime createdDateAudit;

    private String principalName;

    private String auditEventType;
}

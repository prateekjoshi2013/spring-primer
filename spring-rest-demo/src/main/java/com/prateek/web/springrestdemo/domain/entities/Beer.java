package com.prateek.web.springrestdemo.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;
import org.hibernate.type.SqlTypes;

import com.prateek.web.springrestdemo.model.BeerStyle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Beer {
    @Id
    // new in hibernate 6:
    @UuidGenerator(style = Style.RANDOM)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    @NotEmpty
    @Size(min = 1, max = 50)
    @Column(length = 50) // hibernate uses default varchar column size to 255 unless length is specified
    private String beerName;
    @NotNull
    @Column(length = 10, columnDefinition = "varchar(10)", updatable = false, nullable = false)
    private BeerStyle beerStyle;
    @NotEmpty
    @Size(min = 1, max = 10)
    private String upc;
    private Integer quantityOnHand;
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

}

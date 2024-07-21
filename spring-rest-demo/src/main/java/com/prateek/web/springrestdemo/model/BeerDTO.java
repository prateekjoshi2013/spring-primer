package com.prateek.web.springrestdemo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {

    private UUID id;
    private Integer version;
    @NotEmpty
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotEmpty
    private String upc;
    private Integer quantityOnHand;
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

}

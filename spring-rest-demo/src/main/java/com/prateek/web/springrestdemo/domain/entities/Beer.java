package com.prateek.web.springrestdemo.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    @JdbcTypeCode(value = SqlTypes.INTEGER)
    private BeerStyle beerStyle;
    @NotEmpty
    @Size(min = 1, max = 10)
    private String upc;
    private Integer quantityOnHand;
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @OneToMany(mappedBy = "beer")
    private Set<BeerOrderLine> beerOrderLines;
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "beer_category", joinColumns = @JoinColumn(name = "beer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getBeers().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getBeers().remove(this);
    }
}

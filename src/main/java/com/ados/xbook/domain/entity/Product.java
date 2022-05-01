package com.ados.xbook.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_product")
public class Product extends BaseEntity {

    @Nationalized
    @Column(nullable = false, unique = true)
    private String title;

    @Nationalized
    @JsonProperty("short_description")
    private String shortDescription;

    @Lob
    @Nationalized
    @Length(max = 100000)
    @JsonProperty("long_description")
    private String longDescription;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductRate> productRates;

    private Double price;

    @Nationalized
    private String author;

    @JsonProperty("current_number")
    private Integer currentNumber;

    @JsonProperty("number_of_page")
    private Integer numberOfPage;

    @Column(nullable = false, unique = true)
    private String slug;

    @JsonProperty("quantity_selled")
    private Integer quantitySelled;

}

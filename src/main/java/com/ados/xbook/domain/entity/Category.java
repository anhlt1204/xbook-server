package com.ados.xbook.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_category")
public class Category extends BaseEntity {

    @Nationalized
    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    @Nationalized
    private String description;

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "parents_category_id")
    @JsonManagedReference
    private Category parentsCategory;

    @OneToMany(mappedBy = "parentsCategory")
    @JsonBackReference
    private List<Category> linkedCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

}

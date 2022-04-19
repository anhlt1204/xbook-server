package com.ados.xbook.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_user")
public class User extends BaseEntity {

    @Nationalized
    private String firstName;

    @Nationalized
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Nationalized
    private String address;

    @JsonIgnore
    private String password;

    private Double amount;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaleOrder> saleOrders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductRate> productRates;

}

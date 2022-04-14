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
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Nationalized
    private String address;

    @JsonIgnore
    private String password;

    private Long amount;

    private String email;

    private String phone;

    private String role;

//    @ManyToMany
//    @JoinTable(name = "tbl_users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaleOrder> saleOrders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductRate> productRates;

}

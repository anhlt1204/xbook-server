package com.ados.xbook.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tbl_delivery")
public class Delivery extends BaseEntity {

    @Column(name = "index_delivery")
    private String index;

    @Nationalized
    private String value;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaleOrder> saleOrders;

    public Delivery(String value) {
        this.index = value;
        this.value = value;
    }
}

package com.ados.xbook.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_order_item")
public class OrderItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    @JsonBackReference
    private SaleOrder saleOrder;

    private Integer quantity;

}

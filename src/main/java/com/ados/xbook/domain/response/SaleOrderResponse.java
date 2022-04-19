package com.ados.xbook.domain.response;

import com.ados.xbook.domain.entity.SaleOrder;
import lombok.Data;

import java.util.Date;

@Data
public class SaleOrderResponse {
    private Long id;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String customerAddress;
    private String phone;
    private Long userId;
    private Long deliveryId;

    public SaleOrderResponse(SaleOrder saleOrder) {
        this.id = saleOrder.getId();
        this.status = saleOrder.getStatus();
        this.createAt = saleOrder.getCreateAt();
        this.createBy = saleOrder.getCreateBy();
        this.updateAt = saleOrder.getUpdateAt();
        this.updateBy = saleOrder.getUpdateBy();
        this.customerAddress = saleOrder.getCustomerAddress();
        this.phone = saleOrder.getPhone();

        if (saleOrder.getUser() != null) {
            this.userId = saleOrder.getUser().getId();
        }

        if (saleOrder.getDelivery() != null) {
            this.deliveryId = saleOrder.getDelivery().getId();
        }
    }
}

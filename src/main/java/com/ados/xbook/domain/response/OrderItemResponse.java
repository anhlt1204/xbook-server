package com.ados.xbook.domain.response;

import java.util.Date;

public class OrderItemResponse {
    private Long id;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private Long productId;
    private Long saleOrderId;
    private Integer quantity;
}

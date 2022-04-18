package com.ados.xbook.domain.request;

import com.ados.xbook.exception.InvalidException;
import lombok.Data;

@Data
public class AddToCardRequest {
    private Long productId;
    private Integer quantity;

    public void validate() {
        if (productId == null || productId <= 0) {
            throw new InvalidException("Invalid product id");
        }

        if (quantity == null || quantity <= 0) {
            throw new InvalidException("Invalid quantity");
        }
    }
}

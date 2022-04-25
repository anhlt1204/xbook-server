package com.ados.xbook.domain.response;

import com.ados.xbook.domain.entity.ProductImage;
import lombok.Data;

@Data
public class ProductImageResponse {
    private Long id;
    private String link;

    public ProductImageResponse(ProductImage image) {
        this.id = image.getId();
        this.link = image.getImageUrl();
    }
}

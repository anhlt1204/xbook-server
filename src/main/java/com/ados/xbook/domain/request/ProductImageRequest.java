package com.ados.xbook.domain.request;

import com.ados.xbook.exception.InvalidException;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductImageRequest {
    private MultipartFile image;
    private String description;
    private Long productId;

    private String username;

    public void validate() {
        if (image.isEmpty()) {
            throw new InvalidException("Image is empty");
        }

        if (productId == null || productId <= 0) {
            throw new InvalidException("Invalid product id");
        }
    }
}

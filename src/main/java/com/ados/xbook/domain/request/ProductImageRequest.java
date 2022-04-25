package com.ados.xbook.domain.request;

import com.ados.xbook.exception.InvalidException;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductImageRequest {
    private MultipartFile[] images;
    private Long productId;

    public void validate() {
        if (images.length <= 0) {
            throw new InvalidException("Images is empty");
        }

        if (productId == null || productId <= 0) {
            throw new InvalidException("Invalid product id");
        }
    }
}

package com.ados.xbook.service;

import com.ados.xbook.domain.request.ProductImageRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    BaseResponse findAll(Long productId);

    BaseResponse create(Long productId, MultipartFile[] images, String username);

    BaseResponse update(Long id, Long productId, MultipartFile image, String username);

    BaseResponse deleteById(String username, Long id);
}

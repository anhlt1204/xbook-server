package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.Product;
import com.ados.xbook.domain.entity.ProductImage;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.ProductImageRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.repository.ProductImageRepo;
import com.ados.xbook.repository.ProductRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.ProductImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductImageServiceImpl extends BaseService implements ProductImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ProductImageRepo productImageRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public BaseResponse findAll(Long productId) {
        GetArrayResponse<ProductImage> response = new GetArrayResponse<>();

        List<ProductImage> productImages = new ArrayList<>();

        Product product = productRepo.findById(productId).orElse(null);

        productImages = productImageRepo.findAllByProduct(product);
        response.setTotalItem(productImages.size());
        response.setData(productImages);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse findById(Long id) {
        GetSingleResponse<ProductImage> response = new GetSingleResponse<>();

        Optional<ProductImage> optional = productImageRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find image has id " + id);
        }

        ProductImage productImage = optional.get();

        response.setItem(productImage);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse upload(MultipartFile file) {
        GetSingleResponse<String> response = new GetSingleResponse<>();
        String link = null;
        Map<?, ?> cloudinaryMap = null;
        try {
            cloudinaryMap = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            link = cloudinaryMap.get("secure_url").toString();
        } catch (IOException e) {
            log.error("Ex: {}", e);
        }

        response.setItem(link);
        response.setSuccess();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GetArrayResponse<ProductImage> create(ProductImageRequest request, SessionEntity info) {
        GetArrayResponse<ProductImage> response = new GetArrayResponse<>();

        try {

            List<ProductImage> list = new ArrayList<>();

            Product product = productRepo.findById(request.getProductId()).orElse(null);

            for (MultipartFile image : request.getImages()) {
                ProductImage productImage = new ProductImage();
                String link = "";
                Map<?, ?> cloudinaryMap = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                link = cloudinaryMap.get("secure_url").toString();
                productImage.setImageUrl(link);
                productImage.setCreateBy(info.getUsername());
                productImage.setProduct(product);
                list.add(productImage);
            }

            productImageRepo.saveAll(list);
            response.setData(list);
            response.setTotalItem(list.size());
            response.setSuccess();
        } catch (Exception e) {
            log.error("Ex: {}", e);
            throw new InvalidException(e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse update(Long id, Long productId, MultipartFile image, String username) {

        GetSingleResponse<ProductImage> response = new GetSingleResponse<>();

        try {

            Optional<ProductImage> optional = productImageRepo.findById(id);

            if (!optional.isPresent()) {
                throw new InvalidException("Cannot find image has id " + id);
            } else {
                ProductImage productImage = optional.get();

                String link = "";
                Map<?, ?> cloudinaryMap = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                link = cloudinaryMap.get("secure_url").toString();

                productImage.setImageUrl(link);
                productImage.setUpdateBy(username);

                if (productId != productImage.getProduct().getId()) {
                    Product product = productRepo.findById(productId).orElse(null);
                    if (product != null) {
                        productImage.setProduct(product);
                    }
                }

                productImageRepo.save(productImage);

                response.setItem(productImage);
                response.setSuccess();
            }

        } catch (Exception e) {
            log.error("Ex: {}", e);
            throw new InvalidException(e.getMessage());
        }

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteById(String username, Long id) {

        BaseResponse response = new BaseResponse();

        Optional<ProductImage> optional = productImageRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find image has id " + id);
        } else {
            productImageRepo.deleteById(id);
            response.setSuccess();
        }

        return response;

    }
}

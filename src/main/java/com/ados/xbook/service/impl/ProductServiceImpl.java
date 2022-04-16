package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.Category;
import com.ados.xbook.domain.entity.Product;
import com.ados.xbook.domain.request.ProductRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.CreateResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.repository.CategoryRepo;
import com.ados.xbook.repository.ProductRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public BaseResponse findAll() {
        GetArrayResponse<Product> response = new GetArrayResponse<>();

        List<Product> products = new ArrayList<>();

        products = productRepo.findAll();
        response.setTotal(products.size());
        response.setRows(products);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse create(ProductRequest request) {

        CreateResponse<Product> response = new CreateResponse<>();

        Product product = request.create();
        product.setCreateBy(request.getUsername());

        Optional<Category> optional = categoryRepo.findById(request.getCategoryId());
        Category category;

        if (optional.isPresent()) {
            category = optional.get();
            product.setCategory(category);
        }

        productRepo.save(product);
        response.setItem(product);
        response.setSuccess();

        return response;

    }

    @Override
    public BaseResponse update(Long id, ProductRequest request) {

        GetSingleResponse<Product> response = new GetSingleResponse<>();

        Optional<Product> optional = productRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find product has id " + id);
        } else {
            Product product = optional.get();
            product = request.update(product);
            product.setUpdateBy(request.getUsername());

            Optional<Category> categoryOptional = categoryRepo.findById(request.getCategoryId());
            Category category;

            if (categoryOptional.isPresent()) {
                category = categoryOptional.get();
                product.setCategory(category);
            }

            productRepo.save(product);
            response.setItem(product);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse deleteById(String username, Long id) {

        BaseResponse response = new BaseResponse();

        Optional<Product> optional = productRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find product has id " + id);
        } else {
            productRepo.deleteById(id);
            response.setSuccess();
        }

        return response;

    }
}

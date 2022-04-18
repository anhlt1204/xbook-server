package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.Category;
import com.ados.xbook.domain.entity.Product;
import com.ados.xbook.domain.request.ProductRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.CreateResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.PagingInfo;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.CategoryRepo;
import com.ados.xbook.repository.ProductRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.ProductService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public BaseResponse findAll(Long categoryId, String key, String value, Integer page, Integer size) {
        GetArrayResponse<Product> response = new GetArrayResponse<>();
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        Long total = 0L;
        Pageable paging;
        Page<Product> p;
        List<Product> products = new ArrayList<>();

        if (Strings.isNullOrEmpty(key) && Strings.isNullOrEmpty(value) && (categoryId == null || categoryId <= 0)) {
            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize());
            p = productRepo.findAll(paging);
            products = p.getContent();
            total = p.getTotalElements();
        }

        if (categoryId == null || categoryId <= 0) {
            if (!Strings.isNullOrEmpty(key)) {
                switch (key.trim().toUpperCase()) {
                    case "TITLE":
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = productRepo.findAllByTitleLike("%" + value + "%", paging);
                        products = p.getContent();
                        total = p.getTotalElements();
                        break;
                    case "FILTER":
                        switch (value.trim().toUpperCase()) {
                            case "ZA":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("title").descending());
                                p = productRepo.findAll(paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                            case "AZ":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("title"));
                                p = productRepo.findAll(paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                            case "OLD":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt"));
                                p = productRepo.findAll(paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                            default:
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                                p = productRepo.findAll(paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                        }
                        break;
                    default:
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = productRepo.findAll(paging);
                        products = p.getContent();
                        total = p.getTotalElements();
                        break;
                }
            }
        } else {
            Optional<Category> optional = categoryRepo.findById(categoryId);
            if (!optional.isPresent()) {
                throw new InvalidException("Cannot find category has id " + categoryId);
            }

            Category category = optional.get();

            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
            p = productRepo.findAllByCategory(category, paging);
            products = p.getContent();
            total = p.getTotalElements();

            if (!Strings.isNullOrEmpty(key)) {
                switch (key.trim().toUpperCase()) {
                    case "TITLE":
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = productRepo.findAllByCategoryAndTitleLike(category, "%" + value + "%", paging);
                        products = p.getContent();
                        total = p.getTotalElements();
                        break;
                    case "FILTER":
                        switch (value.trim().toUpperCase()) {
                            case "ZA":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("title").descending());
                                p = productRepo.findAllByCategory(category, paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                            case "AZ":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("title"));
                                p = productRepo.findAllByCategory(category, paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                            case "OLD":
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt"));
                                p = productRepo.findAllByCategory(category, paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                            default:
                                paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                                p = productRepo.findAllByCategory(category, paging);
                                products = p.getContent();
                                total = p.getTotalElements();
                                break;
                        }
                        break;
                    default:
                        paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                        p = productRepo.findAllByCategory(category, paging);
                        products = p.getContent();
                        total = p.getTotalElements();
                        break;
                }
            }
        }

        response.setTotalItem(total);
        response.setCurrentPage(pagingInfo.getPage() + 1);
        response.setTotalPage(total % pagingInfo.getSize() == 0 ?
                total / pagingInfo.getSize() : total / pagingInfo.getSize() + 1);
        response.setData(products);
        response.setSuccess();

        return response;
    }

    @Override
    public BaseResponse findById(Long id) {

        GetSingleResponse<Product> response = new GetSingleResponse<>();

        Optional<Product> optional = productRepo.findById(id);
        Product product;

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find product has id " + id);
        } else {
            product = optional.get();
            response.setItem(product);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse findBySlug(String slug) {

        GetSingleResponse<Product> response = new GetSingleResponse<>();

        Product product = productRepo.findFirstBySlug(slug);

        if (product == null) {
            throw new InvalidException("Cannot find product has slug " + slug);
        } else {
            response.setItem(product);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse create(ProductRequest request) {

        CreateResponse<Product> response = new CreateResponse<>();

        List<Product> products = productRepo.findAll();
        List<String> titles = products.stream().map(Product::getTitle).collect(Collectors.toList());

        if (titles.contains(request.getTitle())) {
            throw new InvalidException("Title is already exist");
        }

        Product product = request.create();
        product.setSlug(StringHelper.toSlug(request.getTitle()));
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

            List<Product> products = productRepo.findAll();
            List<String> titles = products.stream().map(Product::getTitle).collect(Collectors.toList());

            titles.remove(product.getTitle());

            if (titles.contains(request.getTitle())) {
                throw new InvalidException("Title is already exist");
            }

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

package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.Category;
import com.ados.xbook.domain.request.CategoryRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.CreateResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.repository.CategoryRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public BaseResponse findAll() {

        GetArrayResponse<Category> response = new GetArrayResponse<>();

        List<Category> categories = new ArrayList<>();

        categories = categoryRepo.findAll();
        response.setTotal(categories.size());
        response.setRows(categories);
        response.setSuccess();

        return response;

    }

    @Override
    public BaseResponse findById(Long id) {

        GetSingleResponse<Category> response = new GetSingleResponse<>();

        Optional<Category> optional = categoryRepo.findById(id);
        Category category;

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find category has id " + id);
        } else {
            category = optional.get();
            response.setItem(category);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse findBySlug(String slug) {

        GetSingleResponse<Category> response = new GetSingleResponse<>();

        Category category = categoryRepo.findFirstBySlug(slug);

        if (category == null) {
            throw new InvalidException("Cannot find category has slug " + slug);
        } else {
            response.setItem(category);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse create(CategoryRequest request) {

        CreateResponse<Category> response = new CreateResponse<>();

        Category category = request.create();
        category.setCreateBy(request.getUsername());
        Optional<Category> parents = categoryRepo.findById(request.getParentsId());

        if (parents.isPresent()) {
            List<Category> list = new ArrayList<>();
            list.add(category);
            category.setParentsCategory(parents.get());
            parents.get().setLinkedCategories(list);

            categoryRepo.save(parents.get());
        }

        categoryRepo.save(category);
        response.setItem(category);
        response.setSuccess();

        return response;

    }

    @Override
    public BaseResponse update(Long id, CategoryRequest request) {

        GetSingleResponse<Category> response = new GetSingleResponse<>();

        Optional<Category> optional = categoryRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find category has id " + id);
        } else {
            Category category = optional.get();
            category = request.update(category);
            category.setUpdateBy(request.getUsername());
            Optional<Category> parents = categoryRepo.findById(request.getParentsId());

            if (parents.isPresent()) {
                List<Category> list = new ArrayList<>();
                list.add(category);
                category.setParentsCategory(parents.get());
                parents.get().setLinkedCategories(list);

                categoryRepo.save(parents.get());
            }

            categoryRepo.save(category);

            response.setItem(category);
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse deleteById(String username, Long id) {

        BaseResponse response = new BaseResponse();

        Optional<Category> optional = categoryRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find category has id " + id);
        } else {
            categoryRepo.deleteById(id);
            response.setSuccess();
        }

        return response;

    }

}

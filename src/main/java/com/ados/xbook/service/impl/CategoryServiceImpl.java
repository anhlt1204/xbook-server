package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.Category;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.CategoryRequest;
import com.ados.xbook.domain.response.CategoryResponse;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.domain.response.base.CreateResponse;
import com.ados.xbook.domain.response.base.GetArrayResponse;
import com.ados.xbook.domain.response.base.GetSingleResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.PagingInfo;
import com.ados.xbook.repository.CategoryRepo;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.CategoryService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public BaseResponse findAll(String key, String value, Integer page, Integer size) {

        GetArrayResponse<CategoryResponse> response = new GetArrayResponse<>();
        PagingInfo pagingInfo = PagingInfo.parse(page, size);
        Long total = 0L;
        Pageable paging;
        Page<Category> p;
        List<Category> categories = new ArrayList<>();

        if (Strings.isNullOrEmpty(key) && Strings.isNullOrEmpty(value)) {
            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
            p = categoryRepo.findAll(paging);
            categories = p.getContent();
            total = p.getTotalElements();
        }

        if (!Strings.isNullOrEmpty(key)) {
            switch (key.trim().toUpperCase()) {
                case "NAME":
                    paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                    p = categoryRepo.findAllByNameLike("%" + value + "%", paging);
                    categories = p.getContent();
                    total = p.getTotalElements();
                    break;
                case "FILTER":
                    switch (value.trim().toUpperCase()) {
                        case "ZA":
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("name").descending());
                            p = categoryRepo.findAll(paging);
                            categories = p.getContent();
                            total = p.getTotalElements();
                            break;
                        case "AZ":
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("name"));
                            p = categoryRepo.findAll(paging);
                            categories = p.getContent();
                            total = p.getTotalElements();
                            break;
                        case "OLD":
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt"));
                            p = categoryRepo.findAll(paging);
                            categories = p.getContent();
                            total = p.getTotalElements();
                            break;
                        default:
                            paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                            p = categoryRepo.findAll(paging);
                            categories = p.getContent();
                            total = p.getTotalElements();
                            break;
                    }
                    break;
                default:
                    paging = PageRequest.of(pagingInfo.getPage(), pagingInfo.getSize(), Sort.by("createAt").descending());
                    p = categoryRepo.findAll(paging);
                    categories = p.getContent();
                    total = p.getTotalElements();
                    break;
            }
        }

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category c : categories) {
            categoryResponses.add(new CategoryResponse(c));
        }

        response.setTotalItem(total);
        response.setCurrentPage(pagingInfo.getPage() + 1);
        response.setTotalPage(total % pagingInfo.getSize() == 0 ?
                total / pagingInfo.getSize() : total / pagingInfo.getSize() + 1);
        response.setData(categoryResponses);
        response.setSuccess();

        return response;

    }

    @Override
    public BaseResponse findById(Long id) {

        GetSingleResponse<CategoryResponse> response = new GetSingleResponse<>();

        Optional<Category> optional = categoryRepo.findById(id);
        Category category;

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find category has id " + id);
        } else {
            category = optional.get();
            response.setItem(new CategoryResponse(category));
            response.setSuccess();
        }

        return response;

    }

    @Override
    public BaseResponse findBySlug(String slug) {

        GetSingleResponse<CategoryResponse> response = new GetSingleResponse<>();

        Category category = categoryRepo.findFirstBySlug(slug);

        if (category == null) {
            throw new InvalidException("Cannot find category has slug " + slug);
        } else {
            response.setItem(new CategoryResponse(category));
            response.setSuccess();
        }

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse create(CategoryRequest request, SessionEntity info) {

        CreateResponse<CategoryResponse> response = new CreateResponse<>();

        List<Category> categories = categoryRepo.findAll();
        List<String> names = categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());

        if (names.contains(request.getName())) {
            throw new InvalidException("Name is already exist");
        }

        Category category = request.create();
        category.setCreateBy(info.getUsername());
        Optional<Category> parents = categoryRepo.findById(request.getParentsId());

        if (parents.isPresent()) {
            List<Category> list = new ArrayList<>();
            list.add(category);
            category.setParentsCategory(parents.get());
            parents.get().setLinkedCategories(list);

            categoryRepo.save(parents.get());
        }

        categoryRepo.save(category);
        response.setItem(new CategoryResponse(category));
        response.setSuccess();

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse update(Long id, CategoryRequest request, SessionEntity info) {

        GetSingleResponse<CategoryResponse> response = new GetSingleResponse<>();

        Optional<Category> optional = categoryRepo.findById(id);

        if (!optional.isPresent()) {
            throw new InvalidException("Cannot find category has id " + id);
        } else {
            Category category = optional.get();

            List<Category> categories = categoryRepo.findAll();
            List<String> names = categories.stream()
                    .map(Category::getName)
                    .collect(Collectors.toList());

            names.remove(category.getName());

            if (names.contains(request.getName())) {
                throw new InvalidException("Name is already exist");
            }

            category = request.update(category);
            category.setUpdateBy(info.getUsername());
            Optional<Category> parents = categoryRepo.findById(request.getParentsId());

            if (parents.isPresent()) {
                List<Category> list = new ArrayList<>();
                list.add(category);
                category.setParentsCategory(parents.get());
                parents.get().setLinkedCategories(list);

                categoryRepo.save(parents.get());
            }

            categoryRepo.save(category);

            response.setItem(new CategoryResponse(category));
            response.setSuccess();
        }

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

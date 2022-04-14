package com.ados.xbook.controller.user;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/category")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public BaseResponse findAll() {

        BaseResponse response;

        log.info("=>findAll");

        response = categoryService.findAll();

        log.info("<=findAll");

        return response;

    }

    @GetMapping("/{id}")
    public BaseResponse findById(@PathVariable(name = "id") Long id) {

        BaseResponse response;

        log.info("=>findById");

        response = categoryService.findById(id);

        log.info("<=findById");

        return response;

    }

    @GetMapping("/slug/{slug}")
    public BaseResponse findBySlug(@PathVariable(name = "slug") String slug) {

        BaseResponse response;

        log.info("=>findBySlug");

        response = categoryService.findBySlug(slug);

        log.info("<=findBySlug");

        return response;

    }

}

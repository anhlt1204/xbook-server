package com.ados.xbook.controller.publicCtrl;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/product")
public class PublicProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public BaseResponse findAll(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                @RequestParam(value = "key", required = false) String key,
                                @RequestParam(value = "value", required = false) String value,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size) {

        BaseResponse response;

        log.info("=>findAll categoryId: {}, key: {}, value: {}, page: {}, size: {}",
                categoryId, key, value, page, size);

        response = productService.findAll(categoryId, key, value, page, size);

        log.info("<=findAll categoryId: {}, key: {}, value: {}, page: {}, size: {}, res: {}",
                categoryId, key, value, page, size, response);

        return response;

    }

    @GetMapping("/{id}")
    public BaseResponse findById(@PathVariable(name = "id") Long id) {

        BaseResponse response;

        log.info("=>findById");

        response = productService.findById(id);

        log.info("<=findById");

        return response;

    }

    @GetMapping("/slug/{slug}")
    public BaseResponse findBySlug(@PathVariable(name = "slug") String slug) {

        BaseResponse response;

        log.info("=>findBySlug");

        response = productService.findBySlug(slug);

        log.info("<=findBySlug");

        return response;

    }

}

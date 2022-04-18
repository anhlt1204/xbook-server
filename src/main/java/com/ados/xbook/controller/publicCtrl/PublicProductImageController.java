package com.ados.xbook.controller.publicCtrl;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/productImage")
public class PublicProductImageController extends BaseController {

    @Autowired
    private ProductImageService productImageService;

    @GetMapping
    public BaseResponse findAll(@RequestParam(value = "productId") Long productId) {

        BaseResponse response;

        log.info("=>findAll productId: {}", productId);

        response = productImageService.findAll(productId);

        log.info("<=findAll findAll productId: {}, res: {}", productId, response);

        return response;

    }

    @GetMapping("/{id}")
    public BaseResponse findById(@PathVariable(name = "id") Long id) {

        BaseResponse response;

        log.info("=>findById id: {}", id);

        response = productImageService.findById(id);

        log.info("<=findById id: {}, res: {}", id, response);

        return response;

    }
}

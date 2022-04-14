package com.ados.xbook.controller.admin;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/admin/productImage")
public class ProductImageAdminController extends BaseController {

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private SessionEntityRepo repo;

    @PostMapping
    public BaseResponse create(@RequestHeader Map<String, String> headers,
                               @RequestParam("productId") Long productId,
                               @RequestParam("images") MultipartFile[] images) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>create info: {}, productId: {}, images: {}", info, productId, images);

        if (productId == null || productId <= 0 || images.length == 0) {
            throw new InvalidException("Params invalid");
        } else {
            response = productImageService.create(productId, images, info.getUsername());
        }

        log.info("<=create info: {}, productId: {}, images: {}, res: {}", info, productId, images, response);

        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse update(@RequestHeader Map<String, String> headers,
                               @PathVariable(name = "id") Long id,
                               @RequestParam("productId") Long productId,
                               @RequestParam("image") MultipartFile image) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>update info: {}, id:{}, productId: {}, image: {}", info, id, productId, image);

        if (id == null || id <= 0 || productId == null || productId <= 0 || image.isEmpty()) {
            throw new InvalidException("Params invalid");
        } else {
            response = productImageService.update(id, productId, image, info.getUsername());
        }

        log.info("<=update info: {}, id:{}, productId: {}, image: {}, res: {}", info, id, productId, image, response);

        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@RequestHeader Map<String, String> headers,
                               @PathVariable(name = "id") Long id) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>delete info: {}, id: {}", info, id);

        if (id == null || id <= 0) {
            throw new InvalidException("Params invalid");
        } else {
            response = productImageService.deleteById(info.getUsername(), id);
        }

        log.info("<=delete info: {}, id: {}, res: {}", info, id, response);

        return response;
    }


}

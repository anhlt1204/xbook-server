package com.ados.xbook.controller.admin;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.request.CategoryRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.helper.StringHelper;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SessionEntityRepo repo;

    @PostMapping
    public BaseResponse create(@RequestHeader Map<String, String> headers,
                               @RequestBody CategoryRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>create info: {}, req: {}", info, request);

        if (request == null) {
            throw new InvalidException("Params invalid");
        } else {
//            request.validate();
            response = categoryService.create(request, info);
        }

        log.info("<=create info: {}, req: {}, res: {}", info, request, response);

        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse update(@RequestHeader Map<String, String> headers,
                               @PathVariable(name = "id") Long id,
                               @RequestBody CategoryRequest request) {
        BaseResponse response;
        String token = headers.get("authorization");
        SessionEntity info = StringHelper.info(token, repo);

        log.info("=>update info: {}, id:{}, req: {}", info, id, request);

        if (id == null || id <= 0 || request == null) {
            throw new InvalidException("Params invalid");
        } else {
//            request.validate();
            response = categoryService.update(id, request, info);
        }

        log.info("<=update info: {}, id:{}, req: {}, res: {}", info, id, request, response);

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
            response = categoryService.deleteById(info.getUsername(), id);
        }

        log.info("<=delete info: {}, id: {}, res: {}", info, id, response);

        return response;
    }

}

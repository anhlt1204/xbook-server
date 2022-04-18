package com.ados.xbook.controller.publicCtrl;

import com.ados.xbook.controller.BaseController;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/user")
public class PublicUserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public BaseResponse findById(@PathVariable("id") Long id) {
        BaseResponse response;

        log.info("=>findById id: {}", id);

        response = userService.findById(id);

        log.info("<=findById id: {}, res: {}", id, response);

        return response;
    }

    @GetMapping("/name/{username}")
    public BaseResponse findById(@PathVariable("username") String username) {
        BaseResponse response;

        log.info("=>findById username: {}", username);

        response = userService.findByUsername(username);

        log.info("<=findById username: {}, res: {}", username, response);

        return response;
    }

}

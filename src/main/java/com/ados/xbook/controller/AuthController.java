package com.ados.xbook.controller;

import com.ados.xbook.domain.request.LoginRequest;
import com.ados.xbook.domain.request.RegisterRequest;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

}

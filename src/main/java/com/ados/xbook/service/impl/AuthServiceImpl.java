package com.ados.xbook.service.impl;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.domain.entity.User;
import com.ados.xbook.domain.request.LoginRequest;
import com.ados.xbook.domain.request.RegisterRequest;
import com.ados.xbook.domain.response.LoginResponse;
import com.ados.xbook.domain.response.RegisterResponse;
import com.ados.xbook.domain.response.base.BaseResponse;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.repository.SessionEntityRepo;
import com.ados.xbook.repository.UserRepo;
import com.ados.xbook.service.AuthService;
import com.ados.xbook.service.BaseService;
import com.ados.xbook.service.CustomUserDetailsService;
import com.ados.xbook.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl extends BaseService implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SessionEntityRepo sessionEntityRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse login(LoginRequest request) {

        LoginResponse response = new LoginResponse();

        try {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));
            } catch (DisabledException e) {
                log.error("Ex: {}", e);
                throw new Exception("USER_DISABLED", e);
            } catch (BadCredentialsException e) {
                log.error("Ex: {}", e);
                throw new Exception("INVALID_CREDENTIALS", e);
            }

            UserDetails userdetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userdetails);
            User user = userRepo.findFirstByUsername(request.getUsername());
            String username = user.getUsername();

            response.setUsername(username);
            response.setRole(user.getRole());
            response.setToken(token);
            response.setSuccess();

            SessionEntity session = new SessionEntity(token, user.getFirstName(), user.getLastName(),
                    user.getUsername(), user.getAddress(), user.getRole(), user.getAmount(),
                    user.getEmail(), user.getPhone());

            sessionEntityRepo.save(session);

        } catch (Exception e) {
            log.error("Ex: {}", e);
            throw new InvalidException(e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse register(RegisterRequest request) {

        RegisterResponse response = new RegisterResponse();

        List<User> users = userRepo.findAll();
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        List<String> emails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        List<String> phones = users.stream()
                .map(User::getPhone)
                .collect(Collectors.toList());

        if (usernames.contains(request.getUsername())) {
            throw new InvalidException("Username is already exist");
        }

        if (emails.contains(request.getEmail())) {
            throw new InvalidException("Email is already exist");
        }

        if (phones.contains(request.getPhone())) {
            throw new InvalidException("Phone number is already exist");
        }

        try {
            userDetailsService.save(request);
            response.setUsername(request.getUsername());
            response.setSuccess();
        } catch (Exception e) {
            log.error("Ex: {}", e);
            throw new InvalidException(e.getMessage());
        }

        return response;
    }
}

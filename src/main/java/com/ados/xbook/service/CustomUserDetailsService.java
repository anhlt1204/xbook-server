package com.ados.xbook.service;

import com.ados.xbook.domain.entity.ERole;
import com.ados.xbook.domain.request.RegisterRequest;
import com.ados.xbook.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        com.ados.xbook.domain.entity.User user = userRepo.findFirstByUsername(username);
        if (user != null) {
            roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
            return new User(user.getUsername(), user.getPassword(), roles);
        }

        throw new UsernameNotFoundException("User not found with the username " + username);
    }

    public com.ados.xbook.domain.entity.User save(RegisterRequest request) {
        com.ados.xbook.domain.entity.User user = new com.ados.xbook.domain.entity.User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(bcryptEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(ERole.USER.toString());
        user.setAmount(0D);
        user.setCreateBy(request.getUsername());

        return userRepo.save(user);
    }

    public com.ados.xbook.domain.entity.User createAdmin(com.ados.xbook.domain.entity.User user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRole(ERole.ADMIN.toString());
        user.setCreateBy(user.getUsername());

        return userRepo.save(user);
    }
}

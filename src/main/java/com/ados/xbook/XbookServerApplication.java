package com.ados.xbook;

import com.ados.xbook.domain.entity.Delivery;
import com.ados.xbook.domain.entity.EDelivery;
import com.ados.xbook.domain.entity.User;
import com.ados.xbook.repository.DeliveryRepo;
import com.ados.xbook.repository.UserRepo;
import com.ados.xbook.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class XbookServerApplication implements CommandLineRunner {

    @Autowired
    private CustomUserDetailsService service;

    @Autowired
    private UserRepo repo;

    @Autowired
    private DeliveryRepo deliveryRepo;

    public static void main(String[] args) {
        SpringApplication.run(XbookServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (repo.findFirstByUsername("admin") == null) {
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setPhone("0987654321");
            user.setUsername("admin");
            user.setPassword("Admin123!");
            service.createAdmin(user);
        }

        if (deliveryRepo.count() == 0) {
            Delivery d0 = new Delivery(EDelivery.MUA_HANG.toString());
            Delivery d1 = new Delivery(EDelivery.CHO_XAC_NHAN.toString());
            Delivery d2 = new Delivery(EDelivery.DANG_GIAO_HANG.toString());
            Delivery d3 = new Delivery(EDelivery.GIAO_HANG_THANH_CONG.toString());

            List<Delivery> list = Arrays.asList(d0, d1, d2, d3);

            deliveryRepo.saveAll(list);
        }
    }

}

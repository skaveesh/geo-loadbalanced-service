package com.skaveesh.geo.geoloadbalanced.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GeoLoadBalancedServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GeoLoadBalancedServiceApplication.class, args);
    }

}

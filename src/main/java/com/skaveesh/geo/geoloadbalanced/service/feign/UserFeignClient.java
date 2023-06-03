package com.skaveesh.geo.geoloadbalanced.service.feign;

import com.skaveesh.geo.geoloadbalanced.service.entity.User;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("userClientAdapter")
public interface UserFeignClient {

    @GetMapping(value = "/user/getUserInThisRegion/{id}")
    @Headers("Content-Type: application/json")
    User getUserInThisRegion(@PathVariable("id") String id);

    @GetMapping(value = "/user/getAllUsersInThisRegion")
    @Headers("Content-Type: application/json")
    List<User> getAllUsersInThisRegion();

}

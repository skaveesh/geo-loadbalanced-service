package com.skaveesh.geo.geoloadbalanced.service.service;

import com.skaveesh.geo.geoloadbalanced.service.config.RegionConfiguration;
import com.skaveesh.geo.geoloadbalanced.service.entity.User;
import com.skaveesh.geo.geoloadbalanced.service.feign.UserFeignClient;
import com.skaveesh.geo.geoloadbalanced.service.repository.UserRepository;
import com.amazonaws.regions.Regions;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Value("${com.skaveesh.geo.geoloadbalanced.service.region.this}")
    private String thisRegion;

    final UserRepository userRepository;

    final RegionConfiguration regionConfiguration;

    public UserService(UserRepository userRepository, RegionConfiguration regionConfiguration) {
        this.userRepository = userRepository;
        this.regionConfiguration = regionConfiguration;
    }

    public User getUserInThisRegion(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsersInTheRegion() {
        return userRepository.findAll();
    }

    public User getUserFromAnyRegion(String id) {

        User finalUserObj = getUserInThisRegion(id);

        if (finalUserObj != null) {
            return finalUserObj;
        }

        for (String otherRegionUrl : regionConfiguration.getOtherRegionsUrls()) {
            UserFeignClient userFeignClient = Feign.builder()
                    .client(new OkHttpClient())
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .contract(new SpringMvcContract())
                    .target(UserFeignClient.class, otherRegionUrl);

            try {
                finalUserObj = userFeignClient.getUserInThisRegion(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (finalUserObj != null) {
                break;
            }
        }

        return finalUserObj;
    }

    public List<User> getAllUsersFromEveryRegion() {

        List<User> finalUserList = getAllUsersInTheRegion();

        for (String otherRegionUrl : regionConfiguration.getOtherRegionsUrls()) {
            UserFeignClient userFeignClient = Feign.builder()
                    .client(new OkHttpClient())
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .contract(new SpringMvcContract())
                    .target(UserFeignClient.class, otherRegionUrl);

            try {
                finalUserList.addAll(userFeignClient.getAllUsersInThisRegion());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return finalUserList;
    }

    public void createUser(String id, String name) {
        User newUser = new User(id, name, Regions.fromName(thisRegion).getDescription());
        userRepository.save(newUser);
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}

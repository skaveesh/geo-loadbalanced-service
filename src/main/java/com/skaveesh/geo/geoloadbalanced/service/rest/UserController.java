package com.skaveesh.geo.geoloadbalanced.service.rest;

import com.skaveesh.geo.geoloadbalanced.service.entity.User;
import com.skaveesh.geo.geoloadbalanced.service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getUserInThisRegion/{id}")
    public ResponseEntity<User> getUserInThisRegion(@PathVariable("id") String id) {
        User user = userService.getUserInThisRegion(id);

        if(user == null) return new ResponseEntity<> (HttpStatus.NOT_FOUND);

        return new ResponseEntity<> (user, HttpStatus.OK);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<User> getUserFromAnyRegion(@PathVariable("id") String id) {
        User user = userService.getUserFromAnyRegion(id);

        if(user == null) return new ResponseEntity<> (HttpStatus.NOT_FOUND);

        return new ResponseEntity<> (user, HttpStatus.OK);
    }

    @GetMapping("getAllUsersInThisRegion")
    public ResponseEntity<List<User>> getAllUsersInThisRegion() {
        List<User> users = userService.getAllUsersInTheRegion();

        if(users == null || users.isEmpty()) return new ResponseEntity<> (HttpStatus.NOT_FOUND);

        return new ResponseEntity<> (users, HttpStatus.OK);
    }

    @GetMapping("get")
    public ResponseEntity<List<User>> getAllUsersFromEveryRegion() {
        List<User> users = userService.getAllUsersFromEveryRegion();

        if(users == null || users.isEmpty()) return new ResponseEntity<> (HttpStatus.NOT_FOUND);

        return new ResponseEntity<> (users, HttpStatus.OK);
    }

    @PostMapping("createUser/{id}/{name}")
    public ResponseEntity<Void> createUser(@PathVariable("id") String id, @PathVariable("name") String name) {
        userService.createUser(id, name);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @DeleteMapping("deleteUser/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

}

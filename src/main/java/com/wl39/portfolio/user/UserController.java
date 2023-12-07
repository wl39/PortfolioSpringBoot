package com.wl39.portfolio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path ="/signup")
    @PostMapping
    public void signUpUser(@RequestBody UserForm userForm) {
        if (userForm.getPassword().length() < 8) {
            throw new IllegalStateException("Password is too short. Password needs at least 8 characters!");
        }

        this.userService.signUpUser(userForm.getUsername(), userForm.getPassword());
    }

    @RequestMapping(path ="/login")
    @PostMapping
    public void loginUser(@RequestBody UserForm userForm) {
        if (userForm.getPassword().length() < 8) {
            throw new IllegalStateException("Password is too short. Password needs at least 8 characters!");
        }

        this.userService.loginUser(userForm.getUsername(), userForm.getPassword());

        System.out.println("login success");
    }
}

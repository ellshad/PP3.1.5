package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.ExceptionInfo;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestUsersController {
    private final UserServiceImpl userService;


    public RestUsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> listUser() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> infoUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping(value = "users")
    public ResponseEntity<ExceptionInfo> newUser(@RequestBody User user) {
        if (userService.isExistEmail(user)){
                return new ResponseEntity<>(new ExceptionInfo("Email already exists. \n Use another."), HttpStatus.BAD_REQUEST);
        } else
            userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping("/users/{id}")
    public ResponseEntity<ExceptionInfo> update(@RequestBody User user) {
        if (userService.isExistEmail(user)){
            return new ResponseEntity<>(new ExceptionInfo("Email already exists. \n Use another."), HttpStatus.BAD_REQUEST);
        } else
            userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> showUser(Authentication auth) {
        return new ResponseEntity<>(userService.findByUsername(auth.getName()), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.listRoles(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    ResponseEntity<Role> getRoleById(@PathVariable("id") long id){
        return new ResponseEntity<>(userService.findRoleById(id), HttpStatus.OK);
    }
}
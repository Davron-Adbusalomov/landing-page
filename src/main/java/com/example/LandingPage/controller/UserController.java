package com.example.LandingPage.controller;

import com.example.LandingPage.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() throws Exception {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(usersService.getAllUsers());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/getUserDetails/{userId}")
    public ResponseEntity<?> getAll(@PathVariable Long userId) throws Exception {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(usersService.getUserDetails(userId));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}

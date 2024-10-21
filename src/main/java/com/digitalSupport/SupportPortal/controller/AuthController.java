package com.digitalSupport.SupportPortal.controller;


import com.digitalSupport.SupportPortal.model.RegisterRequestBody;
import com.digitalSupport.SupportPortal.model.User;
import com.digitalSupport.SupportPortal.service.JwtService;
import com.digitalSupport.SupportPortal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin()
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    // add roles
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestBody registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        List<String> roles = registerRequest.getRoles();

        User user = userService.registerUser(username, password, roles);
        System.out.println(user);
        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        User user = userService.authenticateUser(username, password);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        return ResponseEntity.ok(response);
    }
}

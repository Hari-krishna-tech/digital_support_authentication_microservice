package com.digitalSupport.SupportPortal.service;





import com.digitalSupport.SupportPortal.model.Role;
import com.digitalSupport.SupportPortal.model.User;
import com.digitalSupport.SupportPortal.repository.RoleRepository;
import com.digitalSupport.SupportPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    Logger logger = Logger.getLogger( UserService.class.getName());
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, List<String> roles) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        try {
            Set<Role> rolesSet = roles.stream().map(name -> roleRepository.findByName(name))
                    .collect(Collectors.toSet());
            User user = new User(username, passwordEncoder.encode(password), rolesSet);
            logger.info(user.toString());
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Roles Doesn't Exists" + e.getMessage());
        }


    }

    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}

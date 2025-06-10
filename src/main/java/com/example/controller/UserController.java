package com.example.controller;

import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.model.dtos.AddressDTO;
import com.example.model.dtos.MonthlyUserCountDTO;
import com.example.model.dtos.UserDTO;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.service.UserChartsService;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final UserChartsService userChartsService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper,
                          PasswordEncoder passwordEncoder,
                          CartRepository cartRepository,
                          UserRepository userRepository,
                          UserChartsService userChartsService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userChartsService = userChartsService;
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UserDTO userDto) {
        User userEntity = UserMapper.toEntity(userDto);
        User createdUser = userService.create(userEntity);
        UserDTO createdUserDTO = UserMapper.toDto(createdUser);
        return ResponseEntity.ok(createdUserDTO);
    }

    @PostMapping("/addresses/{userId}")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO, @PathVariable int userId) {
        if (userId == 0) {
            throw new RuntimeException("Invalid user ID");
        }

        try {
            AddressDTO savedAddress = userService.addAddressToUser(addressDTO, userId);
            return ResponseEntity.ok(savedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDto) {
        User userToLogin = UserMapper.toEntity(userDto);
        User user = userService.login(userToLogin.getEmail(), userToLogin.getPassword());
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.sendResetCode(email);
            return ResponseEntity.ok("Reset code sent to your email.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");

        try {
            userService.resetPasswordWithCode(email, code, newPassword);
            return ResponseEntity.ok("Password has been successfully reset.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String code = requestBody.get("code");
        try {
            userService.verify(email, code);
            return ResponseEntity.ok("Cont verificat cu succes!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verificarea a esuat: " + e.getMessage());
        }
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserData(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> ResponseEntity.ok(UserMapper.toDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users-by-month")
    public ResponseEntity<List<Map<String, Object>>> getUsersByMonth(@RequestParam int year) {
        return ResponseEntity.ok(userChartsService.getUserRegistrationsByMonth(year));
    }

    @GetMapping("/age-distribution")
    public Map<String, Long> getAgeDistribution() {
        return userChartsService.getAgeDistribution();
    }

    @GetMapping("/verification-status")
    public ResponseEntity<Map<String, Long>> getVerificationStatus() {
        return ResponseEntity.ok(userChartsService.getVerifiedStatusCount());
    }

    @GetMapping("/users-by-county")
    public Map<String, Long> getUsersByCounty() {
        return userChartsService.getUsersByCounty();
    }


    @PutMapping("/user")
    public ResponseEntity<User> updateUserData(@RequestBody User userData) {

        Optional<User> existingUserOptional = userRepository.findById(userData.getId());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();


            if (!userData.getPassword().equals(existingUser.getPassword())) {
                userData.setPassword(passwordEncoder.encode(userData.getPassword()));
            }

            User updatedUser = userRepository.save(userData);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(u -> ResponseEntity.ok(UserMapper.toDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        try {
            User userDetails = UserMapper.toEntity(userDTO);
            User updatedUser = userService.updateUser(userDetails, id);
            UserDTO updatedUserDTO = UserMapper.toDto(updatedUser);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable int userId, @RequestBody Map<String, String> passwords) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return ResponseEntity.ok("Parola a fost schimbata cu succes!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

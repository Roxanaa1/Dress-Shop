package com.example.controller;

import com.example.MessageResponse;
import com.example.mapper.UserMapper;
import com.example.model.Cart;
import com.example.model.User;
import com.example.model.dtos.AddressDTO;
import com.example.model.dtos.UserDTO;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController
{
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper,PasswordEncoder passwordEncoder,CartRepository cartRepository,UserRepository userRepository)
    {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder=passwordEncoder;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO newUser)
    {
        boolean userExists = userService.existsByEmail(newUser.getEmail()).orElse(false);

        if (userExists)
        {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        User user = userMapper.userDTOToUser(newUser);
        User savedUser = userService.createUser(user);
        UserDTO savedUserDTO = userMapper.userToUserDTO(savedUser);

        return ResponseEntity.ok(savedUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO loginUser)
    {
        Optional<User> userOptional = userService.findUserByEmail(loginUser.getEmail());

        if (userOptional.isPresent())
        {
            User user = userOptional.get();

            if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword()))
            {
                if (user.getCart() == null)
                {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    Cart savedCart = cartRepository.save(newCart);
                    user.setCart(savedCart);
                    userService.updateUser(user, user.getId());
                }

                UserDTO userDTO = userMapper.userToUserDTO(user);

                Map<String, Object> response = new HashMap<>();
                response.put("userId", user.getId());
                response.put("cartId", userDTO.getCartId());
                response.put("message", "Login successful");

                return ResponseEntity.ok(response);
            } else
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid password!"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserData(@PathVariable int id)
    {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/user")
    public ResponseEntity<User> updateUserData(@RequestBody User userData) {
        User updatedUser = userRepository.save(userData);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/addresses/{userId}")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO, @PathVariable int userId)
    {
        if (userId == 0)
        {
            throw new RuntimeException("Invalid user ID");
        }

        try {
            AddressDTO savedAddress = userService.addAddressToUser(addressDTO, userId);
            return ResponseEntity.ok(savedAddress);
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO)
    {

        User user = userMapper.userDTOToUser(userDTO);
        User savedUser = userService.createUser(user);
        UserDTO savedUserDTO = userMapper.userToUserDTO(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id)
    {
        Optional<User> user = userService.findUserById(id);
        return user.map(u -> ResponseEntity.ok(userMapper.userToUserDTO(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO)
    {
        try {
            User userDetails = userMapper.userDTOToUser(userDTO);
            User updatedUser = userService.updateUser(userDetails, id);
            UserDTO updatedUserDTO = userMapper.userToUserDTO(updatedUser);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id)
    {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

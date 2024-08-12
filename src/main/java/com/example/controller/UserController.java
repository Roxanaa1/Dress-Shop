package com.example.controller;

import com.example.MessageResponse;
import com.example.mapper.CartMapper;
import com.example.mapper.UserMapper;
import com.example.model.Cart;
import com.example.model.User;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.UserDTO;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController
{
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper,PasswordEncoder passwordEncoder,CartService cartService,CartRepository cartRepository,CartMapper cartMapper)
    {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder=passwordEncoder;
        this.cartService=cartService;
        this.cartRepository=cartRepository;
        this.cartMapper=cartMapper;
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

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword()))
            {

                if (user.getCart() == null) {

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    Cart savedCart = cartRepository.save(newCart);

                    user.setCart(savedCart);
                    userService.updateUser(user, user.getId());
                }

                UserDTO userDTO = userMapper.userToUserDTO(user);

                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid password!"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
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

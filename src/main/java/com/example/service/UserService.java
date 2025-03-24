package com.example.service;

import com.example.mapper.AddressMapper;
import com.example.mapper.UserMapper;
import com.example.model.*;
import com.example.model.dtos.AddressDTO;
import com.example.model.dtos.UserDTO;
import com.example.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final ProductImageRepository productImageRepository;
    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,AddressMapper addressMapper,AddressRepository addressRepository,EmailService emailService,RoleRepository roleRepository,ProductRepository productRepository,ProductImageRepository productImageRepository,CartRepository cartRepository)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.addressMapper=addressMapper;
        this.addressRepository=addressRepository;
        this.emailService=emailService;
        this.roleRepository=roleRepository;
        this.productRepository=productRepository;
        this.productImageRepository=productImageRepository;
        this.cartRepository=cartRepository;
    }

    public User create(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        RoleType selectedRole = (user.getRole() != null && user.getRole().getRoleType() != null)
                ? user.getRole().getRoleType()
                : RoleType.USER;

        Role role = roleRepository.findByRoleType(selectedRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setPassword(encodedPassword);
        user.setRole(role);

        String verificationCode = String.valueOf(new Random().nextInt(100000, 999999));
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(10));
        user.setVerifiedAccount(false);

        User savedUser = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cart.setTotalPrice(0);
        cartRepository.save(cart);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
        return savedUser;
    }





    public User verify(String email, String verificationCode) {
        System.out.println("Email primit pentru verificare: " + email);
        User user =userRepository.findByEmail(email.trim())


                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getVerificationCodeExpiration() == null || LocalDateTime.now().isAfter(user.getVerificationCodeExpiration())) {
            throw new RuntimeException("Verification code has expired.");
        }

        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new RuntimeException("Invalid verification code.");
        }

        user.setVerifiedAccount(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiration(null);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getVerifiedAccount()) {
            throw new RuntimeException("Contul nu este verificat!");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Parolă incorectă!");
        }

        return user;
    }


    public Optional<User>  findUserById(int id)
    {
        return userRepository.findById(id);
    }

    public User updateUser(User userDetails,int id)
    {
        return userRepository.findById(id).map(user->
        {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setPassword(userDetails.getPassword());
            user.setDefaultDeliveryAddress(userDetails.getDefaultDeliveryAddress());
            user.setDefaultBillingAddress(userDetails.getDefaultBillingAddress());

            return userRepository.save(user);
        }).orElseThrow(()-> new EntityNotFoundException("User not found with id:"+id));
    }
    public void deleteUser(int id)
    {
        if(userRepository.existsById(id))
        {
            userRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("User not found with id :"+id);
        }
    }
    public Optional<Boolean> existsByEmail(String email)
    {
        return userRepository.existsByEmail(email);
    }
    public boolean checkPassword(String rawPassword, String encodedPassword)
    {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    @Transactional
    public AddressDTO addAddressToUser(AddressDTO addressDTO, int userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        Address address = addressMapper.addressDTOToAddress(addressDTO);
        address.setUser(user);
        address = addressRepository.save(address);

        if (user.getDefaultBillingAddress() == 0 ) {
            user.setDefaultBillingAddress(address.getId());
        } else {
            user.setDefaultBillingAddress(address.getId());
        }

        if ( user.getDefaultDeliveryAddress() == 0) {
            user.setDefaultDeliveryAddress(address.getId());
        } else {
            user.setDefaultDeliveryAddress(address.getId());
        }

        userRepository.save(user);

        return addressMapper.addressToAddressDTO(address);
    }

    public User getUserById(int id)
    {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

}

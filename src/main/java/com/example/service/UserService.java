package com.example.service;

import com.example.mapper.AddressMapper;
import com.example.model.Address;
import com.example.model.User;
import com.example.model.dtos.AddressDTO;
import com.example.repository.AddressRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,AddressMapper addressMapper,AddressRepository addressRepository)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.addressMapper=addressMapper;
        this.addressRepository=addressRepository;
    }

    public User createUser(User user)
    {
        Optional<Boolean> userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists.orElse(false)) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
        return addressMapper.addressToAddressDTO(address);
    }




}

package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    public User createUser(User user)
    {
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
            user.setOrders(userDetails.getOrders());
            user.setCarts(userDetails.getCarts());

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
}

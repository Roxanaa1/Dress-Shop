package com.example.mapper;

import com.example.model.*;
import com.example.model.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


import com.example.model.User;
import com.example.model.dtos.UserDTO;
import com.example.model.Cart;
import com.example.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper
{

    public static UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setPassword(user.getPassword());
        userDTO.setDefaultDeliveryAddress(user.getDefaultDeliveryAddress());
        userDTO.setDefaultBillingAddress(user.getDefaultBillingAddress());

        // Adăugăm verificare pentru orders ca să evităm NullPointerException
        userDTO.setOrderIds(user.getOrders() != null ?
                user.getOrders().stream()
                        .map(Order::getId)
                        .collect(Collectors.toList())
                : new ArrayList<>());

        userDTO.setCartId(user.getCart() != null ? user.getCart().getId() : null);
        userDTO.setVerifiedAccount(user.getVerifiedAccount());
        userDTO.setVerificationCode(user.getVerificationCode());
        userDTO.setVerificationCodeExpiration(user.getVerificationCodeExpiration());
        userDTO.setRole(user.getRole().getRoleType().name());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setBirthDate(user.getBirthDate());
        return userDTO;
    }


    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        user.setDefaultDeliveryAddress(userDTO.getDefaultDeliveryAddress());
        user.setDefaultBillingAddress(userDTO.getDefaultBillingAddress());
        user.setVerifiedAccount(userDTO.getVerifiedAccount());
        user.setVerificationCode(userDTO.getVerificationCode());
        user.setVerificationCodeExpiration(userDTO.getVerificationCodeExpiration());

        if (userDTO.getRole() != null) {
            Role role = new Role();
            role.setRoleType(RoleType.valueOf(userDTO.getRole().toUpperCase()));
            user.setRole(role);
        }
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setBirthDate(userDTO.getBirthDate());
        return user;
    }



}

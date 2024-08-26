package com.example.controller;

import com.example.model.dtos.AddressDTO;
import com.example.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/addresses")
@CrossOrigin(origins = "http://localhost:3000")
public class AddressController
{

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService)
    {
        this.addressService = addressService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AddressDTO> getAddress(@PathVariable int userId)
    {
        Optional<AddressDTO> addressOptional = addressService.getAddressByUserId(userId);
        return addressOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable int userId, @RequestBody AddressDTO addressDTO)
    {
        try {
            AddressDTO updatedAddress = addressService.updateUserAddress(userId, addressDTO);
            return ResponseEntity.ok(updatedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

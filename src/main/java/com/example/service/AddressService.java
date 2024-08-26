package com.example.service;

import com.example.mapper.AddressMapper;
import com.example.model.Address;
import com.example.model.User;
import com.example.model.dtos.AddressDTO;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressService(UserRepository userRepository, AddressMapper addressMapper)
    {
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    public Optional<AddressDTO> getAddressByUserId(int userId)
    {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.map(user -> {
            // sorteaza lista de adrese descrescator  dupa id
            List<Address> sortedAddresses = user.getAddresses().stream()
                    .sorted((a1, a2) -> Integer.compare(a2.getId(), a1.getId()))
                    .toList();

            // ret prima adresa(ultima )
            Address lastAddress = sortedAddresses.isEmpty() ? null : sortedAddresses.get(0);

            return addressMapper.addressToAddressDTO(lastAddress);
        });
    }


    public AddressDTO addAddressToUser(int userId, AddressDTO addressDTO)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Address address = addressMapper.addressDTOToAddress(addressDTO);
        address.setUser(user);
        user.getAddresses().add(address);
        userRepository.save(user);

        return addressMapper.addressToAddressDTO(address);
    }

    public AddressDTO updateUserAddress(int userId, AddressDTO addressDTO)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        Address address = addressMapper.addressDTOToAddress(addressDTO);
        address.setUser(user);


        List<Address> addresses = user.getAddresses();
        if (addresses.isEmpty())
        {
            addresses.add(address);
        } else
        {
             addresses.set(0, address);
        }
        userRepository.save(user);

        return addressMapper.addressToAddressDTO(address);
    }

}

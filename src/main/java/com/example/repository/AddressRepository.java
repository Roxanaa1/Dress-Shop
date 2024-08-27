package com.example.repository;

import com.example.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer>
{
    Optional<Address> findByStreetLineAndCityAndPostalCodeAndCountry(
            String streetLine, String city, String postalCode, String country);
}

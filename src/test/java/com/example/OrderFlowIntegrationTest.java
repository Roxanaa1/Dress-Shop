package com.example;

import com.example.model.*;
import com.example.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class OrderFlowIntegrationTest {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartEntryRepository cartEntryRepository;
    @Autowired private OrderRepository orderRepository;

    @Test
    public void givenUserAndProduct_whenPlacingOrder_thenOrderIsSavedCorrectly() {
        final String email = "ana@test.com";

        Role role = roleRepository.findByRoleType(RoleType.USER)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleType(RoleType.USER);
                    return roleRepository.save(r);
                });

        User user = new User();
        user.setEmail(email);
        user.setPassword("123456");
        user.setFirstName("Ana");
        user.setLastName("Popescu");
        user.setPhoneNumber("0712345678");
        user.setVerifiedAccount(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);
        user = userRepository.save(user);

        Address address = new Address();
        address.setStreetLine("Str. Florilor 10");
        address.setPostalCode("010101");
        address.setCity("Bucuresti");
        address.setCounty("Bucuresti");
        address.setCountry("Romania");
        address.setUser(user);
        address = addressRepository.save(address);
        user.setDefaultDeliveryAddress(address.getId());
        user = userRepository.save(user);

        Category category = new Category();
        category.setName("TestCategory");
        category.setDescription("Descriere test");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Rochie eleganta");
        product.setDescription("Rochie de seara");
        product.setPrice(100f);
        product.setBuyingPrice(60f);
        product.setAvailableQuantity(10);
        product.setAddedDate(LocalDate.now());
        product.setCategory(category);
        product = productRepository.save(product);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setPaymentMethod(PaymentMethod.CASH);
        cart.setTotalPrice(100.0f);
        cart = cartRepository.save(cart);

        CartEntry entry = new CartEntry();
        entry.setCart(cart);
        entry.setProduct(product);
        entry.setQuantity(1);
        entry.setPricePerPiece(product.getPrice());
        entry.setTotalPricePerEntry(product.getPrice());
        entry = cartEntryRepository.save(entry);

        Order order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(100f);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setDeliveryAddress(address.getId());
        order.setInvoiceAddress(address.getId());
        order = orderRepository.save(order);

        Optional<Order> saved = orderRepository.findById(order.getId());
        assertThat(saved).isPresent();
        assertThat(saved.get().getUser().getEmail()).isEqualTo(email);
        assertThat(saved.get().getCart().getId()).isEqualTo(cart.getId());
        assertThat(saved.get().getTotalPrice()).isEqualTo(100f);
        assertThat(saved.get().getOrderStatus()).isEqualTo(OrderStatus.PENDING);
    }
}

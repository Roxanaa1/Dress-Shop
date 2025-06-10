package com.example.repository;

import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CartRepository cartRepository;

    private User user;
    private Order order;

    @BeforeEach
    public void setUp() {
        Role role = roleRepository.findByRoleType(RoleType.USER)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleType(RoleType.USER);
                    return roleRepository.save(r);
                });

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("Roxana");
        user.setLastName("Enache");
        user.setPhoneNumber("0712345678");
        user.setCreatedAt(LocalDateTime.now());
        user.setVerifiedAccount(true);
        user.setRole(role);
        user = userRepository.save(user);

        Address address = new Address();
        address.setStreetLine("Str. Exemplu 123");
        address.setPostalCode("010203");
        address.setCity("Bucuresti");
        address.setCounty("Bucuresti");
        address.setCountry("Romania");
        address.setUser(user);
        address = addressRepository.save(address);

        user.setDefaultDeliveryAddress(address.getId());
        user = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cart = cartRepository.save(cart);

        order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalPrice(100.0f);
        order = orderRepository.save(order);

    }

    @Test
    public void givenInvalidCart_whenFindByCartIdAndUserIdAndOrderDateIsNull_thenReturnEmpty() {
        Optional<Order> result = orderRepository.findByCartIdAndUserIdAndOrderDateIsNull(999, 999);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenOrderStatusAndDate_whenFindByOrderStatusAndOrderDateBefore_thenReturnList() {
        List<Order> results = orderRepository.findByOrderStatusAndOrderDateBefore(OrderStatus.PENDING, LocalDate.now().plusDays(1));
        assertThat(results).isNotEmpty();
    }

    @Test
    public void givenValidYear_whenCountOrdersByMonthFilteredByYear_thenReturnData() {
        List<Object[]> results = orderRepository.countOrdersByMonthFilteredByYear(LocalDate.now().getYear());
        assertThat(results).isNotEmpty();
    }

    @Test
    public void givenValidYear_whenCountOrdersByCountyFilteredByYear_thenReturnData() {
        List<Object[]> results = orderRepository.countOrdersByCountyFilteredByYear(LocalDate.now().getYear());
        assertThat(results).isNotEmpty();
        assertThat(results.get(0)[0]).isEqualTo("Bucuresti");
    }

    @Test
    public void givenValidYear_whenSumOrderValuesByMonthFilteredByYear_thenReturnSums() {
        List<Object[]> results = orderRepository.sumOrderValuesByMonthFilteredByYear(LocalDate.now().getYear());
        assertThat(results).isNotEmpty();
    }

    @Test
    public void givenValidYear_whenCountByStatusFilteredByYear_thenReturnStatusCounts() {
        List<Object[]> results = orderRepository.countByStatusFilteredByYear(LocalDate.now().getYear());

        assertThat(results).isNotEmpty();
        boolean hasPending = results.stream().anyMatch(r -> r[0] == OrderStatus.PENDING);
        assertThat(hasPending).isTrue();
    }

    @Test
    public void givenValidYear_whenFindTopCustomersFilteredByYear_thenReturnNamesAndCounts() {
        List<Object[]> results = orderRepository.findTopCustomersFilteredByYear(PageRequest.of(0, 5), LocalDate.now().getYear());
        assertThat(results).isNotEmpty();
        assertThat(((String) results.get(0)[0])).contains("Roxana");
    }
}

package com.example.service;

import com.example.mapper.OrderMapper;
import com.example.model.*;
import com.example.model.dtos.OrderDTO;
import com.example.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private CartEntryRepository cartEntryRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_order_should_save_order_and_clear_cart() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1);
        orderDTO.setCartId(10);
        orderDTO.setPaymentMethod("CASH");
        orderDTO.setTotalPrice((float) 100.0);


        User user = new User();
        user.setDefaultBillingAddress(2);
        user.setDefaultDeliveryAddress(3);

        Cart cart = new Cart();
        cart.setCartEntries(new ArrayList<>());

        Order order = new Order();
        order.setOrderItems(new ArrayList<>());

        when(orderRepository.findByCartIdAndUserIdAndOrderDateIsNull(10, 1)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(cartRepository.findById(10)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(orderMapper.orderDTOToOrder(orderDTO)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        verify(orderRepository).save(order);
        verify(emailService).sendOrderConfirmationEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    public void get_all_order_details_should_return_correct_dto_list() {
        Order order = new Order();
        User user = new User();
        user.setFirstName("Ana");
        user.setLastName("Pop");
        user.setEmail("ana@example.com");
        user.setPhoneNumber("1234");

        order.setUser(user);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setTotalPrice((float) 150.0);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());

        when(orderRepository.findAll()).thenReturn(List.of(order));

        var result = orderService.getAllOrderDetails();

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getFirstName());
    }

    @Test
    public void update_product_quantities_should_update_all_quantities() {
        Product product = new Product();
        product.setAvailableQuantity(10);

        CartEntry entry = new CartEntry();
        entry.setQuantity(3);
        entry.setProduct(product);

        Cart cart = new Cart();
        cart.setCartEntries(List.of(entry));

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        orderService.updateProductQuantities(1);

        assertEquals(7, product.getAvailableQuantity());
    }

    @Test
    public void clear_cart_should_empty_entries_and_reset_price() {
        Cart cart = new Cart();
        cart.setCartEntries(List.of(new CartEntry()));
        cart.setTotalPrice(200);

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        orderService.clearCart(1);

        assertEquals(0, cart.getCartEntries().size());
        assertEquals(0, cart.getTotalPrice());
    }

    @Test
    public void get_order_by_id_should_return_order() {
        Order order = new Order();
        order.setId(5);

        when(orderRepository.findById(5)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(5);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getId());
    }
}

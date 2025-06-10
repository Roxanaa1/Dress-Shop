package com.example.service;

import com.example.mapper.OrderMapper;
import com.example.model.*;
import com.example.model.dtos.*;
import com.example.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    private final CartEntryRepository cartEntryRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository, AddressRepository addressRepository, OrderMapper orderMapper, ProductRepository productRepository, EmailService emailService, CartEntryRepository cartEntryRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.emailService = emailService;
        this.cartEntryRepository = cartEntryRepository;

    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {

        Optional<Order> existingOrder = orderRepository.findByCartIdAndUserIdAndOrderDateIsNull(orderDTO.getCartId(), orderDTO.getUserId());
        if (existingOrder.isPresent()) {
            throw new IllegalArgumentException("Comanda deja existenta pentru acest cos.");
        }
        try {
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + orderDTO.getUserId()));

            Cart cart = cartRepository.findById(orderDTO.getCartId())
                    .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + orderDTO.getCartId()));

            Order order = orderMapper.orderDTOToOrder(orderDTO);
            order.setUser(user);
            order.setCart(cart);
            order.setOrderDate(LocalDate.now());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(orderDTO.getTotalPrice());

            if (orderDTO.getPaymentMethod() == null || PaymentMethod.valueOf(orderDTO.getPaymentMethod()) == null) {
                throw new IllegalArgumentException("Invalid or missing payment method.");
            }

            order.setPaymentMethod(PaymentMethod.valueOf(orderDTO.getPaymentMethod()));

            if (orderDTO.getDeliveryAddress() == 0) {
                orderDTO.setDeliveryAddress(user.getDefaultDeliveryAddress());
            }
            if (orderDTO.getInvoiceAddress() == 0) {
                orderDTO.setInvoiceAddress(user.getDefaultBillingAddress());
            }

            order.setDeliveryAddress(orderDTO.getDeliveryAddress());
            order.setInvoiceAddress(orderDTO.getInvoiceAddress());

            List<CartEntry> orderItems = new ArrayList<>();
            for (CartEntry entry : cart.getCartEntries()) {
                CartEntry clone = new CartEntry();
                clone.setProduct(entry.getProduct());
                clone.setQuantity(entry.getQuantity());
                clone.setPricePerPiece(entry.getPricePerPiece());
                clone.setTotalPricePerEntry(entry.getTotalPricePerEntry());
                orderItems.add(clone);
            }

            order.setOrderItems(orderItems);

            Order savedOrder = orderRepository.save(order);

            emailService.sendOrderConfirmationEmail(
                    user.getEmail(),
                    "Confirmare comanda #" + savedOrder.getId(),
                    String.valueOf(savedOrder.getId())
            );

            updateProductQuantities(orderDTO.getCartId());

            createNewCartFromOldCart(orderDTO.getCartId());

            clearCart(orderDTO.getCartId());

            return savedOrder;

        } catch (Exception e) {
            throw e;
        }
    }

    private Cart createNewCartFromOldCart(int oldCartId) {
        Cart oldCart = cartRepository.findById(oldCartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + oldCartId));

        oldCart.setTotalPrice(0);
        oldCart.getCartEntries().clear();

        Cart updatedCart = cartRepository.save(oldCart);
        return updatedCart;
    }

    public List<OrderDetailsDTO> getAllOrderDetails() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            OrderDetailsDTO dto = new OrderDetailsDTO();
            dto.setOrderId(order.getId());
            User user = order.getUser();

            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhoneNumber());

            dto.setPaymentMethod(order.getPaymentMethod().toString());
            dto.setTotalPrice(order.getTotalPrice());
            dto.setOrderDate(order.getOrderDate());
            dto.setOrderStatus(order.getOrderStatus().name());

            List<ProductInfoDTO> productList = order.getOrderItems().stream().map(entry -> {
                Product product = entry.getProduct();
                ProductInfoDTO prodDto = new ProductInfoDTO();
                prodDto.setName(product.getName());
                prodDto.setCategory(product.getCategory().getName());
                prodDto.setDescription(product.getDescription());
                prodDto.setPrice(product.getPrice());
                prodDto.setImageUrl(getProductImageUrl(product));
                return prodDto;
            }).toList();

            dto.setProducts(productList);
            return dto;
        }).toList();
    }

    private String getProductImageUrl(Product product) {
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            String code = product.getProductImages().get(0).getCode();
            return "https://i.postimg.cc/" + code + ".png";
        }
        return "https://via.placeholder.com/100x100?text=No+Image";
    }


    @Transactional
    public void updateProductQuantities(int cartId) {
        cartRepository.findById(cartId).ifPresent(cart -> {
            cart.getCartEntries().forEach(entry -> {
                Product product = entry.getProduct();
                int newQuantity = product.getAvailableQuantity() - entry.getQuantity();
                product.setAvailableQuantity(newQuantity);
                productRepository.save(product);
            });
            logger.info("Product quantities updated for Cart ID: {}", cartId);
        });
    }


    @Transactional
    public void clearCart(int cartId) {
        cartRepository.findById(cartId).ifPresent(cart ->
        {
            logger.info("Clearing cart with ID: {}", cartId);
            cart.setCartEntries(new ArrayList<>());
            cart.setTotalPrice(0);
            Cart updatedCart = cartRepository.save(cart);
            logger.info("Cart cleared: {}", updatedCart.getCartEntries().isEmpty());
        });
    }


    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Order orderDetails, int id) {
        return orderRepository.findById(id).map(order ->
        {
            order.setUser(orderDetails.getUser());
            order.setCart(orderDetails.getCart());
            order.setPaymentMethod(orderDetails.getPaymentMethod());
            order.setDeliveryAddress(orderDetails.getDeliveryAddress());
            order.setInvoiceAddress(orderDetails.getInvoiceAddress());
            order.setTotalPrice(orderDetails.getTotalPrice());
            order.setOrderDate(orderDetails.getOrderDate());

            return orderRepository.save(order);
        }).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    public void deleteOrder(int id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Order not found with id:" + id);
        }
    }
}

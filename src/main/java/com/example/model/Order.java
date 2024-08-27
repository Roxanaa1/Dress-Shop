package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    public void setPaymentMethod(PaymentMethod paymentMethod)
    {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("PaymentType cannot be null");
        }
        this.paymentMethod = paymentMethod;
    }
    private int deliveryAddress;
    private int invoiceAddress;

    @Column(nullable = false)
    private float totalPrice;

    @Column(nullable = false)
    private LocalDate orderDate;


}

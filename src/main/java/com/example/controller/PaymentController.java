package com.example.controller;

import com.example.model.dtos.CartRequest;
import com.example.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckout(@RequestBody CartRequest request) {
        String sessionUrl = stripeService.createCheckoutSession(
                request.getProductNames(), request.getPrices(), request.getQuantities()
        );
        return ResponseEntity.ok(sessionUrl);
    }
}

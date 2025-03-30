package com.example.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public String createCheckoutSession(List<String> productNames, List<Long> prices, List<Long> quantities) {
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> lineItems = new java.util.ArrayList<>();

        for (int i = 0; i < productNames.size(); i++) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(quantities.get(i))
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("ron")
                                            .setUnitAmount(prices.get(i))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(productNames.get(i))
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success")
                .setCancelUrl("http://localhost:3000/cancel")
                .addAllLineItem(lineItems)
                .build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Stripe session failed: " + e.getMessage());
        }
    }
}


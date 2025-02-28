package com.vutran0943.payment_service.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vutran0943.payment_service.services.PaypalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/payments/paypal")
public class PaypalController {
    private final PaypalService paypalService;
    @Value("${paypal.cancel-url}")
    private String cancelUrl;
    @Value("${paypal.success-url}")
    private String successUrl;

    @PostMapping("/create")
    public RedirectView createPayment(
            @RequestParam String amount,
            @RequestParam String currency,
            @RequestParam String paymentMethod,
            @RequestParam String description
    ) throws PayPalRESTException {
    try {
        Payment payment = paypalService.createPayment(
                currency,
                Double.parseDouble(amount),
                description,
                "sale",
                paymentMethod,
                cancelUrl,
                successUrl
        );


        payment.getLinks().forEach(link -> {
            System.out.println(link.getHref());
        });

        for(Links links : payment.getLinks()) {
            if (links.getRel().equals("approval_url")) return new RedirectView(links.getHref());
        }

    } catch (Exception e) {
        log.error(e.getMessage());
    }
        return new RedirectView("/payments/error");
    }
}

package com.vutran0943.payment_service.controllers;

import com.vutran0943.payment_service.dto.request.PaymentCreationRequest;
import com.vutran0943.payment_service.dto.response.ApiResponse;
import com.vutran0943.payment_service.factory.PaymentServiceFactory;
import com.vutran0943.payment_service.services.PaymentService;
import com.vutran0943.payment_service.shared.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    @Value("${url.error-url}")
    private String errorUrl;
    private final PaymentServiceFactory paymentServiceFactory;


    @PostMapping("/pay")
    public ApiResponse<String> pay(@Valid @RequestBody PaymentCreationRequest paymentCreationRequest, HttpServletRequest httpRequest) throws Exception {
        try {
            String provider = paymentCreationRequest.getPaymentMethod().getServiceName();

            PaymentService paymentService = paymentServiceFactory.getPaymentService(provider);

            String paymentUrl = paymentService.processPayment(paymentCreationRequest, httpRequest);

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setData(paymentUrl);
            apiResponse.setMessage("Create payment successful");

            return apiResponse;
        } catch(Exception e) {
            log.error(e.getMessage());
            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setData(errorUrl);
            apiResponse.setMessage("Create payment failed");
            return apiResponse;
        }
    }

    @GetMapping("/{payment-method}/inspect")
    public String successHandler(@PathVariable("payment-method") String paymentMethod, HttpServletRequest http) throws Exception {
        try {
            String provider = PaymentMethod.valueOf(paymentMethod).getServiceName();

            PaymentService paymentService = paymentServiceFactory.getPaymentService(provider);

            return paymentService.inspectPaymentStatus(http);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{payment-method}/cancel")
    public String cancelHandler() {
        System.out.print("ok");
        return "cancel";
    }

    @GetMapping("/error")
    public String errorHandler() {
        return "error";
    }
}

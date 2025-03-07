package com.vutran0943.payment_service.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.vutran0943.payment_service.dto.request.PaymentCreationRequest;
import com.vutran0943.payment_service.mappers.PaymentMapper;
import com.vutran0943.payment_service.repositories.PaymentRepository;
import com.vutran0943.payment_service.shared.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaypalService implements PaymentService {
    @NonFinal
    private final String failedStatus = PaymentStatus.FAILED.toString().toLowerCase();
    @NonFinal
    private final String approvedStatus = PaymentStatus.APPROVED.toString().toLowerCase();
    @Value("${paypal.return-url}")
    private String returnUrl;
    @Value("${paypal.cancel-url}")
    private String cancelUrl;
    @Value("${paypal.intent}")
    private String intent;
    private final APIContext apiContext;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public String processPayment(PaymentCreationRequest paymentCreationRequest, HttpServletRequest request) throws PayPalRESTException {
        double totalAmount = paymentCreationRequest.getAmount();
        
        String currency = paymentCreationRequest.getCurrency();
        String description = paymentCreationRequest.getDescription();
        String paymentMethod = paymentCreationRequest.getPaymentMethod().toString();

        Payment payment = new Payment();
        String id = UUID.randomUUID().toString();
        Amount amount = new Amount();

        amount.setCurrency(currency);
        amount.setTotal(Double.toString(totalAmount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(paymentMethod);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl + "/" + id);
        redirectUrls.setReturnUrl(returnUrl + "/" + id);

        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        for(Links links : createdPayment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                com.vutran0943.payment_service.entities.Payment paymentEntity = paymentMapper.toPayment(paymentCreationRequest);
                paymentEntity.setId(id);
                paymentEntity.setStatus(PaymentStatus.CREATED.toString());

                paymentRepository.save(paymentEntity);

                return links.getHref();
            }
        }

        return failedStatus;
    }

    @Override
    public String inspectPaymentStatus(String id, HttpServletRequest http) throws Exception {
        String paymentId = http.getParameter("paymentId");
        String payerId = http.getParameter("PayerID");

        if(payerId == null || paymentId == null) return PaymentStatus.FAILED.toString();

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, execution);

        com.vutran0943.payment_service.entities.Payment paymentEntity = paymentRepository.findPaymentById(id);

        if (executedPayment.getState().equals(approvedStatus)) paymentEntity.setStatus(approvedStatus);
        else paymentEntity.setStatus(failedStatus);

        paymentRepository.save(paymentEntity);

        return executedPayment.getState();
    }

    @Override
    public String discardPayment(String paymentId, HttpServletRequest http) throws PayPalRESTException {
        com.vutran0943.payment_service.entities.Payment paymentEntity = paymentRepository.findPaymentById(paymentId);

        paymentEntity.setStatus(failedStatus);
        paymentRepository.save(paymentEntity);

        return failedStatus;
    }

}

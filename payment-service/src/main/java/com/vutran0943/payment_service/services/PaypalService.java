package com.vutran0943.payment_service.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.vutran0943.payment_service.dto.request.PaymentCreationRequest;
import com.vutran0943.payment_service.exceptions.AppException;
import com.vutran0943.payment_service.exceptions.ErrorCode;
import com.vutran0943.payment_service.mappers.PaymentMapper;
import com.vutran0943.payment_service.repositories.PaymentRepository;
import com.vutran0943.payment_service.shared.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaypalService implements PaymentService {
    @Value("${paypal.success-url}")
    private String successUrl;
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
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);
        for(Links links : createdPayment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                com.vutran0943.payment_service.entities.Payment paymentEntity = paymentMapper.toPayment(paymentCreationRequest);
                paymentEntity.setPaymentId(createdPayment.getId());
                paymentEntity.setStatus(PaymentStatus.CREATED.toString());

                paymentRepository.save(paymentEntity);

                return links.getHref();
            }
        }

        return null;
    }

    @Override
    public String inspectPaymentStatus(HttpServletRequest http) throws Exception {
        String paymentId = http.getParameter("paymentId");
        String payerId = http.getParameter("PayerID");

        if(payerId == null || paymentId == null) throw new AppException(ErrorCode.INVALID_PAYMENT);

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, execution);

        com.vutran0943.payment_service.entities.Payment paymentEntity = paymentRepository.findPaymentByPaymentId(paymentId);

        if (executedPayment.getState().equals(PaymentStatus.APPROVED.toString().toLowerCase())) paymentEntity.setStatus(PaymentStatus.APPROVED.toString());
        else paymentEntity.setStatus(PaymentStatus.FAILED.toString());

        paymentRepository.save(paymentEntity);

        return executedPayment.getState();
    }

}

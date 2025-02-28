package com.vutran0943.payment_service.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class PaypalService {
    private final APIContext apiContext;

    public Payment createPayment(
            String currency,
            Double totalAmount,
            String description,
            String intent,
            String paymentMethod,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        Amount amount = new Amount();

        amount.setCurrency(currency);
        amount.setTotal(totalAmount.toString());

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

        return payment.create(apiContext);
    }

    public Payment excutePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext, execution);
    }
}

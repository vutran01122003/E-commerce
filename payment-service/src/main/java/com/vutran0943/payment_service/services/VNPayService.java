package com.vutran0943.payment_service.services;

import com.vutran0943.payment_service.dto.request.PaymentCreationRequest;
import com.vutran0943.payment_service.entities.Payment;
import com.vutran0943.payment_service.mappers.PaymentMapper;
import com.vutran0943.payment_service.repositories.PaymentRepository;
import com.vutran0943.payment_service.shared.PaymentMethod;
import com.vutran0943.payment_service.shared.PaymentStatus;
import com.vutran0943.payment_service.utils.VnpayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class VNPayService implements PaymentService {
    @Value("${vnpay.vnp_Version}")
    private String vnp_Version;
    @Value("${vnpay.vnp_Command}")
    private String vnp_Command;
    @Value("${vnpay.orderType}")
    private String orderType;
    @Value("${vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;
    @Value("${vnpay.vnp_HashSecret}")
    private String secretKey;
    @Value("${vnpay.vnp_Url}")
    private String vnp_PayUrl;
    @Value("${vnpay.vnp_returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${vnpay.vnp_CurrCode}")
    private String vnp_CurrCode;
    private final VnpayUtils vnpayUtils;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public String processPayment(PaymentCreationRequest paymentCreationRequest, HttpServletRequest req) throws UnsupportedEncodingException {
        int amount = (int) (paymentCreationRequest.getAmount() * 100);
        String bankCode = paymentCreationRequest.getBankCode();
        String description = paymentCreationRequest.getDescription();
        String vnp_TxnRef = vnpayUtils.getRandomNumber(8);
        String vnp_IpAddr = vnpayUtils.getIpAddress(req);
        String paymentId = UUID.randomUUID().toString();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", description + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl + String.format("/%s", paymentId));
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);

            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = vnpayUtils.hmacSHA512(secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = vnp_PayUrl + "?" + queryUrl;

        Payment payment = paymentMapper.toPayment(paymentCreationRequest);
        payment.setStatus(PaymentStatus.CREATED.toString());
        payment.setId(paymentId);

        paymentRepository.save(payment);

        return paymentUrl;
    }

    @Override
    public String inspectPaymentStatus(String id, HttpServletRequest http) throws Exception {
        String vnp_ResponseCode = http.getParameter("vnp_ResponseCode");

        String status = PaymentStatus.APPROVED.toString();
        if(!vnp_ResponseCode.equals("00")) status = PaymentStatus.FAILED.toString();

        Payment payment = paymentRepository.findPaymentById(id);
        payment.setStatus(status);

        paymentRepository.save(payment);

        return status;
    }

    @Override
    public String discardPayment(String paymentId, HttpServletRequest http) throws Exception {
        Payment payment = paymentRepository.findPaymentById(paymentId);

        String status = PaymentStatus.FAILED.toString();

        payment.setStatus(PaymentStatus.FAILED.toString());
        paymentRepository.save(payment);

        return status;
    }
}

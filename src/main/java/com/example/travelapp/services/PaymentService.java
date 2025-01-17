package com.example.travelapp.services;

import com.example.travelapp.models.Payment;
import com.example.travelapp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${mpesa.consumer-key}")
    private String consumerKey;

    @Value("${mpesa.consumer-secret}")
    private String consumerSecret;

    @Value("${mpesa.passkey}")
    private String passkey;

    @Value("${mpesa.business-shortcode}")
    private String businessShortCode;

    @Value("${mpesa.callback-url}")
    private String callbackUrl;

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    public PaymentService(PaymentRepository paymentRepository, RestTemplate restTemplate) {
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
    }

    // Initialize STK Push
    public ResponseEntity<String> initiatePayment(String phoneNumber, double amount, int bookingId) {
        try {
            // Create new payment record
            Payment payment = new Payment();
            payment.setPaymentType("MPESA");
            payment.setPaymentStatus("PENDING");
            payment.setBookingId(bookingId);
            paymentRepository.save(payment);

            // Generate access token
            String accessToken = generateAccessToken();

            // Prepare STK push request
            Map<String, Object> stkRequest = prepareStkPushRequest(phoneNumber, amount);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // Make request to M-Pesa API
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(stkRequest, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest",
                    request,
                    String.class
            );

            return response;

        } catch (Exception e) {
            return new ResponseEntity<>("Payment initiation failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Process M-Pesa callback
    public void processPaymentCallback(Map<String, Object> callbackData) {
        try {
            // Extract relevant data from callback
            Map<String, Object> body = (Map<String, Object>) callbackData.get("Body");
            Map<String, Object> stkCallback = (Map<String, Object>) body.get("stkCallback");

            String resultCode = stkCallback.get("ResultCode").toString();
            String merchantRequestId = stkCallback.get("MerchantRequestID").toString();

            // Update payment status
            Payment payment = paymentRepository.findByMerchantRequestId(merchantRequestId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            if (resultCode.equals("0")) {
                payment.setPaymentStatus("COMPLETED");
            } else {
                payment.setPaymentStatus("FAILED");
            }

            paymentRepository.save(payment);

        } catch (Exception e) {
            throw new RuntimeException("Error processing payment callback: " + e.getMessage());
        }
    }

    // Generate access token
    private String generateAccessToken() {
        String appKeySecret = consumerKey + ":" + consumerSecret;
        String auth = Base64.getEncoder().encodeToString(appKeySecret.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials",
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    // Prepare STK Push request
    private Map<String, Object> prepareStkPushRequest(String phoneNumber, double amount) {
        Map<String, Object> request = new HashMap<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String password = Base64.getEncoder().encodeToString(
                (businessShortCode + passkey + timestamp).getBytes()
        );

        request.put("BusinessShortCode", businessShortCode);
        request.put("Password", password);
        request.put("Timestamp", timestamp);
        request.put("TransactionType", "CustomerPayBillOnline");
        request.put("Amount", amount);
        request.put("PartyA", phoneNumber);
        request.put("PartyB", businessShortCode);
        request.put("PhoneNumber", phoneNumber);
        request.put("CallBackURL", callbackUrl);
        request.put("AccountReference", "TravelApp");
        request.put("TransactionDesc", "Travel Booking Payment");

        return request;
    }

    // Check payment status
    public String checkPaymentStatus(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return payment.getPaymentStatus();
    }

    // Get payment details
    public Payment getPaymentDetails(int paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
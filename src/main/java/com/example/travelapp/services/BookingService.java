package com.example.travelapp.services;

import com.example.travelapp.dto.*;
import com.example.travelapp.models.Booking;
import com.example.travelapp.models.User;
import com.example.travelapp.repository.BookingRepository;
import com.example.travelapp.BookingException;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    AuthService authService;
    @Autowired
    OtpService otpService;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          PaymentService paymentService,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Booking createBooking(BookingRequest request) {
        try {
            // Create new booking
            Booking booking = new Booking();
            booking.setDestinationId(request.getDestinationId());
            booking.setBookingDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
            booking.setBookingTime(LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
            booking.setBookingStatus("PENDING");
            booking.setBookingAmount(request.getAmount());
            booking.setLocation(request.getLocation());

            // Save initial booking
            Booking savedBooking = bookingRepository.save(booking);

            // Initiate payment
//            paymentService.initiatePayment(savedBooking);

            // Send confirmation notification
            User user;
            notificationService.sendBookingConfirmation(savedBooking);
            if(authService.getLoggedInEmail() == null){
                throw new BookingException("User not found");
            }else{
                String email = authService.getLoggedInEmail();
                user = userRepository.findByEmail(email).get();
            }
            String message = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Booking Confirmation</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; }\n" +
                    "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                    "        .header { background-color: #f4f4f4; padding: 10px; text-align: center; }\n" +
                    "        .content { padding: 20px; background-color: #ffffff; border: 1px solid #ddd; }\n" +
                    "        .footer { font-size: 0.9em; color: #888; text-align: center; padding: 10px; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div class=\"header\">\n" +
                    "            <h1>Booking Confirmation</h1>\n" +
                    "        </div>\n" +
                    "        <div class=\"content\">\n" +
                    "            <p>Hello " +  user.getUsername()+ ",</p>\n" +
                    "            <p>Your booking has been successfully confirmed!</p>\n" +
                    "            <p><strong>Booking Details:</strong></p>\n" +
                    "            <ul>\n" +
                    "                <li><strong>Place:</strong> " + request.getLocation() + "</li>\n" +
                    "                <li><strong>Date:</strong> " + "10/02/2025"+ "</li>\n" +
                    "                <li><strong>Amount Paid:</strong> kes" + request.getAmount() + "</li>\n" +
                    "            </ul>\n" +
                    "            <p>Thank you for choosing us. We look forward to serving you!</p>\n" +
                    "        </div>\n" +
                    "        <div class=\"footer\">\n" +
                    "            <p>If you have any questions, please contact us at support@mulonzikelvin.com</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

            otpService.sendEmail(user.getEmail(),"Booking Confirmation",message);
            return savedBooking;

        } catch (Exception e) {
            throw new BookingException("Failed to create booking: " + e.getMessage());
        }
    }

    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking not found with id: " + bookingId));
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, String status) {
        Booking booking = getBooking(bookingId);
        booking.setBookingStatus(status);

        if (status.equals("CONFIRMED")) {
            notificationService.sendBookingConfirmation(booking);
        } else if (status.equals("CANCELLED")) {
            handleBookingCancellation(booking);
        }

        return bookingRepository.save(booking);
    }

    private void handleBookingCancellation(Booking booking) {
        // Check cancellation policy
        if (isCancellationAllowed(booking)) {
            // Process refund if needed
//            paymentService.processRefund(booking);
            // Send cancellation notification
            notificationService.sendCancellationNotification(booking);
        } else {
            throw new BookingException("Cancellation not allowed for this booking");
        }
    }

    private boolean isCancellationAllowed(Booking booking) {
        // Implement your cancellation policy logic here
        LocalDateTime bookingDate = LocalDateTime.parse(booking.getBookingDate());
        return LocalDateTime.now().isBefore(bookingDate.minusHours(24));
    }

    public List<Booking> searchBookings(BookingSearchCriteria criteria) {
        // Implement search logic based on various criteria
        return bookingRepository.findBySearchCriteria(criteria);
    }

    @Transactional
    public void processPaymentCallback(PaymentCallback paymentCallback) {
        Booking booking = getBooking(paymentCallback.getBookingId());

        if (paymentCallback.isSuccessful()) {
            booking.setBookingStatus("CONFIRMED");
            bookingRepository.save(booking);
            notificationService.sendPaymentConfirmation(booking);
        } else {
            booking.setBookingStatus("PAYMENT_FAILED");
            bookingRepository.save(booking);
            notificationService.sendPaymentFailureNotification(booking);
        }
    }

    public BookingSummary getBookingSummary(Long userId) {
        List<Booking> userBookings = getUserBookings(userId);
        // Create and return booking summary with statistics
        return new BookingSummary(userBookings);
    }

    public boolean validateBookingAvailability(String destinationId, String date) {
        // Check if the destination is available for booking on the given date
        return bookingRepository.checkAvailability(destinationId, date);
    }

    @Transactional
    public Booking modifyBooking(Long bookingId, BookingModificationRequest request) {
        Booking booking = getBooking(bookingId);

        // Check if modification is allowed
        if (!isModificationAllowed(booking)) {
            throw new BookingException("Booking modification not allowed");
        }

        // Update booking details
        if (request.getNewDate() != null) {
            booking.setBookingDate(request.getNewDate());
        }
        if (request.getNewLocation() != null) {
            booking.setLocation(request.getNewLocation());
        }

        // Handle price differences
        if (request.getNewAmount() != null) {
            handlePriceDifference(booking, request.getNewAmount());
        }

        return bookingRepository.save(booking);
    }

    private boolean isModificationAllowed(Booking booking) {
        // Implement modification policy logic
        LocalDateTime bookingDate = LocalDateTime.parse(booking.getBookingDate());
        return LocalDateTime.now().isBefore(bookingDate.minusHours(48));
    }

    private void handlePriceDifference(Booking booking, String newAmount) {
        double oldAmount = Double.parseDouble(booking.getBookingAmount());
        double updatedAmount = Double.parseDouble(newAmount);

        if (updatedAmount > oldAmount) {
            paymentService.chargeAdditional(booking, updatedAmount - oldAmount);
        } else if (updatedAmount < oldAmount) {
            paymentService.processPartialRefund(booking, oldAmount - updatedAmount);
        }

        booking.setBookingAmount(newAmount);
    }
}
//package com.example.travelapp.config;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureException;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    record ErrorResponse(
//            int status,
//            String error,
//            String message,
//            LocalDateTime timestamp
//    ) {}
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
//        return createErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "An unexpected error occurred",
//                ex.getMessage()
//        );
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
//        return createErrorResponse(
//                HttpStatus.FORBIDDEN,
//                "Access Denied",
//                ex.getMessage()
//        );
//    }
//
//    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
//    public ResponseEntity<ErrorResponse> handleJwtExceptions(Exception ex) {
//        return createErrorResponse(
//                HttpStatus.UNAUTHORIZED,
//                "Invalid or expired JWT token",
//                ex.getMessage()
//        );
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
//        return createErrorResponse(
//                HttpStatus.UNAUTHORIZED,
//                "Invalid credentials",
//                ex.getMessage()
//        );
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, Object> response = new HashMap<>();
//        Map<String, String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .collect(HashMap::new,
//                        (map, error) -> map.put(error.getField(), error.getDefaultMessage()),
//                        HashMap::putAll);
//
//        response.put("timestamp", LocalDateTime.now());
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//        response.put("errors", errors);
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    private ResponseEntity<ErrorResponse> createErrorResponse(
//            HttpStatus status,
//            String error,
//            String message) {
//        return new ResponseEntity<>(
//                new ErrorResponse(
//                        status.value(),
//                        error,
//                        message,
//                        LocalDateTime.now()
//                ),
//                status
//        );
//    }
//}

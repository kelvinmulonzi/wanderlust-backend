package com.example.travelapp.controllers;

import com.example.travelapp.security.ApiResponse;
import com.example.travelapp.dto.DestinationRequest;
import com.example.travelapp.models.Destination;
import com.example.travelapp.services.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Destination>> createDestination(@RequestBody DestinationRequest destination) {
        try {
            Destination createdDestination = destinationService.createDestination(destination);
            ApiResponse<Destination> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Destination created successfully",
                    createdDestination
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<Destination> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create destination",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Destination>>> getAllDestinations() {
        try {
            List<Destination> destinations = destinationService.getAllDestinations();
            ApiResponse<List<Destination>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Destinations retrieved successfully",
                    destinations
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Destination>> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to retrieve destinations",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Destination>> getDestinationById(@PathVariable Long id) {
        try {
            Optional<Destination> destinationOptional = destinationService.getDestinationById(id);
            if (destinationOptional.isPresent()) {
                Destination destination = destinationOptional.get();
                ApiResponse<Destination> response = new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Destination retrieved successfully",
                        destination
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Destination> response = new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        "Destination not found",
                        null
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<Destination> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to retrieve destination",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Destination>> updateDestination(
            @PathVariable Long id,
            @RequestBody Destination destination) {
        try {
            Destination updatedDestination = destinationService.updateDestination(id, destination);
            ApiResponse<Destination> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Destination updated successfully",
                    updatedDestination
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Destination> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to update destination",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDestination(@PathVariable Long id) {
        try {
            destinationService.deleteDestination(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Destination deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to delete destination",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
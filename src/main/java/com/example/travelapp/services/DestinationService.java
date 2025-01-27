package com.example.travelapp.services;

import com.example.travelapp.dto.DestinationRequest;
import com.example.travelapp.models.Destination;
import com.example.travelapp.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public Destination createDestination(DestinationRequest destination) {

        Destination newDestination = new Destination();

        newDestination.setName(destination.getName());
        newDestination.setDescription(destination.getDescription());
        newDestination.setLocation(destination.getLocation());
        newDestination.setImageUrl(destination.getImageUrl());
        newDestination.setPrice(destination.getPrice());
        newDestination.setRating(destination.getRating());

        return destinationRepository.save(newDestination);
    }

    public Destination updateDestination(Long id, Destination destinationDetails) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        destination.setName(destinationDetails.getName());
        destination.setDescription(destinationDetails.getDescription());
        destination.setLocation(destinationDetails.getLocation());

        return destinationRepository.save(destination);
    }

    public void deleteDestination(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));
        destinationRepository.delete(destination);
    }
}
package com.raf.rafvodic.services;

import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.repositories.destination.DestinationRepository;

import javax.inject.Inject;
import java.util.List;

public class DestinationService {

    @Inject
    DestinationRepository destinationRepository;

    public DestinationService() {
    }

    public Destination addDestination(Destination destination) {
        return destinationRepository.addDestination(destination);
    }

    public Destination findDestinationById(int id) {
        return destinationRepository.findDestinationById(id);
    }

    public List<Destination> getAllDestinations(int page, int limit) {
        int offset = (page - 1) * limit;
        return destinationRepository.getAllDestinations(offset, limit);
    }

    public Destination updateDestination(Destination destination) {
        return destinationRepository.updateDestination(destination);
    }

    public void deleteDestination(int id) {
        destinationRepository.deleteDestination(id);
    }

}

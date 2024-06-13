package com.raf.rafvodic.repositories.destination;

import com.raf.rafvodic.entities.Destination;

import java.util.List;

public interface DestinationRepository {

    public Destination addDestination(Destination destination);
    public Destination findDestinationById(int id);
    public Destination findDestinationByName(String name);
    public List<Destination> getAllDestinations(int offset, int limit);
    public Destination updateDestination(Destination destination);
    public void deleteDestination(int id);
}

package com.raf.rafvodic.resources;


import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.services.DestinationService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/destinations")
public class DestinationResource {

    @Inject
    private DestinationService destinationService;

    public DestinationResource() {
    }

    // @GET for all destinations
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Destination> allDestinations(@QueryParam("page") @DefaultValue("1") int page,
                                             @QueryParam("limit") @DefaultValue("10") int limit) {
        return this.destinationService.getAllDestinations(page, limit);
    }

    // add destination
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Destination create(@Valid Destination destination) {
        return this.destinationService.addDestination(destination);
    }

    // get destination by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Destination getDestinationById(@PathParam("id")int id) {
        return this.destinationService.findDestinationById(id);
    }


    // update destionation by id
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Destination updateDestination(@PathParam("id")int id, @Valid Destination destination) {
        return destinationService.updateDestination(destination);
    }

    // delete destination by id
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteDestination(@PathParam("id")int id) {
        destinationService.deleteDestination(id);
    }


}

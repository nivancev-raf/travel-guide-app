package com.raf.rafvodic.resources;

import com.raf.rafvodic.entities.User;
import com.raf.rafvodic.enums.UserStatus;
import com.raf.rafvodic.requests.LoginRequest;
import com.raf.rafvodic.requests.UserStatusUpdateRequest;
import com.raf.rafvodic.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginRequest loginRequest)
    {
        Map<String, String> response = new HashMap<>();
        System.out.println(loginRequest.getEmail() + " " + loginRequest.getPassword());
        String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (jwt == null) {
            response.put("message", "These credentials do not match our records");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);

        return Response.ok(response).build();
    }

    // get users by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        User user = this.userService.findUserById(id);
        if (user == null) {
            return Response.status(404, "Not Found").build();
        }
        return Response.ok(user).build();
    }

    // get all users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allUsers(@QueryParam("page") @DefaultValue("1") int page,
                             @QueryParam("limit") @DefaultValue("10") int limit) {
        return Response.ok(this.userService.getAllUsers(page, limit)).build();
    }

    // add user
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(@Valid User user) {
        return Response.ok(this.userService.addUser(user)).build();
    }

    // update user by id
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, @Valid User user) {
        user.setId(id);
        return Response.ok(this.userService.updateUser(user)).build();
    }

    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUserStatus(@PathParam("id") int id, @Valid UserStatusUpdateRequest request) {
        User user = userService.findUserById(id);
        if (user != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus()));
            return userService.updateUser(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }
}



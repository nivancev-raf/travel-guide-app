package com.raf.rafvodic.resources;


import com.raf.rafvodic.entities.Activity;
import com.raf.rafvodic.entities.ArticleActivity;
import com.raf.rafvodic.services.ActivityService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/activities")
public class ActivityResource {

    @Inject
    private ActivityService activityService;


    public ActivityResource() {
    }

    // @GET for all activities
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Activity> allActivities() {
        return this.activityService.getAllActivities();
    }

    // add activity
     @POST
     @Produces(MediaType.APPLICATION_JSON)
     public Activity create(@Valid Activity activity) {
         return this.activityService.addActivity(activity);
     }

     @POST()
     @Path("/addArticleActivity")
     @Produces(MediaType.APPLICATION_JSON)
     public ArticleActivity createArticleActivity(@Valid ArticleActivity articleActivity) {
            return this.activityService.addArticleActivity(articleActivity);
        }

    // get activities by articleID
    @GET
    @Path("/article/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Activity> getActivitiesByArticleId(@PathParam("id")int id) {
        return this.activityService.getActivitiesByArticleId(id);
    }

    // detele all activities by article id in articleactivities table
    @DELETE
    @Path("/article/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteActivitiesByArticleId(@PathParam("id")int id) {
        this.activityService.deleteActivitiesByArticleId(id);
    }

    // get activity by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Activity getActivityById(@PathParam("id")int id) {
        return this.activityService.findActivityById(id);
    }

    // detele activity by id
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteActivity(@PathParam("id")int id) {
        this.activityService.deleteActivity(id);
    }







}

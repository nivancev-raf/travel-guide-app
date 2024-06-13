package com.raf.rafvodic.services;

import com.raf.rafvodic.entities.Activity;
import com.raf.rafvodic.entities.ArticleActivity;
import com.raf.rafvodic.repositories.activities.ActivityRepository;

import javax.inject.Inject;
import java.util.List;

public class ActivityService {

    @Inject
    ActivityRepository activityRepository;

    public ActivityService() {
    }

    public Activity addActivity(Activity activity) {
        return activityRepository.addActivity(activity);
    }
    public ArticleActivity addArticleActivity(ArticleActivity articleActivity) {
        return activityRepository.addArticleActivity(articleActivity);
    }
    public Activity findActivityById(int id) {
        return activityRepository.findActivityById(id);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.getAllActivities();
    }

    public void deleteActivity(int id) {
        activityRepository.deleteActivity(id);
    }

    public List<Activity> getActivitiesByArticleId(int id) {
        return activityRepository.getActivitiesByArticleId(id);
    }

    public void deleteActivitiesByArticleId(int id) {
        activityRepository.deleteActivitiesByArticleId(id);
    }

}

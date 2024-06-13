package com.raf.rafvodic.repositories.activities;

import com.raf.rafvodic.entities.Activity;
import com.raf.rafvodic.entities.ArticleActivity;
import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityRepositoryImpl extends MySqlAbstractRepository implements ActivityRepository {
    @Override
    public Activity addActivity(Activity activity) {
        // ukoliko u tabeli vec postji activity sa istim imenom, ne dodajemo ga u tabelu activites

        // ako vrati null znaci da treba da dodamo novi activity
        // ako nije null vrati taj activity

        Activity act = activityExists(activity.getName());
        if (act != null) {
            return act;
        }


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"}; // id is autoincrement

            preparedStatement = connection.prepareStatement("INSERT INTO activities (name) VALUES(?)", generatedColumns);
            preparedStatement.setString(1, activity.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                activity.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return activity;
    }

    private Activity activityExists(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM activities WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return new Activity(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return null;
    }

    @Override
    public ArticleActivity addArticleActivity(ArticleActivity articleActivity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("INSERT INTO articleactivities (article_id, activity_id) VALUES(?, ?)");
            preparedStatement.setInt(1, articleActivity.getArticleId());
            preparedStatement.setInt(2, articleActivity.getActivityId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return articleActivity;

    }


    @Override
    public Activity findActivityById(int id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Activity activity = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM activities WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                activity = new Activity(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return activity;
    }

    @Override
    public Activity findActivityByName(String name) {
        return null;
    }

    @Override
    public Activity updateActivity(Activity activity) {
        return null;
    }

    @Override
    public List<Activity> getAllActivities() {
        List<Activity> activitiesList = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM activities");

            while (resultSet.next()) {
                int activityId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                activitiesList.add(new Activity(activityId, name));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return activitiesList;
    }

    @Override
    public void deleteActivity(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM activities WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public Activity findActivityByDestinationId(Long id) {
        return null;
    }

    @Override
    public Activity findActivityByDestinationName(String name) {
        return null;
    }

    @Override
    public List<Activity> getActivitiesByArticleId(int id) {
        List<Activity> activitiesList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            // Join the articleactivities and activities table to get the activities related to the article
            String query = "SELECT a.id, a.name " +
                    "FROM articleactivities aa " +
                    "JOIN activities a ON aa.activity_id = a.id " +
                    "WHERE aa.article_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int activityId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                activitiesList.add(new Activity(activityId, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return activitiesList;
    }

    @Override
    public void deleteActivitiesByArticleId(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM articleactivities WHERE article_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

    }
}

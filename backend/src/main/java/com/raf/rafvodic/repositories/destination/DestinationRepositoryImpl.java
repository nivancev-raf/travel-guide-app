package com.raf.rafvodic.repositories.destination;

import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.entities.User;
import com.raf.rafvodic.enums.UserStatus;
import com.raf.rafvodic.enums.UserType;
import com.raf.rafvodic.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinationRepositoryImpl extends MySqlAbstractRepository implements DestinationRepository {

    public DestinationRepositoryImpl() {
        initializeDestinations();
    }

    private void initializeDestinations() {
        // if there are no destinations in the database, add some
        if (this.getAllDestinations(0,10).isEmpty()) {
            this.addDestination(new Destination("Belgrade", "Capital of Serbia"));
            this.addDestination(new Destination("Novi Sad", "Capital of Vojvodina"));
            this.addDestination(new Destination("Nis", "Capital of South Serbia"));
        }
    }

    @Override
    public Destination addDestination(Destination destination) {
        // ukoliko ime destinacije vec postoji u bazi, ne dodajemo je
        if (this.findDestinationByName(destination.getName()) != null) {
            System.out.println("Destination with name " + destination.getName() + " already exists in the database");
            return null;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"}; // id is autoincrementq

            preparedStatement = connection.prepareStatement("INSERT INTO destinations (name, description) VALUES(?, ?)", generatedColumns);
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                destination.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public Destination findDestinationById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Destination destination = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM destinations WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                destination = new Destination(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public Destination findDestinationByName(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Destination destination = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM destinations WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                destination = new Destination(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public List<Destination> getAllDestinations(int offset, int limit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Destination> destinations = new ArrayList<>();
        try {
            connection = this.newConnection();
            String query = "SELECT * FROM destinations LIMIT ? OFFSET ?"; // if offset is 0, we get first limit destinations, if offset is 1, we get second limit destinations, etc.
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Destination destination = new Destination();
                destination.setId(resultSet.getInt("id"));
                destination.setName(resultSet.getString("name"));
                destination.setDescription(resultSet.getString("description"));
                destinations.add(destination);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return destinations;
    }


    @Override
    public Destination updateDestination(Destination destination) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE destinations SET name = ?, description = ? WHERE id = ?");
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.setInt(3, destination.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public void deleteDestination(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM destinations WHERE id = ?");
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

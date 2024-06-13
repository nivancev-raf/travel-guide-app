package com.raf.rafvodic.repositories.user;

import com.raf.rafvodic.entities.User;
import com.raf.rafvodic.enums.UserStatus;
import com.raf.rafvodic.enums.UserType;
import com.raf.rafvodic.repositories.MySqlAbstractRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl extends MySqlAbstractRepository implements UserRepository {


    public UserRepositoryImpl() {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        if (findUserByEmail("admin@gmail.com") == null) {
            User admin = new User("admin@gmail.com", "admin", "admin",
                    "123", // hashed password for "123"
                    // a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3 -> 123
                    UserType.ADMIN, UserStatus.ACTIVE);
            addUser(admin);
        }
    }
    @Override
    public User addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"}; // id is autoincrementq

            preparedStatement = connection.prepareStatement("INSERT INTO users (email, first_name, last_name, user_type, status, password_hash) VALUES(?, ?, ?, ?, ?, ?)", generatedColumns);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getUserType().toString());
            preparedStatement.setString(5, user.getStatus().toString());
            String hashedPassword = DigestUtils.sha256Hex(user.getHashedPassword());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers(int offset, int limit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        try {
            connection = this.newConnection();
            String query = "SELECT * FROM users LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setUserType(UserType.valueOf(resultSet.getString("user_type").toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString("status").toUpperCase()));
                user.setHashedPassword(resultSet.getString("password_hash"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return users;
    }


    @Override
    public User findUserByEmail(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setUserType(UserType.valueOf(resultSet.getString("user_type").toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString("status").toUpperCase()));
                user.setHashedPassword(resultSet.getString("password_hash"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User findUserById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setUserType(UserType.valueOf(resultSet.getString("user_type").toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString("status").toUpperCase()));
                user.setHashedPassword(resultSet.getString("password_hash"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User updateUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement(
                    "UPDATE users SET email = ?, first_name = ?, last_name = ?, user_type = ?, status = ?, password_hash = ? WHERE id = ?");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getUserType().toString());
            preparedStatement.setString(5, user.getStatus().toString());
            preparedStatement.setString(6, user.getHashedPassword());
            preparedStatement.setInt(7, user.getId());
            preparedStatement.executeUpdate();

            // Return the updated user
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // or throw a custom exception
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }


    @Override
    public void deleteUser(int id) {

    }


}

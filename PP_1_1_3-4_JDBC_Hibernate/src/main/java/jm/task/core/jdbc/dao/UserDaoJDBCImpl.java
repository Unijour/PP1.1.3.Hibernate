package jm.task.core.jdbc.dao;

import com.mysql.cj.jdbc.Driver;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                CREATE TABLE IF NOT EXISTS Users (
                id SERIAL PRIMARY KEY, 
                name VARCHAR(20),
                lastname VARCHAR(20),
                age INT
                );
                """;
        try (Connection connection = Util.connectionToBase();
             Statement statement = connection.createStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                DROP TABLE IF EXISTS Users;
                """;
        try (Connection connection = Util.connectionToBase();
             Statement statement = connection.createStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Class<Driver> driverClass = Driver.class;
        String sql = "INSERT INTO Users(name, lastname, age) VALUES(?, ?, ?);";
        try (Connection connection = Util.connectionToBase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
             preparedStatement.setString(1, name);
             preparedStatement.setString(2, lastName);
             preparedStatement.setByte(3, age);
             preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        Class<Driver> driverClass = Driver.class;
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.connectionToBase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Class<Driver> driverClass = Driver.class;
        String sql = """
                SELECT * FROM Users;
                """;
        try (Connection connection = Util.connectionToBase();
             Statement statement = connection.createStatement()){
             ResultSet resultSet = statement.executeQuery(sql);
             while (resultSet.next()) {
                 User user = new User(resultSet.getString("name"),
                         resultSet.getString("lastname"),
                         resultSet.getByte("age"));
                 user.setId(resultSet.getLong("id"));
                 users.add(user);
             }
             return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                DELETE FROM Users;
                """;
        try (Connection connection = Util.connectionToBase();
             Statement statement = connection.createStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

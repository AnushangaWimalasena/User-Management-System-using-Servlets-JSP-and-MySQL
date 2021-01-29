package com.tetraquark.UserManagement.dao;

import com.tetraquark.UserManagement.bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao{

    private String jdbcURL = "jdbc:mysql://localhost:3306/userdb?useSSL=false";
    private String jdbcUserName = "root";
    private String jdbcPassword = "";
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    private static final String INSERT_USER_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,country from users where id=?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users  where id=?";
    private static final String UPDATE_USERS_SQL = "update users set name=?, email=?, country=? where id=?";

    public UserDao(){}

    //Get db connection
    protected Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("jdbcDriver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //Insert user
    public void insertUsers(User user){
        try{
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    //Select user by id
    public User selectUserByID(int id){
        User user = null;
        try{
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String userName = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(userName, email, country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
    //Select all users

    public List<User> selectAllUsers(){
        List<User> users = new ArrayList<>();

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                String userName = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(userName, email, country));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }


    //Delete user
    public boolean deleteUser(int id){
        boolean result = false;

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);
            statement.setInt(1, id);
            result = statement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    //update users
    public boolean updateUser(User user){
        boolean result = false;
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());
            result = statement.executeUpdate() >0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

}

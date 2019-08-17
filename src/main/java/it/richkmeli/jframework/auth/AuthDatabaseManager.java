package it.richkmeli.jframework.auth;

import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.orm.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class AuthDatabaseManager extends DatabaseManager implements AuthModel {

    public AuthDatabaseManager(String database) throws DatabaseException {
        schemaName = "AuthSchema";
        tableName = schemaName + "." + "auth";
        table = "(" +
                "email VARCHAR(50) NOT NULL PRIMARY KEY," +
                "password VARCHAR(100) NOT NULL," +
                "isAdmin BOOLEAN NOT NULL " + ("mysql".equalsIgnoreCase(dbtype) ? "DEFAULT 0" : "") +
                ")";

        init(database);
    }

    public AuthDatabaseManager() throws DatabaseException {
        this(null);
    }

    public List<User> getAllUsers() throws DatabaseException {
        return readAll(User.class);
    }


    public User getUser(String email) throws DatabaseException {
        return read(new User(email, null));
    }

    public boolean addUser(User user) throws DatabaseException {
        //Logger.info("AuthDatabaseManager, addUser. User: " + user.email);
        //String hash = Crypto.hash(user.getPassword());
        String password = Crypto.hashPassword(user.password, false);
        user.setPassword(password);
        return create(user);
    }

    public boolean removeUser(String email) throws DatabaseException {
        return super.delete(new User(email, ""));
    }


    public boolean isUserPresent(String email) throws DatabaseException {
        return getUser(email) != null;
    }

    public boolean editPassword(String email, String pass) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET pass = ? WHERE email = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean editAdmin(String email, Boolean isAdmin) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET isAdmin = ? WHERE email = ?");
            preparedStatement.setBoolean(1, isAdmin);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean checkPassword(String email, String pass) throws DatabaseException {
        User user = getUser(email);
        if (user != null) {
            return Crypto.verifyPassword(getUser(email).getPassword(), pass);
        } else {
            return false;
        }
    }

    public boolean isAdmin(String email) throws DatabaseException {
        User user = getUser(email);
        if (user != null) {
            return getUser(email).isAdmin();
        } else {
            return false;
        }
    }
}
/*
    public boolean checkPassword(String email, String pass) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isPass = false;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

//            String hash = Crypto.hash(pass);
//
//            if (resultSet.next()) {
//                if (resultSet.getString("password").compareTo(hash) == 0) {
//                    isPass = true;
//                }
//
//            }

            if (resultSet.next()) {
                if (Crypto.verifyPassword(resultSet.getString("password"), pass)) {
                    isPass = true;
                }
            }

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return isPass;
    }

    public boolean isAdmin(String email) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isAdmin = false;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getBoolean("isAdmin")) {
                    isAdmin = true;
                }
            }

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return isAdmin;
    }

}

public boolean addUser(User user) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();

            String hash = Crypto.HashSHA256(user.getPassword());

            preparedStatement = connection.prepareStatement("INSERT IGNORE INTO " + tableName + " (email, pass, isAdmin) VALUES (?,?,?)");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, hash);
            preparedStatement.setBoolean(3, user.isAdmin());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            if (e.getMessage().contains("Duplicate entry")) {
                Logger.error("AuthDatabaseManager, addUser", e);
            } else {
                throw new DatabaseException(e);
            }
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

        public boolean removeUser(String email) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE email = ?");
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

     public List<User> getAllUsers() throws DatabaseException {
        List<User> userList = new ArrayList<User>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User tmp = new User(
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("isAdmin"));
                userList.add(tmp);
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return userList;
    }

*/
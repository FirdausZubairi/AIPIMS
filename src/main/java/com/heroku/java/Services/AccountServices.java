package com.heroku.java.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heroku.java.Model.Users;
import jakarta.servlet.http.HttpSession;

@Service
public class AccountServices {
  private final DataSource dataSource;

  @Autowired
  public AccountServices(DataSource dataSource, HttpSession session) {
    this.dataSource = dataSource;
  }

//Login
  public Users authenticateUser(String username, String password) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Users user = new Users();
            user.setUname(resultSet.getString("username"));
            user.setPword(resultSet.getString("password"));
            user.setRoles(resultSet.getString("role"));
            user.setSid(resultSet.getInt("staffid"));
            return user;
        }
    } catch (SQLException e) {
        throw e;
    }
    return null;
    }

// Add Account
    public void addAccount(Users user) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String insertAccountSql = "INSERT INTO staff(fullname, username, password, role) VALUES(?,?,?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertAccountSql);
            insertStatement.setString(1, user.getName());
            insertStatement.setString(2, user.getUname());
            insertStatement.setString(3, user.getPword());
            insertStatement.setString(4, user.getRoles());
            insertStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

// Display Account
    public List<Users> getAllUsers() throws SQLException {
        List<Users> userList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM staff ORDER BY staffid");

            while (resultSet.next()) {
                Integer sid = resultSet.getInt("staffid");
                String name = resultSet.getString("fullname");
                String uname = resultSet.getString("username");
                String pword = resultSet.getString("password");
                String roles = resultSet.getString("role");

                Users staff = new Users(sid, name, uname, pword, roles);
                userList.add(staff);
            }
        } catch (SQLException e) {
            throw e;
        }
        return userList;
    }

// Update Account
    public Users getAccountDetails(int staffId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM staff WHERE staffid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("fullname");
                String uname = resultSet.getString("username");
                String pword = resultSet.getString("password");
                String roles = resultSet.getString("role");

                return new Users(staffId, name, uname, pword, roles);
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void updateAccount(int staffId, String name, String uname, String pword, String roles) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE staff SET fullname = ?, username = ?, password = ?, role = ? WHERE staffid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, uname);
            statement.setString(3, pword);
            statement.setString(4, roles);
            statement.setInt(5, staffId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

//Delete Account
    // public boolean deleteAccount(int staffId) {
    //     try (Connection connection = dataSource.getConnection()) {
    //         String sql = "DELETE FROM staff WHERE staffid=?;";
    //         PreparedStatement statement = connection.prepareStatement(sql);
    //         statement.setInt(1, staffId);

    //         int rowsAffected = statement.executeUpdate();
    //         return rowsAffected > 0;
    //     } catch (SQLException e) {
    //         // Handle exceptions appropriately
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

    public boolean deleteAccount(int staffId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM staff WHERE staffid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);
    
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }
    
}


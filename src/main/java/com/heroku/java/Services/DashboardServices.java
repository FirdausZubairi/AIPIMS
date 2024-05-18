package com.heroku.java.Services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

@Service
public class DashboardServices {
    private final DataSource dataSource;

  @Autowired
  public DashboardServices(DataSource dataSource, HttpSession session) {
    this.dataSource = dataSource;
  }

    public int getItemCount() throws SQLException {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM item;");
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    public int getStaffCount() throws SQLException {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM staff;");
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    public int getRequestCount() throws SQLException {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM request WHERE status = 'pending';");
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    public int getApproveCount() throws SQLException {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM request WHERE status = 'approved';");
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

}

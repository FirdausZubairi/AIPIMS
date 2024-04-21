package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // @GetMapping("/")
    // public String index() {
    //     return "index";
    // }

    @GetMapping("/dashboard-admin")
    public String dashboardAdmin(Model model) {
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.createStatement();
            final var resultSet = statement.executeQuery(
                    "SELECT COUNT(*) AS count FROM item;");
            resultSet.next();
            int count = resultSet.getInt("count");

            model.addAttribute("itemCount", count);

            Connection connection2 = dataSource.getConnection();
            final var statement2 = connection2.createStatement();
            final var resultSet2 = statement2.executeQuery(
                    "SELECT COUNT(*) AS count FROM staff;");
            resultSet2.next();
            int count2 = resultSet2.getInt("count");

            model.addAttribute("accountCount", count2);

            Connection connection3 = dataSource.getConnection();
            final var statement3 = connection3.createStatement();
            final var resultSet3 = statement3.executeQuery(
                    "SELECT COUNT(*) AS count FROM request;");
            resultSet3.next();
            int count3 = resultSet3.getInt("count");

            model.addAttribute("requestCount", count3);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        return "admin/dashboard-admin";
    }

    @GetMapping("/dashboard-staff")
    public String dashboardS() {
        return "staff/dashboard-staff";
    }

    @GetMapping("/approve-inventory")
    public String approveInventory() {
        return "admin/approve-inventory";
    }

    @GetMapping("/about")
    public String about() {
        return "admin/about";
    }
    
    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }
}

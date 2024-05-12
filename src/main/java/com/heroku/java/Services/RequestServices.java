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

import com.heroku.java.Model.Request;
import jakarta.servlet.http.HttpSession;

@Service
public class RequestServices {
  private final DataSource dataSource;

  @Autowired
  public RequestServices(DataSource dataSource, HttpSession session) {
    this.dataSource = dataSource;
  }
  
  public void addReq(Request request) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String insertRequestSql = "INSERT INTO request(projecttype, reqquantity, status) VALUES(?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertRequestSql);
        insertStatement.setString(1, request.getProtype());
        insertStatement.setInt(2, request.getReqQuantity());
        insertStatement.setString(3, request.getRstatus());
        insertStatement.execute();
    } catch (SQLException e) {
        throw e;
    }
  }

  public List<Request> getAllReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM request WHERE status = 'pending' ORDER BY reqid");

        while (resultSet.next()) {
            Integer rid = resultSet.getInt("reqid");
            String protype = resultSet.getString("projecttype");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String rstatus = resultSet.getString("status");

            Request request = new Request(rid, protype, reqQuantity, rstatus);
            requestList.add(request);
        }
    } catch (SQLException e) {
        throw e;
    }
    return requestList;
  }

  //approve
    public Request getrequestDetails(int requestId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM request WHERE reqid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, requestId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String protype = resultSet.getString("projecttype");
                Integer reqQuantity = resultSet.getInt("reqquantity");
                String status = resultSet.getString("status");
                System.out.println(requestId);
                return new Request(requestId, protype, reqQuantity, status);
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void approveInventory(int requestId, String protype, Integer reqQuantity, String rstatus) throws SQLException {
      try (Connection connection = dataSource.getConnection()) {
          String sql = "UPDATE request SET projecttype = ?, reqquantity = ?, status = ? WHERE reqid = ?";
          PreparedStatement statement = connection.prepareStatement(sql);
          statement.setString(1, protype);
          statement.setInt(2, reqQuantity);
          statement.setString(3, rstatus);
          statement.setInt(4, requestId);
          statement.executeUpdate();
      } catch (SQLException e) {
          throw e;
      }
  }

  //reject
    public Request getDetails(int requestId) throws SQLException {
      try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT * FROM request WHERE reqid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, requestId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String protype = resultSet.getString("projecttype");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String status = resultSet.getString("status");
            System.out.println(requestId);
            return new Request(requestId, protype, reqQuantity, status);
        }
      } catch (SQLException e) {
        throw e;
      }
      return null;
  }

  public void rejectInventory(int requestId, String protype, Integer reqQuantity, String rstatus) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "UPDATE request SET projecttype = ?, reqquantity = ?, status = ? WHERE reqid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, protype);
        statement.setInt(2, reqQuantity);
        statement.setString(3, rstatus);
        statement.setInt(4, requestId);
        statement.executeUpdate();
    } catch (SQLException e) {
        throw e;
    }
  }

//staff view
public List<Request> getReq() throws SQLException {
  List<Request> requestList = new ArrayList<>();
  try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM request ORDER BY reqid");

      while (resultSet.next()) {
          Integer rid = resultSet.getInt("reqid");
          String protype = resultSet.getString("projecttype");
          Integer reqQuantity = resultSet.getInt("reqquantity");
          String rstatus = resultSet.getString("status");

          Request request = new Request(rid, protype, reqQuantity, rstatus);
          requestList.add(request);
      }
  } catch (SQLException e) {
      throw e;
  }
  return requestList;
}
}

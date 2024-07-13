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
  public RequestServices(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public String addReq(Request request, List<String> checkboxValues, HttpSession session) throws SQLException {
    int userid = (int) session.getAttribute("staffid");
    try (Connection connection = dataSource.getConnection()) {
        String insertRequestSql = "INSERT INTO request(projectid, reqquantity, status, datereq, datenext, staffid) VALUES(?,?,?,?,?,?) RETURNING reqid";
        PreparedStatement insertStatement = connection.prepareStatement(insertRequestSql);
        insertStatement.setInt(1, Integer.parseInt(request.getProid()));
        insertStatement.setInt(2, request.getReqQuantity());
        insertStatement.setString(3, request.getRstatus());
        insertStatement.setDate(4, request.getDateReq());
        insertStatement.setString(5, request.getDateNext());
        insertStatement.setInt(6, userid);

        ResultSet resultSet = insertStatement.executeQuery();
        
        // Retrieve the request ID based on database logic
        int reqid = 0;
        if (resultSet.next()) {
            reqid = resultSet.getInt("reqid");
        }

        // Insert request details for each checkbox value
        String insertRequestDetailSql = "INSERT INTO reqdetail(reqid, itemid) VALUES(?,?)";
        PreparedStatement insertStatement2 = connection.prepareStatement(insertRequestDetailSql);

        for (String value : checkboxValues) {
            insertStatement2.setInt(1, reqid);
            insertStatement2.setInt(2, Integer.parseInt(value));
            insertStatement2.addBatch();
        }
        insertStatement2.executeBatch();

    } catch (SQLException e) {
        throw e;
    }
    return null;
  }

  public List<Request> getAllReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        Statement statement = connection.createStatement();
        // ResultSet resultSet = statement.executeQuery("SELECT * FROM request WHERE status = 'pending' ORDER BY reqid");
        ResultSet resultSet = statement.executeQuery("SELECT r.reqid, s.fullname, p.projectname, r.reqquantity,  r.status FROM staff s JOIN request r ON(s.staffid = r.staffid) JOIN project p ON(r.projectid = p.projectid) WHERE status = 'pending'  ORDER BY r.reqid;");
        while (resultSet.next()) {
            Integer rid = resultSet.getInt("reqid");
            String sname = resultSet.getString("fullname");
            String proname = resultSet.getString("projectname");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String rstatus = resultSet.getString("status");

            Request request = new Request(rid, sname, proname, reqQuantity, rstatus);
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
                Integer proid = resultSet.getInt("projectid");
                Integer reqQuantity = resultSet.getInt("reqquantity");
                String status = resultSet.getString("status");
                System.out.println(requestId);
                return new Request(requestId, proid.toString(), reqQuantity, status);
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void approveInventory(int requestId, String proid, Integer reqQuantity, String rstatus) throws SQLException {
      try (Connection connection = dataSource.getConnection()) {
          String sql = "UPDATE request SET projectid = ?, reqquantity = ?, status = ? WHERE reqid = ?";
          PreparedStatement statement = connection.prepareStatement(sql);
          statement.setInt(1, Integer.parseInt(proid));
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
            Integer proid = resultSet.getInt("projectid");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String status = resultSet.getString("status");
            System.out.println(requestId);
            return new Request(requestId, proid.toString(), reqQuantity, status);
        }
      } catch (SQLException e) {
        throw e;
      }
      return null;
  }

  public void rejectInventory(int requestId, String proid, Integer reqQuantity, String rstatus) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "UPDATE request SET projectid = ?, reqquantity = ?, status = ? WHERE reqid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,  Integer.parseInt(proid));
        statement.setInt(2, reqQuantity);
        statement.setString(3, rstatus);
        statement.setInt(4, requestId);
        statement.executeUpdate();
    } catch (SQLException e) {
        throw e;
    }
  }

//staff view
  public List<Request> getReq(HttpSession session) throws SQLException {
    List<Request> requestList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        int sid = (int) session.getAttribute("staffid");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT r.*, p.projectname FROM request r JOIN project p ON (r.projectid = p.projectid) WHERE r.staffid = ? ORDER BY reqid");
        preparedStatement.setInt(1, sid);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer rid = resultSet.getInt("reqid");
            Integer proid = resultSet.getInt("projectid");
            String proname = resultSet.getString("projectname");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String rstatus = resultSet.getString("status");

            Request request = new Request(rid, sid, proid.toString(), proname, reqQuantity, rstatus);
            requestList.add(request);
        }
    }   catch (SQLException e) {
      throw e;
    }
    return requestList;
  }

}

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
  private final HttpSession session;

  @Autowired
  public RequestServices(DataSource dataSource, HttpSession session) {
    this.dataSource = dataSource;
    this.session = session;
  }
  
  public void addReq(Request request) throws SQLException {
    int userid = (int) session.getAttribute("staffid");
    try (Connection connection = dataSource.getConnection()) {
        String insertRequestSql = "INSERT INTO request(projectid, reqquantity, status, staffid) VALUES(?,?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertRequestSql);
        insertStatement.setInt(1, Integer.parseInt(request.getProid()));
        insertStatement.setInt(2, request.getReqQuantity());
        insertStatement.setString(3, request.getRstatus());
        insertStatement.setInt(4, userid);
        insertStatement.executeUpdate();
        //amik projectid dri dropdown, then guna project_item table utk amik id item dia.

    } catch (SQLException e) {
        throw e;
    }
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
public List<Request> getReq() throws SQLException {
  List<Request> requestList = new ArrayList<>();
  try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      int sid = (int) session.getAttribute("staffid");
      ResultSet resultSet = statement.executeQuery("SELECT r.*, p.projectname FROM request r JOIN project p ON (r.projectid = p.projectid) WHERE r.staffid = ? ORDER BY reqid");

      while (resultSet.next()) {
          Integer rid = resultSet.getInt("reqid");
          Integer proid = resultSet.getInt("projectid");
          String proname = resultSet.getString("projectname");
          Integer reqQuantity = resultSet.getInt("reqquantity");
          String rstatus = resultSet.getString("status");

          Request request = new Request(rid, sid, proid.toString(), proname, reqQuantity, rstatus);
          requestList.add(request);
      }
  } catch (SQLException e) {
      throw e;
  }
  return requestList;
}
// public List<Request> getReq(Request req) throws SQLException {
//   List<Request> requestList = new ArrayList<>();
//   try (Connection connection = dataSource.getConnection()) {
//       String sql = "SELECT r.*, p.projectname FROM request r JOIN project p ON (r.projectid = p.projectid) WHERE r.staffid = ? ORDER BY reqid";
//       PreparedStatement statement = connection.prepareStatement(sql);
//       int sid = (int) session.getAttribute("staffid");
//       ResultSet resultSet = statement.executeQuery();

//       while (resultSet.next()) {
//           Integer rid = resultSet.getInt("reqid");
//           Integer proid = resultSet.getInt("projectid");
//           String proname = resultSet.getString("projectname");
//           Integer reqQuantity = resultSet.getInt("reqquantity");
//           String rstatus = resultSet.getString("status");

//           Request request = new Request(rid, sid, proid.toString(), proname, reqQuantity, rstatus);
//           requestList.add(request);
//       }
//   } catch (SQLException e) {
//       throw e;
//   }
//   return requestList;
// }

}

package com.heroku.java.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heroku.java.Model.Predict;
import com.heroku.java.Model.CaseBased;
import com.heroku.java.Model.Item;
// import jakarta.servlet.http.HttpSession;
import com.heroku.java.Model.Request;

@Service
public class PredictServices {
  private final DataSource dataSource;


  @Autowired
  public PredictServices(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public List<CaseBased> getPredict() throws SQLException {
    List<CaseBased> predictList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT v.*, i.itemname, p.projectname FROM cbr v " +
            "JOIN project_item pt ON (v.piid = pt.piid) " +
            "JOIN item i ON (pt.itemid = i.itemid) " + 
            "JOIN project p ON (pt.projectid = p.projectid)"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer cbrID = resultSet.getInt("cbrid");
            String projectName = resultSet.getString("projectname");
            String itemName = resultSet.getString("itemname");
            Integer predictQuan = resultSet.getInt("predictedquantity");
            String years = resultSet.getString("years");

            CaseBased cbr = new CaseBased(cbrID, predictQuan, years, itemName, projectName);
            predictList.add(cbr);
        }
    } catch (SQLException e) {
        throw e;
    }
    return predictList;
  }


  //Search Year
  public List<CaseBased> getPredicts(String year) throws SQLException {
    List<CaseBased> predictList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        String query = "SELECT v.*, i.itemname, p.projectname FROM cbr v " +
                       "JOIN project_item pt ON (v.piid = pt.piid) " +
                       "JOIN item i ON (pt.itemid = i.itemid) " + 
                       "JOIN project p ON (pt.projectid = p.projectid)" +
                       "WHERE v.years LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, year ); // Use '%' for all years if no specific year is provided
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer cbrID = resultSet.getInt("cbrid");
            String projectName = resultSet.getString("projectname");
            String itemName = resultSet.getString("itemname");
            Integer predictQuan = resultSet.getInt("predictedquantity");
            String years = resultSet.getString("years");

            CaseBased casebased = new CaseBased(cbrID, predictQuan, years, itemName, projectName);
            predictList.add(casebased);
        }
    } catch (SQLException e) {
        throw e;
    }
    return predictList;
}

    //Get the Detail before retrieve from request and project_item
    public Request getRetrieveDetails(int rId, int piId) throws SQLException {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT r.*, p.projectName, pi.piID, pi.projectQuantity, i.itemname " +
                            "FROM request r " + 
                            "JOIN project p ON r.projectid = p.projectid " +
                            "JOIN project_item pi ON p.projectid = pi.projectid " + 
                            "JOIN item i ON pi.itemid = i.itemid " + 
                            "WHERE r.status = 'approved' " + 
                                    "AND r.reqid = ? AND pi.piid = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, rId);
                statement.setInt(2, piId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    String proname = resultSet.getString("projectname");
                    Integer reqQuantity = resultSet.getInt("reqquantity");
                    String iname = resultSet.getString("itemname");
                    Integer iquantity = resultSet.getInt("projectquantity");

                    System.out.println("Request ID: " + rId);
                    System.out.println("Project_Item ID: " + piId);

                   return new Request(rId, reqQuantity, proname,  iname, iquantity, piId);
                } 
            } catch (SQLException e) {
                throw e;
            }
            return null;
        }

  //Retrieve Postmapping
  public void addPredict(CaseBased casebased) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String insertPredictSql = "INSERT INTO cbr(reqid, piid, predictedquantity, years) VALUES(?,?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertPredictSql);
        insertStatement.setInt(1, casebased.getReqID());
        insertStatement.setInt(2, casebased.getPiid());
        insertStatement.setInt(3, casebased.getPredictedQuan());
        insertStatement.setString(4, casebased.getYears());
  
        insertStatement.executeUpdate();

    } catch (SQLException e) {
        throw e;
    }
  }

  //req data to predict (retrieve)
  public List<Request> getReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT r.*, p.projectName, pi.piID, pi.projectQuantity, i.itemname " +
                            "FROM request r " + 
                            "JOIN project p ON r.projectid = p.projectid " +
                            "JOIN project_item pi ON p.projectid = pi.projectid " + 
                            "JOIN item i ON pi.itemid = i.itemid " + 
                            "WHERE r.status = 'approved' " + 
                            "ORDER BY r.reqid");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer rid = resultSet.getInt("reqid");
            Integer proid = resultSet.getInt("projectid");
            String proname = resultSet.getString("projectname");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String rstatus = resultSet.getString("status");
            String iname = resultSet.getString("itemname");
            Integer piid = resultSet.getInt("piid");
            Integer iquantity = resultSet.getInt("projectquantity");

            Request request = new Request(rid, proid.toString(), reqQuantity, rstatus, proname, iname, iquantity, piid);
            requestList.add(request);
        }
    }   catch (SQLException e) {
      throw e;
    }
    return requestList;
  }

}
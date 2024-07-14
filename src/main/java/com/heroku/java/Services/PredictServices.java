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

            CaseBased cbr = new CaseBased(cbrID, predictQuan, projectName, itemName, years);
            predictList.add(cbr);
        }
    } catch (SQLException e) {
        throw e;
    }
    return predictList;
  }


  //Search Year
  public List<Predict> getPredicts(String year) throws SQLException {
    List<Predict> predictList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        String query = "SELECT v.*, i.itemname FROM cbr v" + 
                       "JOIN project_item p ON (v.piid = p.piid)" +
                       "JOIN item i ON (p.itemid = i.itemid)" +
                       "WHERE v.years LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, year ); // Use '%' for all years if no specific year is provided
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer reqID = resultSet.getInt("reqid");
            String proName = resultSet.getString("projectname");
            String itemName = resultSet.getString("itemname");
            Integer itemID = resultSet.getInt("itemid");
            Integer predictquan = resultSet.getInt("predictedquantity");
            String yearResult = resultSet.getString("years");

            Predict predict = new Predict(predictquan, itemID.toString(), reqID, yearResult, itemName, proName);
            predictList.add(predict);
        }
    } catch (SQLException e) {
        throw e;
    }
    return predictList;
}

//Get the Detail before retrieve from request and project_item
    public Item getRetrieveDetails(int reqId, int piId) throws SQLException {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT r.*, p.projectName, pi.piID " +
                              "FROM request r " +
                                    "JOIN project p ON r.projectID = p.projectID " + 
                                    "JOIN project_item pi ON p.projectID = pi.projectID " + 
                                    "WHERE r.status = 'approved' " +
                                    "WHERE r.reqid = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, reqId);
                statement.setInt(2, piId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // String iname = resultSet.getString("itemname");
                    // Integer iquantity = resultSet.getInt("itemquantity");
                    // String icategory = resultSet.getString("category");
                    // System.out.println(itemId);
                    // return new Item(itemId, iname, iquantity, icategory);
                }
            } catch (SQLException e) {
                throw e;
            }
            return null;
        }

//Retrieve Postmapping
  public void addPredict(Predict predict) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String insertPredictSql = "INSERT INTO predict_inventory(projectid, itemid, reqid, predictedquantity) VALUES(?,?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertPredictSql);
        insertStatement.setInt(1, Integer.parseInt(predict.getProID()));
        insertStatement.setInt(2, Integer.parseInt(predict.getItemID()));
        insertStatement.setInt(3, predict.getReqID());
        insertStatement.setInt(4, predict.getPredictquan());
  
        insertStatement.executeUpdate();

    } catch (SQLException e) {
        throw e;
    }
  }

  //req data to predict (retrieve)
  public List<Request> getReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT r.*, p.projectName, pi.piID " + 
                    "FROM request r " + 
                    "JOIN project p ON r.projectID = p.projectID " + 
                    "JOIN project_item pi ON p.projectID = pi.projectID " + 
                    "WHERE r.status = 'approved' " + 
                    "ORDER BY r.reqID");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer rid = resultSet.getInt("reqid");
            Integer proid = resultSet.getInt("projectid");
            String proname = resultSet.getString("projectname");
            Integer reqQuantity = resultSet.getInt("reqquantity");
            String rstatus = resultSet.getString("status");

            Request request = new Request(rid, proid.toString(), proname, reqQuantity, rstatus);
            requestList.add(request);
        }
    }   catch (SQLException e) {
      throw e;
    }
    return requestList;
  }

}
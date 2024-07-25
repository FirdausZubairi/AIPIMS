package com.heroku.java.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heroku.java.Model.CaseBased;
// import jakarta.servlet.http.HttpSession;
import com.heroku.java.Model.Request;

@Service
public class PredictServices {
  private final DataSource dataSource;


//   @Autowired
  public PredictServices(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public List<CaseBased> getPredict() throws SQLException {
    List<CaseBased> predictList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT v.*,i.itemid, i.itemquantity, i.itemname, p.projectname, p.projecttype FROM cbr v " +
            "JOIN project_item pt ON (v.piid = pt.piid) " +
            "JOIN item i ON (pt.itemid = i.itemid) " + 
            "JOIN project p ON (pt.projectid = p.projectid)" +
            "ORDER BY v.cbrid"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer cbrID = resultSet.getInt("cbrid");
            String projectName = resultSet.getString("projectname");
            String projectType = resultSet.getString("projecttype");
            String itemName = resultSet.getString("itemname");
            Integer predictQuan = resultSet.getInt("predictedquantity");
            String years = resultSet.getString("years");
            Integer reqID = resultSet.getInt("reqid");
            Integer piid = resultSet.getInt("piid");
            Integer itemid = resultSet.getInt("itemid");
            Integer iquantity = resultSet.getInt("itemquantity");

            CaseBased cbr = new CaseBased(cbrID, predictQuan, years, reqID, piid, itemid, itemName, projectType, projectName, iquantity);
            predictList.add(cbr); 
        }
        connection.close();
    } catch (SQLException e) {
        throw e;
    }
    return predictList;
  }


  //Search Year
  public List<CaseBased> getPredicts(String year) throws SQLException {
    List<CaseBased> predictList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        String query = "SELECT v.*, i.itemid, i.itemquantity, i.itemname, p.projectname, p.projecttype FROM cbr v " +
                        "JOIN project_item pt ON (v.piid = pt.piid) " +
                        "JOIN item i ON (pt.itemid = i.itemid) " + 
                        "JOIN project p ON (pt.projectid = p.projectid) " +
                        // "ORDER BY pt.piid " +
                       "WHERE v.years LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, year ); // Use '%' for all years if no specific year is provided
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer cbrID = resultSet.getInt("cbrid");
            String projectName = resultSet.getString("projectname");
            String projectType = resultSet.getString("projecttype");
            String itemName = resultSet.getString("itemname");
            Integer predictQuan = resultSet.getInt("predictedquantity");
            String years = resultSet.getString("years");
            Integer reqID = resultSet.getInt("reqid");
            Integer piid = resultSet.getInt("piid");
            Integer itemid = resultSet.getInt("itemid");
            Integer iquantity = resultSet.getInt("itemquantity");

            CaseBased cbr = new CaseBased(cbrID, predictQuan, years, reqID, piid, itemid, itemName, projectType, projectName, iquantity);
            predictList.add(cbr); 
        }
        connection.close();
    } catch (SQLException e) {
        throw e;
    }
    return predictList;
}
//===================RETAIN=============================
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
                connection.close();
            } catch (SQLException e) {
                throw e;
            }
            return null;
        }

  //Retain Postmapping
  public void addPredict(CaseBased casebased) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String insertPredictSql = "INSERT INTO cbr(reqid, piid, predictedquantity, years) VALUES(?,?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertPredictSql);
        insertStatement.setInt(1, casebased.getReqID());
        insertStatement.setInt(2, casebased.getPiid());
        insertStatement.setInt(3, casebased.getPredictedQuan());
        insertStatement.setString(4, casebased.getYears());
  
        insertStatement.executeUpdate();
        connection.close();

    } catch (SQLException e) {
        throw e;
    }
  }

  //req data to predict (retrieve)
  public List<Request> getReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT r.*, p.projectName, pi.piID, pi.projectQuantity, i.itemname, " +
                                                                        "cbr.predictedquantity " +
                                                                        "FROM request r " +
                                                                        "JOIN project p ON r.projectid = p.projectid " +
                                                                        "JOIN project_item pi ON p.projectid = pi.projectid " +
                                                                        "JOIN item i ON pi.itemid = i.itemid " +
                                                                        "LEFT JOIN cbr ON r.reqid = cbr.reqid AND pi.piID = cbr.piid " +
                                                                        "WHERE r.status = 'approved' AND cbr.predictedquantity IS NULL " +
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
        connection.close();
    }   catch (SQLException e) {
      throw e;
    }
    return requestList;
  }
//======================REUSE==================================
    public void restockItem(Integer iQuantity, Integer itemId) throws SQLException {
        System.out.println("New Quantity : " + iQuantity);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE item SET itemquantity = ? WHERE itemid = ?"
            );
            preparedStatement.setInt(1, iQuantity);
            preparedStatement.setInt(2, itemId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw e;
        }
    }


//======================REVISED=================================
    public CaseBased getRevisedDetails(int cbrId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT r.reqquantity, p.projectName, pi.projectQuantity, i.itemname, c.* " +
                        "FROM request r " + 
                        "JOIN project p ON r.projectid = p.projectid " +
                        "JOIN project_item pi ON p.projectid = pi.projectid " + 
                        "JOIN item i ON pi.itemid = i.itemid " + 
                        "JOIN cbr c ON pi.piid = c.piid " +
                        "WHERE c.cbrid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cbrId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String projectName = resultSet.getString("projectname");
                Integer reqQuantity = resultSet.getInt("reqquantity");
                String itemName = resultSet.getString("itemname");
                Integer iquantity = resultSet.getInt("projectquantity");
                Integer predictedQuan = resultSet.getInt("predictedquantity");
                String years = resultSet.getString("years");
                Integer reqID = resultSet.getInt("reqid");
                Integer piid = resultSet.getInt("piid");

                return new CaseBased(cbrId, predictedQuan, years, reqID, piid, itemName, projectName, reqQuantity, iquantity);
            }
            connection.close();
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void RevisedPredict(int cbrId, int reqID, int piid, int predictedQuan, String years) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String insertPredictSql = "UPDATE cbr SET reqid = ?, piid = ?, predictedquantity = ?, years = ? WHERE cbrid = ?";
            PreparedStatement insertStatement = connection.prepareStatement(insertPredictSql);
            insertStatement.setInt(1, reqID);
            insertStatement.setInt(2, piid);
            insertStatement.setInt(3, predictedQuan);
            insertStatement.setString(4, years);
            insertStatement.setInt(5, cbrId);
      
            insertStatement.executeUpdate();
            connection.close();
    
        } catch (SQLException e) {
            throw e;
        }
      }
}
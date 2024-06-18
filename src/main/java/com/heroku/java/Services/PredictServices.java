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
// import jakarta.servlet.http.HttpSession;

@Service
public class PredictServices {
  private final DataSource dataSource;


  @Autowired
  public PredictServices(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public List<Predict> getPredict() throws SQLException {
    List<Predict> predictList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT r.reqid, p.projectid, i.itemname, r.reqquantity, p.quantityitem, iv.predictedquantity FROM item i JOIN project_item p ON (i.itemid = p.itemid) JOIN request r ON (p.projectid = r.projectid) JOIN predicted_inventory iv ON (r.reqid = iv.reqid) ORDER BY reqid;");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer reqID = resultSet.getInt("reqid");
            Integer reqQuan = resultSet.getInt("reqquantity");
            Integer proID = resultSet.getInt("projectid");
            Integer quanItem = resultSet.getInt("quantityitem");
            String itemName = resultSet.getString("itemname");
            Integer itemID = resultSet.getInt("itemid");
            Integer predictquan = resultSet.getInt("predictedquantity");

            Predict predict = new Predict(reqID, reqQuan, proID, quanItem, itemName,itemID, predictquan);
            predictList.add(predict);
        }
    }   catch (SQLException e) {
      throw e;
    }
    return predictList;
  }

}
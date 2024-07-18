package com.heroku.java.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heroku.java.Model.Item;

import jakarta.servlet.http.HttpSession;

@Service
public class ItemServices {
  private final DataSource dataSource;

//   @Autowired
  public ItemServices(DataSource dataSource, HttpSession session) {
    this.dataSource = dataSource;
  }

// Add Item
    public void addItem(Item item) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String insertItemSql = "INSERT INTO item(itemname, itemquantity, category) VALUES(?,?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertItemSql);
            insertStatement.setString(1, item.getIname());
            insertStatement.setInt(2, item.getIquantity());
            insertStatement.setString(3, item.getIcategory());
            insertStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }
// Display Item
    public List<Item> getAllItem() throws SQLException {
        List<Item> itemList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM item ORDER BY itemid");

            while (resultSet.next()) {
                Integer iId = resultSet.getInt("itemid");
                String iname = resultSet.getString("itemname");
                Integer iquantity = resultSet.getInt("itemquantity");
                String icategory = resultSet.getString("category");

                Item item = new Item(iId, iname, iquantity, icategory);
                itemList.add(item);
            }
        } catch (SQLException e) {
            throw e;
        }
        return itemList;
    }

// Update Item
    public Item getItemDetails(int itemId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM item WHERE itemid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String iname = resultSet.getString("itemname");
                Integer iquantity = resultSet.getInt("itemquantity");
                String icategory = resultSet.getString("category");
                System.out.println(itemId);
                return new Item(itemId, iname, iquantity, icategory);
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void updateItem(int itemId, String iname, Integer iquantity, String icategory) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE item SET itemname = ?, itemquantity = ?, category = ? WHERE itemid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, iname);
            statement.setInt(2, iquantity);
            statement.setString(3, icategory);
            statement.setInt(4, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

//Delete Item
    public boolean deleteItem(int itemId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM item WHERE itemid=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, itemId);

            System.out.println("hehe");

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return false;
        }
    }
}

package gestion.stock.model;

import gestion.stock.database.ConnectionBD;
import gestion.stock.entity.Order;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderModel {

    public ObservableList<Order> getOrders(long id) throws SQLException {

        ObservableList<Order> list = FXCollections.observableArrayList();
        ArrayList<Order> products = ConnectionBD.getOrders(id);
        for (int i = 0; i < products.size(); i++) {
            list.add(products.get(i));
        }

        return list;
    }

    
    public void deleteOrder(Order product) throws SQLException {
        ConnectionBD.deleteOrder(product);
    }

    public void updateOrder(Order editedOrder) throws SQLException {
        ConnectionBD.updateOrder(editedOrder);
    }

    public void saveOrder(Order product, long id) throws SQLException {
        ConnectionBD.saveOrder(product,id);
    }
    
}

package gestion.stock.interfaces;

import gestion.stock.entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public interface OrderInterface {
    
    public ObservableList<Order> ORDERLIST = FXCollections.observableArrayList();   
}

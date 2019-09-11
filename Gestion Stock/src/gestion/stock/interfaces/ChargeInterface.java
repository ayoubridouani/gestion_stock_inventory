package gestion.stock.interfaces;

import gestion.stock.entity.Charge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public interface ChargeInterface {
    
    public ObservableList<Charge> CHARGELIST = FXCollections.observableArrayList();   
}

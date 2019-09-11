package gestion.stock.interfaces;

import gestion.stock.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public interface ClientInterface {
    
    public ObservableList<Client> CLIENTLIST = FXCollections.observableArrayList();
}

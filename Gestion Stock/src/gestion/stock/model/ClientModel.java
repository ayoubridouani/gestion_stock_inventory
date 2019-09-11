package gestion.stock.model;

import gestion.stock.database.ConnectionBD;
import gestion.stock.entity.Client;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
    private Client user;

    public ClientModel() {
        user = new Client();
    }
    
    

    public ObservableList<Client> getClients() throws SQLException {
        ObservableList<Client> list = FXCollections.observableArrayList();
        ArrayList<Client> users = ConnectionBD.getClients();
        for (int i = 0; i < users.size(); i++) {
            list.add(users.get(i));
        }
        return list;    
    }

    public void saveClient(Client client) throws SQLException {
        ConnectionBD.saveClient(client);
    }

    public void deleteClient(Client selectedClient) throws SQLException {
        ConnectionBD.deleteClient(selectedClient.getId());
    }

    public void updateClient(Client editedClient) throws SQLException {
        ConnectionBD.updateClient(editedClient);
    }
}

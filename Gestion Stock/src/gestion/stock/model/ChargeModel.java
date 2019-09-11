package gestion.stock.model;

import gestion.stock.database.ConnectionBD;
import gestion.stock.entity.Charge;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChargeModel {

    public ObservableList<Charge> getCharges() throws SQLException {

        ObservableList<Charge> list = FXCollections.observableArrayList();
        ArrayList<Charge> charges = ConnectionBD.getCharges();
        for (int i = 0; i < charges.size(); i++) {
            list.add(charges.get(i));
        }

        return list;
    }
    
    public void saveCharge(Charge charge) throws SQLException {
        ConnectionBD.saveCharge(charge);
    }
    
    public void updateCharge(Charge editedCharge) throws SQLException {
        ConnectionBD.updateCharge(editedCharge);
    }

    public void deleteCharge(Charge selectedCharge) throws SQLException {
        ConnectionBD.deleteCharge(selectedCharge);
    }

    public ObservableList<Charge> getCharges(LocalDate localDate1, LocalDate localDate2) throws SQLException {

        ObservableList<Charge> list = FXCollections.observableArrayList();
        ArrayList<Charge> charges = ConnectionBD.getCharges(localDate1, localDate2);
        for (int i = 0; i < charges.size(); i++) {
            list.add(charges.get(i));
        }

        return list;
    }
}
package gestion.stock.controller.charge;

import gestion.stock.entity.Charge;
import gestion.stock.interfaces.ChargeInterface;
import static gestion.stock.interfaces.ChargeInterface.CHARGELIST;
import gestion.stock.model.ChargeModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController implements Initializable, ChargeInterface {
    
    @FXML
    private TextField nameField,priceField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private Button saveButton;
    private ChargeModel chargeModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargeModel = new ChargeModel();
    }
    
    @FXML
    public void handleCancel(ActionEvent event) {
        nameField.setText("");
        priceField.setText("");
        descriptionArea.setText("");
    }
    
    @FXML
    public void handleSave(ActionEvent event) throws SQLException {

        if (validateInput()) {

            Charge charge = new Charge(
                    0,
                    nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    "",
                    descriptionArea.getText()
            );

            chargeModel.saveCharge(charge);
            CHARGELIST.clear();
            CHARGELIST.addAll(chargeModel.getCharges());

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Charge Created!");
            alert.setContentText("Charge is created successfully");
            alert.showAndWait();
        }
    }
    
    private boolean validateInput() {

        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }

        if (priceField.getText() == null || priceField.getText().length() == 0) {
            errorMessage += "No valid Price!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
    
    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
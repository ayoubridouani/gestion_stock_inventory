package gestion.stock.controller.client;

import gestion.stock.entity.Client;
import java.net.URL;
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
import gestion.stock.interfaces.ClientInterface;
import gestion.stock.model.ClientModel;
import java.sql.SQLException;

public class AddController implements Initializable, ClientInterface {

    @FXML
    private TextField fullname, email, telephone, profession, ice;
    
    @FXML
    private TextArea address;
    
    @FXML
    private Button saveButton;
    
    private ClientModel clientModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientModel = new ClientModel();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        fullname.setText("");
        email.setText("");
        telephone.setText("");
        ice.setText("");
        profession.setText("");
        address.setText("");
    }

    @FXML
    public void handleSave(ActionEvent event) throws SQLException {
        if (validateInput()) {
            Client client = new Client(0,fullname.getText(),email.getText(),telephone.getText(),address.getText(),profession.getText(),ice.getText());

            clientModel.saveClient(client);
            CLIENTLIST.clear();
            CLIENTLIST.addAll(clientModel.getClients());

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Client Created!");
            alert.setContentText("Client is created successfully");
            alert.showAndWait();
        }
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (fullname.getText() == null || fullname.getText().length() == 0) {
            errorMessage += "No valid fullname!\n";
        }

        if (email.getText() == null || email.getText().length() == 0) {
            errorMessage += "No valid email!\n";
        }

        if (telephone.getText() == null || telephone.getText().length() == 0) {
            errorMessage += "No valid telephone!\n";
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
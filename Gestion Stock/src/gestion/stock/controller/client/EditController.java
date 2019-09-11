package gestion.stock.controller.client;

import gestion.stock.entity.Client;
import gestion.stock.interfaces.ClientInterface;
import static gestion.stock.interfaces.ClientInterface.CLIENTLIST;
import gestion.stock.model.ClientModel;
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

public class EditController implements Initializable, ClientInterface {

    @FXML
    private TextField fullname, email, telephone, profession, ice;
    
    @FXML
    private TextArea address;
    
    @FXML
    private Button saveButton;
    
    private long selectedClientId;
    private ClientModel clientModel;
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientModel = new ClientModel();
        resetValues();
    }

    public void setClient(Client client, long selectedClientId) {
        this.client = client;
        this.selectedClientId = selectedClientId;
        setData();
    }

    @FXML
    public void handleSave(ActionEvent event) throws SQLException {
        if (validateInput()) {
            Client editedClient = new Client(client.getId(),fullname.getText(),email.getText(),telephone.getText(),address.getText(),profession.getText(),ice.getText());

            clientModel.updateClient(editedClient);
            CLIENTLIST.set((int) selectedClientId, editedClient);

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Client Updated!");
            alert.setContentText("Client is updated successfully");
            alert.showAndWait();
        }
    }

    private void setData() {
        fullname.setText(client.getFullname());
        email.setText(client.getEmail());
        telephone.setText(client.getTelephone());
        address.setText(client.getAddress());
        profession.setText(client.getProfession());
        ice.setText(client.getIce());
    }

    private void resetValues() {
        fullname.setText("");
        email.setText("");
        telephone.setText("");
        address.setText("");
        profession.setText("");
        ice.setText("");
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        resetValues();
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
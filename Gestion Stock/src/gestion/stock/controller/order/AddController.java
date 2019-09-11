package gestion.stock.controller.order;

import gestion.stock.entity.Order;
import static gestion.stock.interfaces.OrderInterface.ORDERLIST;
import gestion.stock.model.OrderModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import gestion.stock.interfaces.OrderInterface;

public class AddController implements Initializable, OrderInterface {

    @FXML
    private TextField titleField, descriptionField;
    
    @FXML
    private Button saveButton;

    private OrderModel orderModel;
    private long id;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = new OrderModel();
    }

    @FXML
    public void handleSave(ActionEvent event) throws SQLException {

        if (validateInput()) {

            Order order = new Order(
                    0,
                    titleField.getText(),
                    descriptionField.getText(),
                    null
            );

            orderModel.saveOrder(order,id);
            ORDERLIST.clear();
            ORDERLIST.addAll(orderModel.getOrders(id));

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Order is added");
            alert.setContentText("Order is added successfully");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        titleField.setText("");
        descriptionField.setText("");
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
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

    void setClient(long id) {
        this.id = id;
    }
}
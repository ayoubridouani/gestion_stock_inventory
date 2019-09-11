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

public class EditController implements Initializable, OrderInterface {

    @FXML
    private TextField titleField, descriptionField;

    @FXML
    private Button saveButton;
    
    private OrderModel orderModel;
    private Order order;
    private long selectedOrderId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = new OrderModel();
        
        resetValues();
    }

    public void setOrder(Order order, long selectedOrderId) {
        this.order = order;
        this.selectedOrderId = selectedOrderId;
        setData();
    }

    private void setData() {
        titleField.setText(order.getTitle());
        descriptionField.setText(String.valueOf(order.getDescription()));
    }

    @FXML
    public void handleSave(ActionEvent event) throws SQLException {

        if (validateInput()) {
            Order editedOrder = new Order(
                order.getId(),
                titleField.getText(),
                descriptionField.getText(),
                null
            );

            orderModel.updateOrder(editedOrder);
            ORDERLIST.set((int) selectedOrderId, editedOrder);

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Order Updated!");
            alert.setContentText("Order is updated successfully");
            alert.showAndWait();
        }
    }

    private void resetValues() {
        titleField.setText("");
        descriptionField.setText("");
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        resetValues();
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
}
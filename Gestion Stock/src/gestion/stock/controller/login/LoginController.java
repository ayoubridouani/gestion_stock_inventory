package gestion.stock.controller.login;

import gestion.stock.model.UserModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController {
    
    @FXML
    private TextField emailField, passwordField;
    
    @FXML
    private Label errorLabel;
    
    private final UserModel model;

    public LoginController() {
        model = new UserModel();
    }   
    
    @FXML
    public void loginAction(ActionEvent event) throws Exception {
        String errorMessage = "";

        if (emailField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Invalid information";
            errorLabel.setText(errorMessage);
        }

        if (errorMessage.length() == 0) {
            String email = emailField.getText().trim();
            String password = DigestUtils.sha1Hex((passwordField.getText().trim()));
            
            if(model.checkUser(email)) {
                if (model.checkPassword(email, password)) {
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    String type = model.getUserType(email);
                    windows("/gui/Client.fxml", "Admin Panel");
                } else {
                    passwordField.setText("");
                    errorLabel.setText("Bad Password");
                }
            } else {
                emailField.setText("");
                passwordField.setText("");
                errorLabel.setText("The user does not exist");
            }
        }
    }
    
    @FXML
    public void cancelAction(ActionEvent event) {
        emailField.setText("");
        passwordField.setText("");
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void minusAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    private void windows(String path, String title) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }
}
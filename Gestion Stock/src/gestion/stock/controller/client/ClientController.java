package gestion.stock.controller.client;

import gestion.stock.controller.order.OrderController;
import static gestion.stock.interfaces.ClientInterface.CLIENTLIST;
import gestion.stock.entity.Client;
import gestion.stock.model.ClientModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Modality;
import gestion.stock.interfaces.ClientInterface;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ClientController implements Initializable, ClientInterface {

    @FXML
    private TableView<Client> clientTable;
    
    @FXML
    private TableColumn<Client, Long> idColumn;
    
    @FXML
    private TableColumn<Client, String> fullnameColumn, emailColumn, telephoneColumn,
            addressColumn, iceColumn, professionColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button editButton, deleteButton, showProduct;
    private ClientModel model;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button menu;
    
    @FXML
    private VBox drawer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ClientModel();

        drawerAction();
        try {
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullnameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        iceColumn.setCellValueFactory(new PropertyValueFactory<>("ice"));
        professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession"));

        clientTable.setItems(CLIENTLIST);

        filterData();

        showProduct
                .disableProperty()
                .bind(Bindings.isEmpty(clientTable.getSelectionModel().getSelectedItems()));
        
        editButton
                .disableProperty()
                .bind(Bindings.isEmpty(clientTable.getSelectionModel().getSelectedItems()));
        deleteButton
                .disableProperty()
                .bind(Bindings.isEmpty(clientTable.getSelectionModel().getSelectedItems()));
    }

    private void filterData() {
        FilteredList<Client> searchedData = new FilteredList<>(CLIENTLIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getFullname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (user.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (user.getTelephone().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (user.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Client> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(clientTable.comparatorProperty());
            clientTable.setItems(sortedData);
        });
    }

    private void loadData() throws SQLException {
        if (!CLIENTLIST.isEmpty()) {
            CLIENTLIST.clear();
        }
        CLIENTLIST.addAll(model.getClients());
    }

    private void drawerAction() {
        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        menu.setOnAction((ActionEvent evt) -> {
            if (drawer.getTranslateX() != 0) {
                openNav.play();
                menu.getStyleClass().remove("hamburger-button");
                menu.getStyleClass().add("open-menu");
            } else {
                closeNav.setToX(-(drawer.getWidth()));
                closeNav.play();
                menu.getStyleClass().remove("open-menu");
                menu.getStyleClass().add("hamburger-button");
            }
        });
    }

    @FXML
    public void clientAction(ActionEvent event) throws Exception {
        windows("/gui/Client.fxml", "Clients", event);
    }
    
    @FXML
    public void chargeAction(ActionEvent event) throws Exception {
        windows("/gui/Charge.fxml", "Charge", event);
    }

    @FXML
    public void logoutAction(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        stage.setTitle("Gestion Stock");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    private void windows(String path, String title, ActionEvent event) throws Exception {
        double width = ((Node) event.getSource()).getScene().getWidth();
        double height = ((Node) event.getSource()).getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void addAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/client/Add.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Client");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void showProduct(ActionEvent event) throws Exception {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/gui/Order.fxml")));
        OrderController controller = new OrderController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Commandes");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
        controller.setClient(selectedClient);
    }
    
    @FXML
    public void editAction(ActionEvent event) throws Exception {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        int selectedClientId = clientTable.getSelectionModel().getSelectedIndex();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/gui/client/Edit.fxml")));
        EditController controller = new EditController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Details");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        controller.setClient(selectedClient, selectedClientId);
        clientTable.getSelectionModel().clearSelection();

    }

    @FXML
    public void deleteAction(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Remove Client");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();

            model.deleteClient(selectedClient);
            CLIENTLIST.remove(selectedClient);
        }

        clientTable.getSelectionModel().clearSelection();
    }
}

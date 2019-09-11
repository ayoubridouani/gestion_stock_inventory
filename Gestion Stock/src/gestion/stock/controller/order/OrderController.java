package gestion.stock.controller.order;

import gestion.stock.entity.Client;
import gestion.stock.model.OrderModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import static gestion.stock.interfaces.OrderInterface.ORDERLIST;
import gestion.stock.entity.Order;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import gestion.stock.interfaces.OrderInterface;

public class OrderController implements Initializable, OrderInterface {

    @FXML
    private TableView<Order> orderTable;
    
    @FXML
    private TableColumn<Order, Long> idColumn;
    
    @FXML
    private TableColumn<Order, String> titleColumn, descriptionColumn,dateColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button editButton, deleteButton;
    
    @FXML
    CategoryAxis pxAxis;

    @FXML
    private Button menu;
    
    @FXML
    private VBox drawer;

    private OrderModel model;
    private Client client;
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new OrderModel();
        
        editButton
                .disableProperty()
                .bind(Bindings.isEmpty(orderTable.getSelectionModel().getSelectedItems()));
        deleteButton
                .disableProperty()
                .bind(Bindings.isEmpty(orderTable.getSelectionModel().getSelectedItems()));        
    }

    public void setClient(Client selectedClient) {
        this.client = selectedClient;
        
        try {
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderTable.setItems(ORDERLIST);

        filterData();
    }
    
    private void filterData() {

        FilteredList<Order> searchedData = new FilteredList<>(ORDERLIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(order -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return order.getTitle().toLowerCase().contains(lowerCaseFilter);
                });
            });

            SortedList<Order> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(orderTable.comparatorProperty());
            orderTable.setItems(sortedData);
        });
    }

    private void loadData() throws SQLException {
        if (!ORDERLIST.isEmpty()) {
            ORDERLIST.clear();
        }
        ORDERLIST.addAll(model.getOrders(client.getId()));
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
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        int selectedOrderId = orderTable.getSelectionModel().getSelectedIndex();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/gui/order/Add.fxml")));
        AddController controller = new AddController();
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
        stage.setTitle("New Order");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        controller.setClient(client.getId());
    }

    @FXML
    public void editAction(ActionEvent event) throws Exception {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        int selectedOrderId = orderTable.getSelectionModel().getSelectedIndex();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/gui/order/Edit.fxml")));
        EditController controller = new EditController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Order");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        controller.setOrder(selectedOrder, selectedOrderId);
        orderTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void deleteAction(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Order");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();

            model.deleteOrder(selectedOrder);
            ORDERLIST.remove(selectedOrder);
        }

        orderTable.getSelectionModel().clearSelection();
    }

}

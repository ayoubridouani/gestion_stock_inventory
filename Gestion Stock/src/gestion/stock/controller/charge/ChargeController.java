package gestion.stock.controller.charge;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gestion.stock.entity.Charge;
import gestion.stock.interfaces.ChargeInterface;
import static gestion.stock.interfaces.ChargeInterface.CHARGELIST;
import gestion.stock.model.ChargeModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ChargeController implements Initializable, ChargeInterface {

    @FXML
    private TableView<Charge> chargeTable;
    
    @FXML
    private TableColumn<Charge, Long> idColumn;
    
    @FXML
    private TableColumn<Charge, String> dateColumn, nameColumn, descriptionColumn;
    
    @FXML
    private TableColumn<Charge, Double> priceColumn;
    
    @FXML
    private TextField searchField,filename;
    
    @FXML
    private Button editButton, deleteButton;
    
    @FXML
    private DatePicker datePicker1,datePicker2;
    
    private ChargeModel model;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button menu,filter;
    
    @FXML
    private VBox drawer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ChargeModel();

        drawerAction();
        try {
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(ChargeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        chargeTable.setItems(CHARGELIST);

        filterData();

        editButton
                .disableProperty()
                .bind(Bindings.isEmpty(chargeTable.getSelectionModel().getSelectedItems()));
        deleteButton
                .disableProperty()
                .bind(Bindings.isEmpty(chargeTable.getSelectionModel().getSelectedItems()));
    }

    private void filterData() {
        FilteredList<Charge> searchedData = new FilteredList<>(CHARGELIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(charge -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (charge.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (charge.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Charge> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(chargeTable.comparatorProperty());
            chargeTable.setItems(sortedData);
        });
    }

    private void loadData() throws SQLException {
        if (!CHARGELIST.isEmpty()) {
            CHARGELIST.clear();
        }
        CHARGELIST.addAll(model.getCharges());
        chargeTable.setItems(CHARGELIST);
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
    public void filter(ActionEvent event) throws SQLException{
        LocalDate localDate1 = datePicker1.getValue();
        LocalDate localDate2 = datePicker2.getValue();
        if(localDate1 == null || localDate2 == null){
            loadData();
        }else{
        if (!CHARGELIST.isEmpty()) {
            CHARGELIST.clear();
        }
        CHARGELIST.addAll(model.getCharges(localDate1,localDate2));
        }
    }
    
    @FXML
    private void pdfs() throws Exception{
        Document my_pdf_report = new Document();
        if(filename.getText().isEmpty())
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("unnamed.pdf"));
        else 
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream(filename.getText()));
        my_pdf_report.open();
        //we have four columns in our table
        PdfPTable my_report_table = new PdfPTable(2);
        //create a cell object
        PdfPCell table_cell;
        for(int i=0;i<chargeTable.getItems().size();i++){
           String name=chargeTable.getItems().get(i).getName();
           table_cell=new PdfPCell(new Phrase(name));
           my_report_table.addCell(table_cell);
           String price=chargeTable.getItems().get(i).getPrice().toString();
           table_cell=new PdfPCell(new Phrase(price));
           my_report_table.addCell(table_cell);
        }

        /* Attach report table to PDF */
        my_pdf_report.add(my_report_table);
        my_pdf_report.close();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful");
        alert.setHeaderText("PDF exported!");
        alert.setContentText("PDF is exported successfully");
        alert.showAndWait();
    }
    
    @FXML
    public void clientAction(ActionEvent event) throws Exception {
        windows("/gui/Client.fxml", "Client", event);
    }

    @FXML
    public void chargeAction(ActionEvent event) throws Exception {

        windows("/gui/Charge.fxml", "Charge", event);
    }

    @FXML
    public void logoutAction(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
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

        Parent root = FXMLLoader.load(getClass().getResource("/gui/charge/Add.fxml"));
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
        stage.setTitle("New Charge");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editAction(ActionEvent event) throws Exception {

        Charge selectedCharge = chargeTable.getSelectionModel().getSelectedItem();
        int selectedChargeId = chargeTable.getSelectionModel().getSelectedIndex();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/gui/charge/Edit.fxml")));
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
        stage.setTitle("Edit Charge");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        controller.setCharge(selectedCharge, selectedChargeId);
        chargeTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void deleteAction(ActionEvent event) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Charge");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Charge selectedCharge = chargeTable.getSelectionModel().getSelectedItem();

            model.deleteCharge(selectedCharge);
            CHARGELIST.remove(selectedCharge);
        }

        chargeTable.getSelectionModel().clearSelection();
    }
}

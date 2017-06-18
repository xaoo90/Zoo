/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.warehouse;

import dao.FeedFacade;
import dao.WarehouseFacade;
import entity.Feed;
import entity.Warehouse;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.UtilView;

public class WarehouseFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    WarehouseFacade warF = new WarehouseFacade(emf);
    FeedFacade feeF = new FeedFacade(emf);
    
    ObservableList<Warehouse> lstWarehouse = FXCollections.observableList(warF.findWarehouseEntities());

    @FXML
    private TableView<Warehouse> tableWarehouse;
    @FXML
    private TableColumn<Warehouse, Integer> warehouseId;
    @FXML
    private TableColumn<Warehouse, String> warehouseName;

    @FXML
    private TableView<Feed> tableFeed;
    @FXML
    private TableColumn<Feed, String> feedName;
    @FXML
    private TableColumn<Feed, BigDecimal> feedAvailability;
    @FXML
    private TableColumn<Feed, String> feedUnit;

    public void showWarehouse() {
        warehouseId.setCellValueFactory(new PropertyValueFactory("idWarehouse"));
        warehouseName.setCellValueFactory(new PropertyValueFactory("name"));
        tableWarehouse.setItems(lstWarehouse);
        tableWarehouse.getSelectionModel().selectFirst();
    }

    public void showSectorInfo() {
        tableWarehouse.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableWarehouse.getSelectionModel().getSelectedIndex() >= 0) {
                        ObservableList<Feed> lstFeed = FXCollections.observableArrayList(warF.showFeed(tableWarehouse.getSelectionModel().getSelectedItem()));
                        feedName.setCellValueFactory(new PropertyValueFactory("name"));
                        feedAvailability.setCellValueFactory(new PropertyValueFactory("availability"));
                        feedUnit.setCellValueFactory(new PropertyValueFactory("unit"));
                        tableFeed.setItems(lstFeed);
                        tableFeed.getSelectionModel().selectFirst();
                    }
                }
            }
        });
    }

    @FXML
    public void addWarehouse(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/warehouse/AddWarehouseFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie magazynu");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editWarehouse(ActionEvent event) throws Exception {
        UtilView.setSellectedWarehouse(tableWarehouse.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/warehouse/EditWarehouseFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja magazynu");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);        
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void delWarehouse(ActionEvent event) {
        warF.deleteWarehouse(tableWarehouse.getSelectionModel().getSelectedItem().getIdWarehouse());
        tableWarehouse.getItems().remove(tableWarehouse.getSelectionModel().getSelectedIndex());
    }
    
    public void addFeed(ActionEvent event) throws Exception {
        UtilView.setSellectedWarehouse(tableWarehouse.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/warehouse/feed/AddFeedFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie pożywienia");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editFeed(ActionEvent event) throws Exception {
        UtilView.setSellectedWarehouse(tableWarehouse.getSelectionModel().getSelectedItem());
        UtilView.setSellectedFeed(tableFeed.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/warehouse/feed/EditFeedFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja pożywienia");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);        
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void delFeed(ActionEvent event) {
        feeF.deleteFeed(tableFeed.getSelectionModel().getSelectedItem().getIdFeed());
        tableFeed.getItems().remove(tableFeed.getSelectionModel().getSelectedIndex());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableWarehouse.setPlaceholder(new Label("Brak magazynów"));
        tableFeed.setPlaceholder(new Label("Brak jedzenia w wybranym magazynie"));
        showWarehouse();
        showSectorInfo();
    }
}

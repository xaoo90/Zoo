/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.sector;

import dao.SectorFacade;
import entity.Cage;
import entity.Sector;
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

public class SectorFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    SectorFacade secF = new SectorFacade(emf);

    ObservableList<Sector> lstSector = FXCollections.observableList(secF.findSectorEntities());

    @FXML
    private TableView<Sector> tableSector = new TableView();
    @FXML
    private TableColumn<Sector, String> sectorName;
    @FXML
    private TableColumn<Sector, String> sectorType;

    @FXML
    private TableView<Cage> tableCage = new TableView();
    @FXML
    private TableColumn<Cage, Integer> cageId;
    @FXML
    private TableColumn<Cage, String> cageType;
    @FXML
    private TableColumn<Cage, String> cageCondition;
    @FXML
    private TableColumn<Cage, String> cageSpace;

    @FXML
    private Label lblEmployee;

    public void showSector() {
        sectorName.setCellValueFactory(new PropertyValueFactory("name"));
        sectorType.setCellValueFactory(new PropertyValueFactory("type"));
        tableSector.setItems(lstSector);
    }

    public void showSectorInfo() {
        tableSector.getSelectionModel().selectFirst();
        tableSector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableSector.getSelectionModel().getSelectedIndex() >= 0) {
                        if (tableSector.getSelectionModel().getSelectedItem().getManager() == null) {
                            lblEmployee.setText("Brak osoby zarządzającej sektorem");
                            tableCage.setItems(FXCollections.emptyObservableList());
                        } else {
                            lblEmployee.setText("Sektor zarządzany przez : " + tableSector.getSelectionModel().getSelectedItem().getManager().getFirstName() + " " + tableSector.getSelectionModel().getSelectedItem().getManager().getLastName());
                            ObservableList<Cage> lstCage = FXCollections.observableArrayList(secF.showCage(tableSector.getSelectionModel().getSelectedItem()));
                            cageId.setCellValueFactory(new PropertyValueFactory("idCage"));
                            cageType.setCellValueFactory(new PropertyValueFactory("type"));
                            cageCondition.setCellValueFactory(new PropertyValueFactory("condition"));
                            cageSpace.setCellValueFactory(new PropertyValueFactory("space"));
                            tableCage.setItems(lstCage);
                        }
                    }
                }
            }
        });
    }

    @FXML
    public void addSector(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/sector/AddSectorFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie sektoru");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editSector(ActionEvent event) throws Exception {
        UtilView.setSellectedSector(tableSector.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/sector/EditSectorFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja sektoru");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void delSector(ActionEvent event) {
        secF.deleteSector(tableSector.getSelectionModel().getSelectedItem().getIdSector());
        tableSector.getItems().remove(tableSector.getSelectionModel().getSelectedIndex());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableCage.setPlaceholder(new Label("Brak klatek w wybranym sektorze"));
        showSector();
        showSectorInfo();
    }
}

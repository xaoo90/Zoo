/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.cage;

import dao.CageFacade;
import entity.Animal;
import entity.Cage;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utilLogic.UtilLogic;
import util.UtilView;

public class CageFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    CageFacade cagF = new CageFacade(emf);

    ObservableList<Cage> lstCage = FXCollections.observableList(cagF.findCageEntities());

    @FXML
    private TableView<Cage> tableCage = new TableView();
    @FXML
    private TableColumn<Cage, Integer> cageId;
    @FXML
    private TableColumn<Cage, String> cageType;
    @FXML
    private TableColumn<Cage, String> cageSpace;

    @FXML
    private TableView<Animal> tableAnimal = new TableView();
    @FXML
    private TableColumn<Animal, String> animalSpecies;
    @FXML
    private TableColumn<Animal, String> animalSex;

    @FXML
    private Label lblSector;
    @FXML
    private Label lblId;
    @FXML
    private Label lblBirthDate;
    @FXML
    private Label lblArrivalDate;
    @FXML
    private Label lblSource;
    
    @FXML
    private TextArea txtArea;


    public void showCage() {
        cageId.setCellValueFactory(new PropertyValueFactory("idCage"));
        cageType.setCellValueFactory(new PropertyValueFactory("type"));
        cageSpace.setCellValueFactory(new PropertyValueFactory("space"));
        tableCage.setItems(lstCage);
    }

    public void showCageInfo() {
        tableCage.getSelectionModel().selectFirst();
        tableCage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableCage.getSelectionModel().getSelectedIndex() >= 0) {
                        lblId.setText(null);
                        lblBirthDate.setText(null);
                        lblArrivalDate.setText(null);
                        txtArea.setText(null);
                        lblSource.setText(null);
                        lblSector.setText("Klatka znajduje sie w sektorze : " + tableCage.getSelectionModel().getSelectedItem().getSector().getName() + " " + tableCage.getSelectionModel().getSelectedItem().getSector().getType());
                        ObservableList<Animal> lstAnimal = FXCollections.observableArrayList(cagF.showAnimal(tableCage.getSelectionModel().getSelectedItem()));
                        animalSpecies.setCellValueFactory(new PropertyValueFactory("species"));
                        animalSex.setCellValueFactory(new PropertyValueFactory("sex"));
                        tableAnimal.setItems(lstAnimal);
                    }
                }
            }
        });
    }

    public void showAnimalInfo() {
        tableAnimal.getSelectionModel().selectFirst();
        tableAnimal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableAnimal.getSelectionModel().getSelectedIndex() >= 0) {
                        lblId.setText(String.valueOf(tableAnimal.getSelectionModel().getSelectedItem().getIdAnimal()));
                        lblArrivalDate.setText(UtilLogic.dateFormat(tableAnimal.getSelectionModel().getSelectedItem().getArrivalDate()));
                        lblBirthDate.setText(UtilLogic.dateFormat(tableAnimal.getSelectionModel().getSelectedItem().getBirthDate()));
                        txtArea.setText(tableAnimal.getSelectionModel().getSelectedItem().getDescription());
                        lblSource.setText(tableAnimal.getSelectionModel().getSelectedItem().getSource());
                    }
                }
            }
        });
    }
    
    @FXML
    public void addCage(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/cage/AddCageFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie klatki");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void editCage(ActionEvent event) throws Exception {
        UtilView.setSellectedCage(tableCage.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/cage/EditCageFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja klatki");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void delCage(ActionEvent event) {
        cagF.deleteCage(tableCage.getSelectionModel().getSelectedItem().getIdCage());
        tableCage.getItems().remove(tableCage.getSelectionModel().getSelectedIndex());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableAnimal.setPlaceholder(new Label("Brak zwierząt"));
        tableCage.setPlaceholder(new Label("Brak klatek"));
        showCage();
        showCageInfo();
        showAnimalInfo();
    }

}

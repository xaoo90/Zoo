/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keeper.animal;

import dao.AnimalFacade;
import dao.AnimalFeedFacade;
import entity.Animal;
import entity.AnimalFeed;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utilLogic.UtilLogic;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class AnimalFXML implements Initializable {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    AnimalFacade aniF = new AnimalFacade(emf);
    AnimalFeedFacade afeF = new AnimalFeedFacade(emf);
    
    ObservableList<Animal> lstAnimal = FXCollections.observableList(aniF.findAnimalEntities());

    @FXML
    private TableView<Animal> tableAnimal = new TableView();
    @FXML
    private TableColumn<Animal, String> animalSpecies;
    @FXML
    private TableColumn<Animal, String> animalSex;
    
    @FXML
    private TableView<AnimalFeed> tableAnimalFeed = new TableView();
    @FXML
    private TableColumn<AnimalFeed, String> feedAnimal;
    @FXML
    private TableColumn<AnimalFeed, String> feedPortion;
    @FXML
    private TableColumn<AnimalFeed, String> feedTime;
    @FXML
    private TableColumn<AnimalFeed, String> feedFrequency;
    
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
    @FXML
    private Label lblTypeCage;
    @FXML
    private Label lblIdCage;
    
    public void showAnimal() {
        animalSpecies.setCellValueFactory(new PropertyValueFactory("species"));
        animalSex.setCellValueFactory(new PropertyValueFactory("sex"));
        tableAnimal.setItems(lstAnimal);
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
                        lblIdCage.setText(String.valueOf(tableAnimal.getSelectionModel().getSelectedItem().getCage().getIdCage()));
                        lblTypeCage.setText(tableAnimal.getSelectionModel().getSelectedItem().getCage().getType());
                        showAnimalFeed(tableAnimal.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });
    }
    
    public void showAnimalFeed(Animal a) {
        ObservableList<AnimalFeed> lstFeed = FXCollections.observableArrayList(aniF.showAnimalFeed(a));
        feedAnimal.setCellValueFactory(new PropertyValueFactory("feed"));
        feedPortion.setCellValueFactory(new PropertyValueFactory("portion"));
        feedTime.setCellValueFactory(new PropertyValueFactory("feedTime"));
        feedFrequency.setCellValueFactory(new PropertyValueFactory("frequency"));
        tableAnimalFeed.setItems(lstFeed);
        tableAnimalFeed.getSelectionModel().selectFirst();
    }

    @FXML
    public void feed() throws IOException {
        UtilView.setSellectedAnimalFeed(tableAnimalFeed.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/keeper/animalFeed/FeedFXML.fxml"));
        BorderPane border = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Karmienie zwierząt");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(border);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showAnimal();
        showAnimalInfo();
        tableAnimalFeed.setPlaceholder(new Label("Zwierzę nie zostalo wybrane lub brak pożywienia dla wybranego zwierzecia"));
    }    
    
}


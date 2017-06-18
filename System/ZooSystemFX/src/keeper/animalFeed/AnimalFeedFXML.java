/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keeper.animalFeed;

import dao.AnimalFacade;
import dao.AnimalFeedFacade;
import entity.AnimalFeed;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import keeper.animal.AnimalFXML;
import org.joda.time.LocalDateTime;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class AnimalFeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    AnimalFacade aniF = new AnimalFacade(emf);
    AnimalFeedFacade afeF = new AnimalFeedFacade(emf);

    ObservableList<AnimalFeed> lstAnimalFeed = FXCollections.observableList(showAnimalFeedCurrentList(afeF.getAnimalFeedType(showAnimalFeedType())));

    AnimalFXML aFXML = new AnimalFXML();
    
    @FXML
    private TableView<AnimalFeed> tableAnimalFeed = new TableView();
    @FXML
    private TableColumn<AnimalFeed, String> feedAnimal;
    @FXML
    private TableColumn<AnimalFeed, String> feedFeed;
    @FXML
    private TableColumn<AnimalFeed, String> feedPortion;
    @FXML
    private TableColumn<AnimalFeed, String> feedTime;
    @FXML
    private TableColumn<AnimalFeed, String> feedFrequency;

    public void showAnimalFeed() {
        feedAnimal.setCellValueFactory(new PropertyValueFactory("animal"));
        feedFeed.setCellValueFactory(new PropertyValueFactory("feed"));
        feedPortion.setCellValueFactory(new PropertyValueFactory("portion"));
        feedTime.setCellValueFactory(new PropertyValueFactory("feedTime"));
        feedFrequency.setCellValueFactory(new PropertyValueFactory("frequency"));
        tableAnimalFeed.setItems(lstAnimalFeed);        
        tableAnimalFeed.getSelectionModel().selectFirst();
    }

    public String showAnimalFeedType() {
        int hour = LocalDateTime.now().getHourOfDay();

        if (hour >= 5 && hour <= 7) {
            return "Rano";
        } else if (hour >= 11 && hour <= 13) {
            return "Południe";
        } else if (hour >= 18 && hour <= 20) {
            return "Wieczór";
        } else {
            return "";
        }
    }

    public List<AnimalFeed> showAnimalFeedCurrentList(List<AnimalFeed> lstAnimalFeed) {
        int dayOfWeek = LocalDateTime.now().getDayOfWeek();
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();

        List<AnimalFeed> lst = new ArrayList();

        for (AnimalFeed af : lstAnimalFeed) {
            if (af.getFrequency().equals("Codziennie") || af.getFrequency().equals(UtilView.intToDayOfWeek(dayOfWeek)) || af.getFrequency().equals(String.valueOf(dayOfMonth))) {
                lst.add(af);
            }
        }
        return lst;
    }
    
    @FXML
    public void feed() throws IOException {
        UtilView.setSellectedAnimalFeed(tableAnimalFeed.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/keeper/animalFeed/FeedFXML.fxml"));
        BorderPane addEmpl = loader.load();
        Stage addDialogStage = new Stage();
        addDialogStage.setTitle("Karmienie zwierząt");
        addDialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(addEmpl);
        addDialogStage.setScene(scene);
        addDialogStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showAnimalFeed();
        showAnimalFeedType();
        tableAnimalFeed.setPlaceholder(new Label("Zaplanowane karmienia widoczne są w godzinach\n"
                + "Poranne : 5:00 - 7:00\n"
                + "Południowe : 11:00 - 13:00\n"
                + "Wieczorne : 18:00 - 20:00"));
    }
}

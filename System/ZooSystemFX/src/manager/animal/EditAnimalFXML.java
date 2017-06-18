/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.animal;

import dao.AnimalFacade;
import dao.CageFacade;
import dao.exceptions.NonexistentEntityException;
import entity.Animal;
import entity.Cage;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import util.UtilView;

public class EditAnimalFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    CageFacade cagF = new CageFacade(emf);
    AnimalFacade aniF = new AnimalFacade(emf);

    ObservableList<Cage> lstCage = FXCollections.observableList(cagF.findCageEntities());

    @FXML
    private TableView<Cage> tableCage = new TableView();
    @FXML
    private TableColumn<Cage, String> cageSector;
    @FXML
    private TableColumn<Cage, String> cageType;

    @FXML
    private TextField txtSpecies;
    @FXML
    private TextField txtSex;
    @FXML
    private TextArea areaDescription;
    @FXML
    private DatePicker dateBirth;
    @FXML
    private CheckBox checkBirth;
    @FXML
    private TextField txtSource;
    @FXML
    private DatePicker dateArrival;

    public void showCage() {
        cageSector.setCellValueFactory(new PropertyValueFactory("sector"));
        cageType.setCellValueFactory(new PropertyValueFactory("type"));
        tableCage.setItems(lstCage);
        tableCage.getSelectionModel().select(UtilView.getSellectedAnimal().getCage());
    }

    @FXML
    public void editAnimalInfo() {
        txtSpecies.setText(UtilView.getSellectedAnimal().getSpecies());
        txtSex.setText(UtilView.getSellectedAnimal().getSex());
        areaDescription.setText(UtilView.getSellectedAnimal().getDescription());
        LocalDate d = UtilView.getSellectedAnimal().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dateBirth.setValue(d);
        LocalDate d2 = UtilView.getSellectedAnimal().getArrivalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dateArrival.setValue(d2);
        txtSource.setText(UtilView.getSellectedAnimal().getSource());
        if (UtilView.getSellectedAnimal().getSource().equals("Urodzenie")) {
            checkBirth.setSelected(true);
            txtSource.setVisible(false);
        }
    }

    @FXML
    public void editAnimal(ActionEvent event) throws NonexistentEntityException, Exception {
        Animal a = UtilView.getSellectedAnimal();
        Cage c = tableCage.getSelectionModel().getSelectedItem();
        c.setCondition("Zajęta");
        cagF.edit(c);
        a.setCage(tableCage.getSelectionModel().getSelectedItem());
        a.setDescription(areaDescription.getText());
        if (aniF.editAnimal(a).equals("OK")) {
            Alerts.setAlerts("Operacja edycji zwierzęcia zakończona powodzeniem\n");
            Alerts.returnAlert("Edycja wykonana", null, AlertType.INFORMATION);
            Alerts.alertsClear();
            cancel(event);
        } else {
            Alerts.setAlerts("Operacja edycji zwierzęcia zakończona niepowodzeniem\n");
            Alerts.returnAlert("Błąd edycji", null, AlertType.ERROR);
            Alerts.alertsClear();
        }
    }

    @FXML
    public void clear(ActionEvent event) {
        areaDescription.setText(UtilView.getSellectedAnimal().getDescription());
        tableCage.getSelectionModel().select(UtilView.getSellectedAnimal().getCage());
    }

    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showCage();
        editAnimalInfo();
    }
}

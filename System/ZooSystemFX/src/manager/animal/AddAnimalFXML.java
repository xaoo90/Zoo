/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.animal;

import dao.AnimalFacade;
import dao.CageFacade;
import entity.Animal;
import entity.Cage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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

public class AddAnimalFXML implements Initializable {

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
        tableCage.getSelectionModel().selectFirst();
    }

    @FXML
    public void addAnimal(ActionEvent event) throws IOException, Exception {
        if (isValidate()) {
            Animal a = new Animal();
            Cage c = tableCage.getSelectionModel().getSelectedItem();
            c.setCondition("Zajęta");
            cagF.edit(c);
            a.setCage(c);
            a.setSpecies(txtSpecies.getText());
            a.setSex(txtSex.getText());
            a.setDescription(areaDescription.getText());
            Date d = new Date(Date.from(dateBirth.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            a.setBirthDate(d);
            if (checkBirth.isSelected()) {
                a.setSource("Urodzenie");
                a.setArrivalDate(d);
            } else {
                a.setSource(txtSource.getText());
                a.setArrivalDate(new Date(Date.from(dateArrival.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
            }
            if (aniF.addAnimal(a).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania zwierzęcia zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania zwierzęcia zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtSpecies.getText())) {
            Alerts.setAlerts("Pole Gatunek jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyLetter(txtSpecies.getText())) {
            Alerts.setAlerts("Gatunek zawiera niedozwolone znaki (tylko litery)\n");
            val = false;
        }
        if (!Alerts.isNull(txtSex.getText())) {
            if (Alerts.isOnlyLetter(txtSex.getText())) {
                Alerts.setAlerts("Płeć zawiera niedozwolone znaki (tylko litery)\n");
                val = false;
            }
        }
        if (Alerts.isNull(dateBirth.getValue())) {
            Alerts.setAlerts("Pole Data urodzenia jest wymagane\n");
            val = false;
        }
        if (!checkBirth.isSelected()) {
            if (Alerts.isNull(txtSource.getText())) {
                Alerts.setAlerts("Pole Pochodzenie jest wymagane\n");
                val = false;
            }
            if (Alerts.isNull(dateArrival.getValue())) {
                Alerts.setAlerts("Pole Data przybycia jest wymagane\n");
                val = false;
            }
            if (dateArrival.getValue() != null) {
                if (Alerts.isAfter(dateBirth.getValue(), dateArrival.getValue())) {
                    Alerts.setAlerts("Data przybycia zwierzęcia nie może być wcześniejsza niż data urodzenia\n");
                    val = false;
                }
            }
        }

        Alerts.returnAlert("Dodawanie zwierzęcia", "Błąd dodawania zwierzęcia", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    public void clear(ActionEvent event) {
        txtSpecies.setText(null);
        txtSex.setText(null);
        areaDescription.setText(null);
        dateBirth.setValue(null);
        dateArrival.setValue(null);
        txtSource.setText(null);
        checkBirth.setSelected(false);
    }

    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @FXML
    public void disable() {
        if (checkBirth.isSelected()) {
            dateArrival.setDisable(true);
            txtSource.setDisable(true);
        } else {
            dateArrival.setDisable(false);
            txtSource.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showCage();
    }

}

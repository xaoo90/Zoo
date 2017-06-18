/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package veterinarian.animal;

import dao.MedExaminationFacade;
import entity.MedExamination;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class EditMedAnimalFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    MedExaminationFacade medF = new MedExaminationFacade(emf);

    @FXML
    private Label lblAnimal;
    @FXML
    private ComboBox boxCondition;
    @FXML
    private TextArea txtDesc;
    @FXML
    private DatePicker dateMed;

    ArrayList<String> lstBox = new ArrayList();

    public void editMed(ActionEvent event) throws Exception {
        if (isValidate()) {
            MedExamination med = UtilView.getSellectedMed();
            med.setCondition((String) boxCondition.getValue());
            med.setDescription(txtDesc.getText());
            med.setMedDate(Date.valueOf(dateMed.getValue()));
            if (medF.editAnimalMed(med).equals("OK")) {
                Alerts.setAlerts("Operacja edycji zabiegu medycznego zakończona powodzeniem\n");
                Alerts.returnAlert("Edycja wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja edycji zabiegu medycznego zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd edycji", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(dateMed.getValue())) {
            Alerts.setAlerts("Pole Data badania jest wymagane\n");
            val = false;
        }
        if (Alerts.isNull(txtDesc.getText())) {
            Alerts.setAlerts("Pole Opis jest wymagane\n");
            val = false;
        }

        Alerts.returnAlert("Edycja zabiegu medycznego", "Błąd edycji zabiegu medycznego", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    public void showInfo() {
        lblAnimal.setText(UtilView.getSellectedMed().getAnimal().getSpecies()
                + " " + UtilView.getSellectedMed().getAnimal().getSex());

        txtDesc.setText(UtilView.getSellectedMed().getDescription());
        dateMed.setValue(UtilView.getSellectedMed().getMedDatee().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    @FXML
    private void clear(ActionEvent event) {
        txtDesc.setText(null);
        boxCondition.setValue(lstBox.get(0));
        dateMed.setValue(LocalDate.now());
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstBox.add("Wykonane");
        lstBox.add("Zaplanowane");
        boxCondition.setValue(lstBox.get(0));
        boxCondition.setItems(FXCollections.observableList(lstBox));
        showInfo();
    }

}

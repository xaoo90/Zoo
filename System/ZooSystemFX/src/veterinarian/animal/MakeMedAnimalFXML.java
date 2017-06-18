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
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
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
public class MakeMedAnimalFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    MedExaminationFacade medF = new MedExaminationFacade(emf);

    @FXML
    private Label lblAnimal;
    @FXML
    private TextArea txtDesc;
    @FXML
    private DatePicker dateMed;

    public void showInfo() {
        lblAnimal.setText(UtilView.getSellectedMed().getAnimal().getSpecies()
                + " " + UtilView.getSellectedMed().getAnimal().getSex());
        txtDesc.setText(UtilView.getSellectedMed().getDescription());
        dateMed.setValue(UtilView.getSellectedMed().getMedDatee().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void edit(ActionEvent event) throws Exception {
        if (isValidate()) {
            MedExamination med = UtilView.getSellectedMed();
            med.setCondition("Wykonane");
            med.setDescription(txtDesc.getText());
            med.setMedDate(Date.valueOf(dateMed.getValue()));
            medF.edit(med);
            cancel(event);

            if (medF.addAnimalMed(med).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania zabiegu medycznego zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonane", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania zabiegu medycznego zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
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

        Alerts.returnAlert("Dodawanie zabiegu medycznego", "Błąd dodawania zabiegu medycznego", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    private void clear(ActionEvent event) {
        txtDesc.setText(null);

    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showInfo();
    }

}

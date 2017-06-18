/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all;

import dao.HolidayFacade;
import entity.Holiday;
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
import javafx.scene.control.TextArea;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class AddHolidayAllFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    HolidayFacade holF = new HolidayFacade(emf);

    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private TextArea txtType;

    @FXML
    public void addHoliday(ActionEvent event) throws Exception {
        if (isValidate()) {
            Holiday h = new Holiday();
            h.setEmployee(UtilLogic.getSessionEmployee());
            Date startDate = new Date(Date.from(dateStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            h.setStartDate(startDate);
            Date endDate = new Date(Date.from(dateEnd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            h.setEndDate(endDate);
            h.setType("Pytanie - " + txtType.getText());

            if (holF.addHoliday(h).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania prośby o urlop zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania prośby o urlop zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        boolean start = Alerts.isNull(dateStart.getValue());
        boolean end = Alerts.isNull(dateEnd.getValue());
        if (start) {
            Alerts.setAlerts("Pole Data rozpoczęcia jest wymagane\n");
            val = false;
        }
        if (end) {
            Alerts.setAlerts("Pole Data zakończenia jest wymagane\n");
            val = false;
        }
        if (!(start && end)) {
            if (Alerts.isAfter(dateStart.getValue(), dateEnd.getValue())) {
                Alerts.setAlerts("Data zakończenia urlopu nie może być wcześniejsza niż rozpoczęcia\n");
                val = false;
            }
        }
        if (Alerts.isNull(txtType.getText())) {
            Alerts.setAlerts("Pole Uzasadnienie jest wymagane\n");
            val = false;
        }

        Alerts.returnAlert("Dodawanie prośby o urlop", "Błąd dodawania prośby o urlop", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    public void clear(ActionEvent event) {
        dateStart.setValue(null);
        dateEnd.setValue(null);
        txtType.setText(null);
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.ticket;

import dao.TicketTypeFacade;
import entity.TicketType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;

public class AddTicketTypeFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    TicketTypeFacade ttyF = new TicketTypeFacade(emf);

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;

    @FXML
    public void addTicketType(ActionEvent event) {
        if (isValidate()) {
            TicketType tt = new TicketType();
            tt.setName(txtName.getText());
            tt.setPrice(new BigDecimal(txtPrice.getText()));
            if (ttyF.addTicketType(tt).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania typu biletu zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania typu biletu zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtName.getText())) {
            Alerts.setAlerts("Pole Nazwa jest wymagane\n");
            val = false;
        }
        if (Alerts.isNull(txtPrice.getText())) {
            Alerts.setAlerts("Cena zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtPrice.getText().replace(",", "."))) {
            Alerts.setAlerts("Cena zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }

        Alerts.returnAlert("Dodawanie typu biletu", "Błąd dodawania typu biletu", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    public void clear(ActionEvent event) {
        txtName.setText(null);
        txtPrice.setText(null);
    }

    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}

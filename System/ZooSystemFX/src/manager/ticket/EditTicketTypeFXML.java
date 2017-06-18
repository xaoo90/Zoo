/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.ticket;

import dao.TicketTypeFacade;
import dao.exceptions.NonexistentEntityException;
import entity.TicketType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class EditTicketTypeFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    TicketTypeFacade ttyF = new TicketTypeFacade(emf);

    @FXML
    private Label lblName;
    @FXML
    private TextField txtPrice;

    @FXML
    public void showEditTypeInfo() {
        lblName.setText(UtilView.getSellectedTicketType().getName());
    }

    @FXML
    public void editTicketType(ActionEvent event) {
        if (isValidate()) {
            TicketType tt = UtilView.getSellectedTicketType();
            tt.setPrice(new BigDecimal(txtPrice.getText()));
            if (ttyF.editTicketType(tt).equals("OK")) {
                Alerts.setAlerts("Operacja edycji typu biletu zakończona powodzeniem\n");
                Alerts.returnAlert("Edycja wykonana", null, Alert.AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja edycji typu biletu zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd edycji", null, Alert.AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtPrice.getText())) {
            Alerts.setAlerts("Cena zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtPrice.getText().replace(",", "."))) {
            Alerts.setAlerts("Cena zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }

        Alerts.returnAlert("Edycja typu biletu", "Błąd edycji typu biletu", Alert.AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    public void clear(ActionEvent event) {
        txtPrice.setText(null);
    }

    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showEditTypeInfo();
    }

}

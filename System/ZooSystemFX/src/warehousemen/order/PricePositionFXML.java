/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warehousemen.order;

import dao.OrderPositionFacade;
import entity.OrderPosition;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
public class PricePositionFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    OrderPositionFacade posF = new OrderPositionFacade(emf);

    @FXML
    private Label lblOrder;
    @FXML
    private Label lblPosition;
    @FXML
    private TextField txtPrice;

    public void showInfo() {
        lblOrder.setText("Numer zamówienia: " + UtilView.getSellectedOrderFeed().getIdOrder());
        lblPosition.setText(UtilView.getSellectedPosition().getFeed().getName());
    }

    public void addPrice(ActionEvent event) throws Exception {
        if (isValidate()) {
            OrderPosition op = UtilView.getSellectedPosition();
            op.setPrice(new BigDecimal(txtPrice.getText().replace(",", ".")));
            if (posF.editOrderPosition(op).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania ceny produktu zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonane", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania ceny produktu zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtPrice.getText())) {
            Alerts.setAlerts("Pole Cena produktu jest wymagane\n");
            val = false;
        }

        Alerts.returnAlert("Dodawanie ceny produktu", "Błąd dodawania ceny produktu", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
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

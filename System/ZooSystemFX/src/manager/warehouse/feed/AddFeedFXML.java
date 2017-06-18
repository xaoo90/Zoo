/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.warehouse.feed;

import dao.FeedFacade;
import entity.Feed;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
public class AddFeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    FeedFacade feeF = new FeedFacade(emf);

    @FXML
    private TextField txtName;
    @FXML
    private Label lblWarehouse;
    @FXML
    private TextField txtAvailability;
    @FXML
    private TextField txtUnit;

    @FXML
    public void addFeed(ActionEvent event) throws Exception {
        if (isValidate()) {
            Feed f = new Feed();
            f.setName(txtName.getText());
            if (txtAvailability.getText() != null) {
                f.setAvailability(new BigDecimal(txtAvailability.getText().replace(",", ".")));
            } else {
                f.setAvailability(new BigDecimal(0));
            }
            f.setUnit(txtUnit.getText());
            f.setWarehouse(UtilView.getSellectedWarehouse());

            if (feeF.addFeed(f).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania pożywienia zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonane", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania pożywienia zakończona niepowodzeniem\n");
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

        Alerts.returnAlert("Dodawanie pożywienia", "Błąd dodawania pożywienia", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    private void clear(ActionEvent event) {
        txtName.setText(null);
        txtAvailability.setText(null);
        txtUnit.setText(null);
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblWarehouse.setText(UtilView.getSellectedWarehouse().getName());
    }

}

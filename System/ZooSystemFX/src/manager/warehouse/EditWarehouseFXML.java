/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.warehouse;

import dao.WarehouseFacade;
import entity.Warehouse;
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

public class EditWarehouseFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    WarehouseFacade warF = new WarehouseFacade(emf);

    @FXML
    private Label lblId;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtName;

    @FXML
    public void editWarehouse(ActionEvent event) throws Exception {
        if (isValidate()) {
            Warehouse w = UtilView.getSellectedWarehouse();
            w.setName(txtName.getText());
            if (warF.editWarehouse(w).equals("OK")) {
                Alerts.setAlerts("Operacja edycji magazynu zakończona powodzeniem\n");
                Alerts.returnAlert("Edycja wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja edycji magazynu zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd edycji", null, AlertType.ERROR);
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

        Alerts.returnAlert("Dodawanie magazynu", "Błąd dodawania magazynu", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    private void clear(ActionEvent event) {
        txtName.setText(null);
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblId.setText(String.valueOf(UtilView.getSellectedWarehouse().getIdWarehouse()));
        lblName.setText(UtilView.getSellectedWarehouse().getName());
    }

}

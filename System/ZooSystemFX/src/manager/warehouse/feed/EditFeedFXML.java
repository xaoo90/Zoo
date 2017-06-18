/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.warehouse.feed;

import dao.FeedFacade;
import dao.WarehouseFacade;
import entity.Feed;
import entity.Warehouse;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
public class EditFeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    FeedFacade feeF = new FeedFacade(emf);
    WarehouseFacade warF = new WarehouseFacade(emf);

    ObservableList<Warehouse> lstWarehouse = FXCollections.observableList(warF.findWarehouseEntities());

    @FXML
    private TextField txtName;
    @FXML
    private ComboBox comboWarehouse;
    @FXML
    private TextField txtAvailability;
    @FXML
    private TextField txtUnit;

    @FXML
    public void editFeed(ActionEvent event) throws Exception {
        if (isValidate()) {
            Feed f = UtilView.getSellectedFeed();
            f.setName(txtName.getText());
            if (txtAvailability.getText() != null) {
                f.setAvailability(new BigDecimal(txtAvailability.getText().replace(",", ".")));
            } else {
                f.setAvailability(new BigDecimal(0));
            }
            f.setUnit(txtUnit.getText());
            f.setWarehouse(UtilView.getSellectedWarehouse());

            if (feeF.editFeed(f).equals("OK")) {
                Alerts.setAlerts("Operacja edycji pożywienia zakończona powodzeniem\n");
                Alerts.returnAlert("Edycja wykonana", null, Alert.AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja edycji pożywienia zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd edycji", null, Alert.AlertType.ERROR);
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

        Alerts.returnAlert("Edycja pożywienia", "Błąd edycji pożywienia", Alert.AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    private void clear(ActionEvent event) {
        txtName.setText(null);
        txtAvailability.setText(null);
        txtUnit.setText(null);
        comboWarehouse.setValue(UtilView.getSellectedWarehouse());
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboWarehouse.setItems(lstWarehouse);
        comboWarehouse.setValue(UtilView.getSellectedWarehouse());
        txtAvailability.setText(String.valueOf(UtilView.getSellectedFeed().getAvailability()));
        txtName.setText(UtilView.getSellectedFeed().getName());
        txtUnit.setText(UtilView.getSellectedFeed().getUnit());
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keeper.animalFeed;

import dao.FeedFacade;
import dao.exceptions.NonexistentEntityException;
import entity.AnimalFeed;
import entity.Feed;
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
public class FeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    FeedFacade feeF = new FeedFacade(emf);

    @FXML
    private Label lblAnimal;
    @FXML
    private Label lblFeed;
    @FXML
    private TextField txtPortion;
    @FXML
    private Label lblUnit;

    public void showAnimal() {
        AnimalFeed af = UtilView.getSellectedAnimalFeed();
        lblAnimal.setText(af.getAnimal().getSpecies() + "   " + af.getAnimal().getSex());
        lblFeed.setText(af.getFeed().getName() + "  " + af.getFeed().getAvailability());
        txtPortion.setText(String.valueOf(af.getPortionn()));
        lblUnit.setText(af.getFeed().getUnit());
    }

    @FXML
    public void feedAnimal(ActionEvent event) throws NonexistentEntityException, Exception {
        if (isValidate()) {
        Feed f = UtilView.getSellectedAnimalFeed().getFeed();
        BigDecimal bd = f.getAvailability();
        f.setAvailability(bd.subtract(new BigDecimal(txtPortion.getText().replace(",", "."))));
        
        if (feeF.editFeed(f).equals("OK")) {
                Alerts.setAlerts("Operacja karmienia zakończona powodzeniem\n");
                Alerts.returnAlert("Karmienie wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja karmienia zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd karmienia", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtPortion.getText())) {
            Alerts.setAlerts("Pole Porcja jest wymagane\n");
            val = false;
        }

        Alerts.returnAlert("Karmienie zwierząt", "Błąd karmienia zwierząt", Alert.AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showAnimal();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.cage;

import dao.CageFacade;
import dao.SectorFacade;
import dao.exceptions.NonexistentEntityException;
import entity.Cage;
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
public class EditCageFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    SectorFacade secF = new SectorFacade(emf);
    CageFacade cagF = new CageFacade(emf);

    @FXML
    private TextField txtSpace;
    @FXML
    private Label lblOldSpace;
    
    @FXML
    public void editCage(ActionEvent event) throws NonexistentEntityException, Exception{
        if (isValidate()) {
        Cage c = UtilView.getSellectedCage();
        c.setSpace(Integer.parseInt(txtSpace.getText()));
        if(cagF.editCage(c).equals("OK")){
                Alerts.setAlerts("Operacja edycji klatki zakończona powodzeniem\n");
                Alerts.returnAlert("Edycjia wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja edycji klatki zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd edycji", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }
    
    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtSpace.getText())) {
            Alerts.setAlerts("Pole Liczba miejsc jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtSpace.getText())){
            Alerts.setAlerts("Liczba miejsc zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }

        Alerts.returnAlert("Edycjia klatki", "Błąd edycji klatki", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }
    
    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblOldSpace.setText(String.valueOf(UtilView.getSellectedCage().getSpace()));
    }    
    
}

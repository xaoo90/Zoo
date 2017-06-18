/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.cage;

import dao.CageFacade;
import dao.SectorFacade;
import entity.Cage;
import entity.Sector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;

public class AddCageFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    SectorFacade secF = new SectorFacade(emf);
    CageFacade cagF = new CageFacade(emf);

    ObservableList<Sector> lstSector = FXCollections.observableList(secF.findSectorEntities());

    ObservableList<String> lstCageType = FXCollections.observableList(cagF.getCageType());

    @FXML
    private ListView listSector;
    @FXML
    private ListView listCageType;
    @FXML
    private TextField txtSpace;
    
    @FXML
    public void addCage(ActionEvent event){
        if (isValidate()) {
        Cage c = new Cage();
        c.setSector((Sector) listSector.getSelectionModel().getSelectedItem());
        c.setType((String) listCageType.getSelectionModel().getSelectedItem());
        c.setSpace(Integer.parseInt(txtSpace.getText()));        
        if(cagF.addCage(c).equals("OK")){
                Alerts.setAlerts("Operacja dodawania klatki zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania klatki zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }
    
    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(listSector.getSelectionModel().getSelectedItem())) {
            Alerts.setAlerts("Brak wybranego sektora\n");
            val = false;
        }
        if (Alerts.isNull(listCageType.getSelectionModel().getSelectedItem())) {
            Alerts.setAlerts("Brak wybranego typu klatki\n");
            val = false;
        }
        if (Alerts.isNull(txtSpace.getText())) {
            Alerts.setAlerts("Pole Liczba miejsc jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtSpace.getText())){
            Alerts.setAlerts("Liczba miejsc zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }

        Alerts.returnAlert("Dodawanie klatki", "Błąd dodawania nowej klatki", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    
    @FXML
    public void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listSector.setItems(lstSector);
        listCageType.setItems(lstCageType);
    }

}

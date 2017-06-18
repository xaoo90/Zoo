/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.sector;

import dao.EmployeeFacade;
import dao.SectorFacade;
import dao.exceptions.NonexistentEntityException;
import entity.Employee;
import entity.Sector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
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
public class EditSectorFXML implements Initializable {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    SectorFacade secF = new SectorFacade(emf);
    EmployeeFacade empF = new EmployeeFacade(emf);
    
    ObservableList<Employee> position = FXCollections.observableList(empF.findEmployeePosition("Kierownik"));
    ObservableList<Sector> cage = FXCollections.observableList(secF.getNullSector());

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtType;
        
    @FXML
    private ChoiceBox choicePosition;
    
    public void editSectorInfo(){
        txtName.setText(UtilView.getSellectedSector().getName());
        txtType.setText(UtilView.getSellectedSector().getType());
        choicePosition.setValue(UtilView.getSellectedSector().getManager());
    }
    
    @FXML
    public void editSector(ActionEvent event) throws NonexistentEntityException, Exception{
        if (isValidate()) {
        Sector s = UtilView.getSellectedSector();
        s.setName(txtName.getText());
        s.setType(txtType.getText());
        s.setManager((Employee) choicePosition.getValue());
        if(secF.editSector(s).equals("OK")){
                Alerts.setAlerts("Operacja edycji sektora zakończona powodzeniem\n");
                Alerts.returnAlert("Edycjia wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja edycji sektora zakończona niepowodzeniem\n");
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
        if (Alerts.isNull(txtType.getText())) {
            Alerts.setAlerts("Pole Typ jest wymagane\n");
            val = false;
        }

        Alerts.returnAlert("Edycja sektora", "Błąd edycji sektora", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }
    
    @FXML
    public void clear(ActionEvent event){
        txtName.setText(null);
        txtType.setText(null);
        choicePosition.setValue(null);
    }
    
    @FXML
    private void cancel(ActionEvent event){
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        position.add(null);
        choicePosition.setItems(position);
        editSectorInfo();
    }    
    
}

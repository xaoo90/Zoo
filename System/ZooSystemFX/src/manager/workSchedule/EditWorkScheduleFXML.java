/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.workSchedule;

import dao.EmployeeFacade;
import dao.EmployeeScheduleFacade;
import entity.Employee;
import entity.EmployeeSchedule;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import utilLogic.UtilLogic;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class EditWorkScheduleFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeFacade empF = new EmployeeFacade(emf);
    EmployeeScheduleFacade escF = new EmployeeScheduleFacade(emf);

    @FXML
    private Label lblEmpl;
    @FXML
    private Label lblPosition;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblWorkShifts;

    @FXML
    private ComboBox<Employee> boxEmployee;

    ObservableList<Employee> lstEmpl = FXCollections.observableArrayList();

    public void comboItems() {
        String pos = UtilView.getSellectedEmployeeSchedule().getPosition().substring(0, 1);
        System.out.println(pos);
        if (pos.equals("S") || pos.equals("P") || pos.equals("G") || pos.equals("R") || pos.equals("B")) {
            System.out.println("if");
            lstEmpl.addAll(empF.findEmployeePosition("Opiekun"));
        } else {
            System.out.println("else");
            lstEmpl.addAll(empF.findEmployeePosition(UtilView.getSellectedEmployeeSchedule().getPosition()));
        }
    }

    public void showInfo() {
        EmployeeSchedule es = UtilView.getSellectedEmployeeSchedule();
        lblEmpl.setText(es.getEmployee().getFirstName() + " " + es.getEmployee().getLastName());
        lblPosition.setText(es.getPosition());
        lblDate.setText(UtilLogic.dateFormat(es.getWorkSchedule().getWorkDate()));
        lblWorkShifts.setText(es.getWorkSchedule().getWorkShifts());
    }

    public void editWorkSchedule(ActionEvent event) throws Exception {
        EmployeeSchedule es = UtilView.getSellectedEmployeeSchedule();
        es.setEmployee(boxEmployee.getValue());
        if (escF.editEmpSchedule(es).equals("OK")) {
            Alerts.setAlerts("Operacja edycji zakończona powodzeniem\n");
            Alerts.returnAlert("Edycjia wykonana", null, AlertType.INFORMATION);
            Alerts.alertsClear();
            cancel(event);
        } else {
            Alerts.setAlerts("Operacja edycji zakończona niepowodzeniem\n");
            Alerts.returnAlert("Błąd edycji", null, AlertType.ERROR);
            Alerts.alertsClear();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showInfo();
        comboItems();
        boxEmployee.setItems(lstEmpl);
        boxEmployee.setValue(lstEmpl.get(0));
    }

}

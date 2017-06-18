/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.holiday;

import dao.EmployeeFacade;
import dao.HolidayFacade;
import entity.Employee;
import entity.Holiday;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;

public class AddHolidayFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    HolidayFacade holF = new HolidayFacade(emf);
    EmployeeFacade empF = new EmployeeFacade(emf);

    ObservableList<Employee> lstEmployee = FXCollections.observableList(empF.findEmployeeEntities());

    @FXML
    private TableView<Employee> tableEmployee;
    @FXML
    private TableColumn<Employee, String> columnFname;
    @FXML
    private TableColumn<Employee, String> columnLname;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtFilter;

    public void showEmployee() {
        columnFname.setCellValueFactory(new PropertyValueFactory("firstName"));
        columnLname.setCellValueFactory(new PropertyValueFactory("lastName"));
        tableEmployee.setItems(lstEmployee);
        tableEmployee.getSelectionModel().selectFirst();
    }

    @FXML
    public void addHoliday(ActionEvent event) throws Exception {
        if (isValidate()) {
            Holiday h = new Holiday();
            h.setEmployee((Employee) tableEmployee.getSelectionModel().getSelectedItem());
            Date startDate = new Date(Date.from(dateStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            h.setStartDate(startDate);
            Date endDate = new Date(Date.from(dateEnd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            h.setEndDate(endDate);
            h.setType(txtType.getText());

            if (holF.addHoliday(h).equals("OK")) {
                Alerts.setAlerts("Operacja dodawania urlopu zakończona powodzeniem\n");
                Alerts.returnAlert("Dodawanie wykonane", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja dodawania urlopu zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }

    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(dateStart.getValue())) {
            Alerts.setAlerts("Pole Data rozpoczęcia jest wymagane\n");
            val = false;
        }
        if (Alerts.isNull(dateEnd.getValue())) {
            Alerts.setAlerts("Pole Data zakończenia jest wymagane\n");
            val = false;
        } else if (Alerts.isAfter(dateStart.getValue(), dateEnd.getValue())) {
            Alerts.setAlerts("Data zakończenia urlopu nie może być wcześniejsza niż rozpoczęcia\n");
            val = false;
        }
        if (Alerts.isNull(txtType.getText())) {
            Alerts.setAlerts("Pole Uzasadnienie jest obowiazkowe\n");
            val = false;
        }

        Alerts.returnAlert("Dodawanie urlopu", "Błąd dodawania nowego urlopu", Alert.AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    public void clear(ActionEvent event) {
        dateStart.setValue(null);
        dateEnd.setValue(null);
        txtType.setText(null);
        txtFilter.setText(null);
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    public void filterEmployee() {
        FilteredList<Employee> lstEmployeeFilter = new FilteredList<Employee>(lstEmployee, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            lstEmployeeFilter.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();
                if (employee.getFirstName().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (employee.getLastName().toLowerCase().contains(lowerCase)) {
                    return true;
                }
                return false;
            });
        }
        );
        SortedList<Employee> lstEmployeeSort = new SortedList<Employee>(lstEmployeeFilter);
        lstEmployeeSort.comparatorProperty().bind(tableEmployee.comparatorProperty());
        tableEmployee.setItems(lstEmployeeSort);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableEmployee.setPlaceholder(new Label("Brak pracowników"));
        showEmployee();
        filterEmployee();
    }
}

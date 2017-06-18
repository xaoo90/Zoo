/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.employee;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import dao.EmployeeFacade;
import entity.Employee;
import java.io.IOException;
import java.math.BigDecimal;
import javafx.event.ActionEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.ZoneId;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import util.Alerts;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class RegisterEmplFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeFacade empF = new EmployeeFacade(emf);

    ObservableList<String> position = FXCollections.observableList(empF.getPosition());
    ObservableList<String> condition = FXCollections.observableArrayList("Aktywne", "Nieaktywne");

    @FXML
    private TextField txtLogin;
    @FXML
    private TextField txtFname;
    @FXML
    private TextField txtLname;
    @FXML
    private ChoiceBox choicePosition;
    @FXML
    private DatePicker empDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField txtSalary;
    @FXML
    private TextField txtBAN;
    @FXML
    private TextField txtMail;
    @FXML
    private ChoiceBox choiceCondition;

    @FXML
    public void addEmployee(ActionEvent event) throws IOException {
        if (isValidate()) {
            Employee e = new Employee();
            e.setLogin(txtLogin.getText());
            e.setFirstName(txtFname.getText().substring(0, 1).toUpperCase() + txtFname.getText().substring(1));
            e.setLastName(txtLname.getText().substring(0, 1).toUpperCase() + txtLname.getText().substring(1));
            e.setPosition(String.valueOf(choicePosition.getValue()));
            Date employmentDate = new Date(Date.from(empDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            e.setEmploymentDate(employmentDate);
            if (endDate.getValue() == null) {
                e.setEndingDate(null);
            } else {
                Date endingDate = new Date(Date.from(endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                e.setEndingDate(endingDate);
            }
            e.setSalary(new BigDecimal(txtSalary.getText()));
            e.setBankAccountNumber(txtBAN.getText());
            e.setEmail(txtMail.getText());
            e.setCondition(String.valueOf(choiceCondition.getValue()));

            if (empF.registerEmployee(e).equals("OK")) {
                Alerts.setAlerts("Operacja rejestracji użytkownika zakończona powodzeniem\n");
                Alerts.returnAlert("Rejestracja wykonana", null, AlertType.INFORMATION);
                Alerts.alertsClear();
                cancel(event);
            } else {
                Alerts.setAlerts("Operacja rejestracji użytkownika zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd rejestracji", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtLogin.getText())) {
            Alerts.setAlerts("Pole Login jest wymagane\n");
            val = false;
        } else if (Alerts.isShorter(txtLogin.getText(), 4)) {
            Alerts.setAlerts("Login zbyt któtki (min 4 znaki)\n");
            val = false;
        }
        if (Alerts.isNull(txtFname.getText())) {
            Alerts.setAlerts("Pole Imię jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyLetter(txtFname.getText())) {
            Alerts.setAlerts("Imię zawiera niedozwolone znaki (tylko litery)\n");
            val = false;
        }
        if (Alerts.isNull(txtLname.getText())) {
            Alerts.setAlerts("Pole Nazwisko jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyLetter(txtLname.getText())) {
            Alerts.setAlerts("Nazwisko zawiera niedozwolone znaki (tylko litery)\n");
            val = false;
        }
        if (Alerts.isNull(empDate.getValue())) {
            Alerts.setAlerts("Pole Data zatrudnienia jest wymagane\n");
            val = false;
        }
        if (endDate.getValue() != null) {
            if (Alerts.isAfter(empDate.getValue(), endDate.getValue())) {
                Alerts.setAlerts("Data zakończenia umowy nie może być wcześniejsza niż zatrudnienia\n");
                val = false;
            }
        }
        if (Alerts.isNull(txtSalary.getText())) {
            Alerts.setAlerts("Pole Wynagrodzenie jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtSalary.getText())) {
            Alerts.setAlerts("Wynagrodzenie zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }
        if (Alerts.isNull(txtBAN.getText())) {
            Alerts.setAlerts("Pole NRB jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtBAN.getText())) {
            Alerts.setAlerts("NRB zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }
        if (Alerts.isNull(txtMail.getText())) {
            Alerts.setAlerts("Pole e-mail jest wymagane\n");
            val = false;
        }

        Alerts.returnAlert("Rejestracja uzytkownika", "Błąd rejestracji nowego użytkownika", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    private void clear() {
        txtLogin.setText(null);
        txtFname.setText(null);
        txtLname.setText(null);
        empDate.setValue(null);
        endDate.setValue(null);
        txtSalary.setText(null);
        txtBAN.setText(null);
        txtMail.setText(null);
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choicePosition.setValue(position.get(0));
        choicePosition.setItems(position);
        choiceCondition.setValue(condition.get(0));
        choiceCondition.setItems(condition);
    }

}

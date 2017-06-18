/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all;

import dao.EmployeeFacade;
import dao.exceptions.NonexistentEntityException;
import entity.Employee;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class ChangePasswordFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeFacade empF = new EmployeeFacade(emf);

    @FXML
    private ImageView img;
    @FXML
    private PasswordField passOld;
    @FXML
    private PasswordField passNew;
    @FXML
    private PasswordField passRepeat;

    public void changePass(ActionEvent event) throws NonexistentEntityException, Exception {
        if (isValidate()) {
            if (passOld.getText().equals(UtilLogic.getSessionEmployee().getPassword())) {
                if (passNew.getText().equals(passRepeat.getText())) {
                    Employee e = UtilLogic.getSessionEmployee();
                    e.setPassword(passNew.getText());
                    if (empF.editEmpl(e).equals("OK")) {
                        Alerts.setAlerts("Operacja zmiany hasła zakończona powodzeniem\n");
                        Alerts.returnAlert("Zmiana wykonana", null, AlertType.INFORMATION);
                        Alerts.alertsClear();
                        cancel(event);
                    } else {
                        Alerts.setAlerts("Operacja zmiany hasła zakończona niepowodzeniem\n");
                        Alerts.returnAlert("Błąd zmiany hasła", null, AlertType.ERROR);
                        Alerts.alertsClear();
                    }
                }
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        boolean newP = Alerts.isNull(passNew.getText());
        boolean repP = Alerts.isNull(passRepeat.getText());

        if (Alerts.isNull(passOld.getText())) {
            Alerts.setAlerts("Pole Aktualnie hasło jest wymagane\n");
            val = false;
        } else if (!passOld.getText().equals(UtilLogic.getSessionEmployee().getPassword())) {
            Alerts.setAlerts("Aktualne hasło jest nieprawidłowe\n");
            val = false;
        }
        if (newP) {
            Alerts.setAlerts("Pole Nowe hasło jest wymagane\n");
            val = false;
        } else if (Alerts.isShorter(passNew.getText(), 8)) {
            Alerts.setAlerts("Hasło zbyt krótkie (min 8 znaków)\n");
            val = false;
        }
        if (repP) {
            Alerts.setAlerts("Pole Powtórz hasło jest wymagane\n");
            val = false;
        }
        if (!(newP || repP)) {
            if (!passNew.getText().equals(passRepeat.getText())) {
                Alerts.setAlerts("Nowe hasło niezgodne z powtórzonym\n");
                val = false;
            }
        }

        Alerts.returnAlert("Zmiana hasła", "Błąd zmiany hasła", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }

}

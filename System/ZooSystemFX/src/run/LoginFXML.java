/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import dao.EmployeeFacade;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class LoginFXML implements Initializable {

    @FXML
    private TextField textLogin;
    @FXML
    private PasswordField passPass;

    private Parent mainLoyout;

    private Stage primaryStage = new Stage();

    @FXML
    public void login(ActionEvent event) throws IOException, Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
        EmployeeFacade emp = new EmployeeFacade(emf);

        String login = textLogin.getText();
        String pass = passPass.getText();

        emp.loginEmployee(login, pass);

        if (UtilLogic.getSessionEmployee() != null) {
            switch (UtilLogic.getSessionEmployee().getPosition()) {
                case "Kierownik": {
                    (((Node) event.getSource()).getScene()).getWindow().hide();
                    showEmployeeItems("/manager/ManagerFXML.fxml", "Panel zarządzania");
                    break;
                }
                case "Opiekun": {
                    (((Node) event.getSource()).getScene()).getWindow().hide();
                    showEmployeeItems("/keeper/KeeperFXML.fxml", "Panel opiekuna");
                    break;
                }
                case "Kasjer": {
                    (((Node) event.getSource()).getScene()).getWindow().hide();
                    showEmployeeItems("/seller/SellerFXML.fxml", "Panel kasjera");
                    break;
                }
                case "Magazynier": {
                    (((Node) event.getSource()).getScene()).getWindow().hide();
                    showEmployeeItems("/warehousemen/WarehousemenFXML.fxml", "Panel magazyniera");
                    break;
                }
                case "Weterynarz": {
                    (((Node) event.getSource()).getScene()).getWindow().hide();
                    showEmployeeItems("/veterinarian/VeterinarianFXML.fxml", "Panel lekarza weterynarii");
                    break;
                }
            }
        } else {
            clear(event);
            Alerts.setAlerts("Nieprawidłowy login lub hasło");
            Alerts.returnAlert("Błąd logowania", null, AlertType.ERROR);
            Alerts.alertsClear();
        }
    }

    public void showEmployeeItems(String path, String description) throws IOException {
        mainLoyout = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(mainLoyout);
        primaryStage.setScene(scene);
        primaryStage.setTitle(UtilLogic.getSessionEmployee().getFirstName() + " " + UtilLogic.getSessionEmployee().getLastName() + " - " + description);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    public void clear(ActionEvent event) {
        textLogin.setText(null);
        passPass.setText(null);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}

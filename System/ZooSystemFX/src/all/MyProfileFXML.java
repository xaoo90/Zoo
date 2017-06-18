/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilLogic.UtilLogic;


/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class MyProfileFXML implements Initializable {
    
    @FXML
    private Label txtLogin;
    @FXML
    private Label txtFname;        
    @FXML
    private Label txtLname;
    @FXML
    private Label txtBan;
    @FXML
    private Label txtHoliday;
    @FXML
    private Label txtMail;
    
    public void showProfileInfo(){
        txtLogin.setText(UtilLogic.getSessionEmployee().getLogin());
        txtFname.setText(UtilLogic.getSessionEmployee().getFirstName());
        txtLname.setText(UtilLogic.getSessionEmployee().getLastName());
        txtBan.setText(UtilLogic.getSessionEmployee().getBankAccountNumber());
        txtHoliday.setText(String.valueOf(UtilLogic.getSessionEmployee().getHoliday()));
        txtMail.setText(UtilLogic.getSessionEmployee().getEmail());
    }
    
    @FXML
    public void changePass() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/all/ChangePasswordFXML.fxml"));
        BorderPane border = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Zmiana hasła");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.show();
    }
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showProfileInfo();
    }    
    
}

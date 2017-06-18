/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

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
import javafx.stage.Stage;

/**
 *
 * @author Xaoo
 */
public class MainWindowFXML implements Initializable {
    
    @FXML
    public void loginWindow(ActionEvent event) throws IOException {
        (((Node) event.getSource()).getScene()).getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/run/LoginFXML.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Logowanie");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    public void showTodaySchedule(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/run/TodayScheduleFXML.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Grafik na dziś");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}

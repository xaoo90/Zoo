/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warehousemen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class WarehousemenFXML implements Initializable {

    @FXML
    AnchorPane ap;

    
    @FXML
    public void showAllHoliday() throws IOException {
        ap.getChildren().clear();
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/all/HolidayAllFXML.fxml")));
    }
    
    @FXML
    public void showOrder() throws IOException {
        ap.getChildren().clear();
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/warehousemen/order/OrderFeedFXML.fxml")));
    }
    
    @FXML
    public void showNewOrder() throws IOException {
        ap.getChildren().clear();
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/warehousemen/feed/FeedFXML.fxml")));
    }
    
    @FXML
    public void showMySchedule() throws IOException {
        ap.getChildren().clear();
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/all/MyWorkScheduleFXML.fxml")));
    }
    
    @FXML
    public void showTodaySchedule() throws IOException {
        ap.getChildren().clear();
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/run/TodayScheduleFXML.fxml")));
    }
    
    @FXML
    public void showMyProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/all/MyProfileFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Moje konto");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void logout(ActionEvent event) throws IOException {
        UtilView.logout(event);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showOrder();
        } catch (IOException ex) {
            Logger.getLogger(WarehousemenFXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class ManagerFXML implements Initializable {

    @FXML
    AnchorPane ap;

    @FXML
    public void showEmployee() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/employee/EmployeeFXML.fxml")));
    }
    
    @FXML
    public void showWorkSchedule() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/workSchedule/WorkScheduleFXML.fxml")));
    }

    @FXML
    public void showSector() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/sector/SectorFXML.fxml")));
    }
    
    @FXML
    public void showHoliday() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/holiday/HolidayFXML.fxml")));
    }
    
    @FXML
    public void showCage() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/cage/CageFXML.fxml")));
    }
    
    @FXML
    public void showWarehouse() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/warehouse/WarehouseFXML.fxml")));
    }
    
    @FXML
    public void showAnimal() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/animal/AnimalFXML.fxml")));
    }
    
    @FXML
    public void showOrderFeed() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/orderFeed/OrderFeedFXML.fxml")));
    }
    
    @FXML
    public void showTicket() throws IOException {
        ap.getChildren().clear();
        UtilView.setSellectedPlanningDate(null);
        ap.getChildren().add(FXMLLoader.load(getClass().getResource("/manager/ticket/TicketFXML.fxml")));
    }
    
    @FXML
    public void logout(ActionEvent event) throws IOException {
        UtilView.logout(event);
        UtilView.setSellectedPlanningDate(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showEmployee();
        } catch (IOException ex) {
            Logger.getLogger(ManagerFXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

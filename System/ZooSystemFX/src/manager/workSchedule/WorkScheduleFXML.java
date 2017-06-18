/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.workSchedule;

import dao.EmployeeScheduleFacade;
import dao.WorkScheduleFacade;
import entity.EmployeeSchedule;
import entity.WorkSchedule;
import entity.view.WorkScheduleView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class WorkScheduleFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeScheduleFacade escF = new EmployeeScheduleFacade(emf);
    WorkScheduleFacade workF = new WorkScheduleFacade(emf);

    WorkScheduleView wsv = new WorkScheduleView();

    @FXML
    private TableView<WorkScheduleView> tablEmployeeSchedule = new TableView();
    @FXML
    private TableColumn<WorkScheduleView, String> esEmployee;
    @FXML
    private TableColumn<WorkScheduleView, String> esPosition;
    @FXML
    private TableColumn<WorkScheduleView, String> esDate;
    @FXML
    private TableColumn<WorkScheduleView, String> esShifts;

    @FXML
    private DatePicker pickerWorkDateHistory;

    @FXML
    private DatePicker pickerPlanning;
    @FXML
    private ComboBox comboPosition;

    @FXML
    private BorderPane bp;

    @FXML
    private VBox boxWeek;
    @FXML
    private VBox boxPlanning;

    @FXML
    private Button btnEdit;

    @FXML
    public void showPositionSchedule() throws IOException {
        boxWeek.getChildren().add(FXMLLoader.load(getClass().getResource("/run/PositionScheduleFXML.fxml")));
    }

    @FXML
    public void showTodaySchedule() throws IOException {
        bp.getChildren().add(FXMLLoader.load(getClass().getResource("/run/TodayScheduleFXML.fxml")));
    }

    public void showWeekSchedule() throws IOException {
        boxWeek.getChildren().clear();
        for (int i = 1; i <= 10; i++) {
            UtilView.setPosition(i);
            showPositionSchedule();
        }
    }
    
    @FXML
    public void showPlanning() throws IOException {
        boxPlanning.getChildren().add(FXMLLoader.load(getClass().getResource(
                "/manager/workSchedule/PlanningScheduleFXML.fxml")));
    }
    
    @FXML
    public void showPlanningSchedule() throws IOException {
        if (pickerPlanning.getValue() == null) {
            Alerts.setAlerts("Należy wybrać datę\n");
            Alerts.returnAlert("Brak daty", 
                    "Brak daty początkowej dla tygodnia planowania grafiku", 
                    AlertType.INFORMATION);
            Alerts.alertsClear();
        } else {
            boxPlanning.getChildren().clear();
            if (comboPosition.getValue() != null) {
                if (comboPosition.getValue().equals("Opiekun")) {
                    for (int i = 1; i <= 7; i++) {
                        UtilView.setPosition(i);
                        showPlanning();
                    }
                }
                if (comboPosition.getValue().equals("Weterynarz")) {
                    UtilView.setPosition(8);
                    showPlanning();
                }
                if (comboPosition.getValue().equals("Magazynier")) {
                    UtilView.setPosition(9);
                    showPlanning();
                }
                if (comboPosition.getValue().equals("Kasjer")) {
                    UtilView.setPosition(10);
                    showPlanning();
                }
            }
        }
    }

    @FXML
    public void setDate() {
        UtilView.setSellectedPlanningDate(pickerPlanning.getValue());
        comboPosition.setValue(null);
    }

    @FXML
    public void showHistorySchedule() {
        ObservableList<WorkScheduleView> lstWorkSchedule = FXCollections.observableArrayList(wsv.workSchedule(escF.findEmployeeScheduleEntities(), pickerWorkDateHistory.getValue()));
        esEmployee.setCellValueFactory(new PropertyValueFactory("employee"));
        esPosition.setCellValueFactory(new PropertyValueFactory("position"));
        esDate.setCellValueFactory(new PropertyValueFactory("workDate"));
        esShifts.setCellValueFactory(new PropertyValueFactory("workShifts"));
        tablEmployeeSchedule.setItems(lstWorkSchedule);

        tablEmployeeSchedule.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tablEmployeeSchedule.getSelectionModel().getSelectedIndex() >= 0) {
                        btnDisable(tablEmployeeSchedule.getSelectionModel().getSelectedItem().getWorkDatee());
                    }
                }
            }
        });
    }

    @FXML
    public void clearDatePicker() {
        pickerWorkDateHistory.setValue(null);
    }

    @FXML
    public void addSchedule() throws Exception {
        for (WorkScheduleView w : UtilView.getNewSchedule()) {
            WorkSchedule workSchedule = workF.getWorkSchedule(new WorkSchedule(null, w.getWorkDatee(), w.getWorkShifts()));
            EmployeeSchedule employeeSchedule = new EmployeeSchedule();
            employeeSchedule.setEmployee(w.getEmployee());
            employeeSchedule.setPosition(w.getPosition());
            employeeSchedule.setWorkSchedule(workSchedule);

            if (escF.addEmpSchedule(employeeSchedule).equals("ERR")) {
                Alerts.setAlerts("Operacja dodawania grafiku zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
        UtilView.getNewSchedule().clear();
    }

    public void btnDisable(Date d) {
        if (Calendar.getInstance().getTime().before(d)) {
            btnEdit.setDisable(false);
        } else {
            btnEdit.setDisable(true);
        }
    }

    @FXML
    public void editWorkSchedule() throws IOException {
        util.UtilView.setSellectedEmployeeSchedule(escF.findEmployeeSchedule(tablEmployeeSchedule.getSelectionModel().getSelectedItem().getIdEmployeeSchedule()));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/workSchedule/EditWorkScheduleFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja grafiku");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablEmployeeSchedule.setPlaceholder(new Label("Brak grafiku na wybrany dzień"));
        List<String> lst = new ArrayList();
        lst.add("Opiekun");
        lst.add("Weterynarz");
        lst.add("Magazynier");
        lst.add("Kasjer");
        comboPosition.setItems(FXCollections.observableList(lst));

        try {
            showTodaySchedule();
            showWeekSchedule();
        } catch (IOException ex) {
            Logger.getLogger(WorkScheduleFXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        showHistorySchedule();
    }
}

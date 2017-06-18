/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all;

import dao.EmployeeScheduleFacade;
import dao.WorkScheduleFacade;
import entity.EmployeeSchedule;
import entity.view.WorkScheduleView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class MyWorkScheduleFXML implements Initializable {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    WorkScheduleFacade workF = new WorkScheduleFacade(emf);
    EmployeeScheduleFacade esF = new EmployeeScheduleFacade(emf);

    WorkScheduleView wsv = new WorkScheduleView();

    @FXML
    private TableView<WorkScheduleView> tablEmployeeSchedule = new TableView();
    @FXML
    private TableColumn<WorkScheduleView, String> esPosition;
    @FXML
    private TableColumn<WorkScheduleView, String> esDate;
    @FXML
    private TableColumn<WorkScheduleView, String> esShifts;
    
    @FXML
    private DatePicker pickerWorkDateHistory;
    
    
    @FXML
    public void showHistorySchedule() {
        List<EmployeeSchedule> lst = (List<EmployeeSchedule>) UtilLogic.getSessionEmployee().getEmployeeScheduleCollection();
        ObservableList<WorkScheduleView> lstWorkSchedule = FXCollections.observableArrayList(wsv.workSchedule(lst, pickerWorkDateHistory.getValue()));
        esPosition.setCellValueFactory(new PropertyValueFactory("position"));
        esDate.setCellValueFactory(new PropertyValueFactory("workDate"));
        esShifts.setCellValueFactory(new PropertyValueFactory("workShifts"));
        tablEmployeeSchedule.setItems(lstWorkSchedule);
    }
    
    @FXML
    public void clearDatePicker() {
        pickerWorkDateHistory.setValue(null);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablEmployeeSchedule.setPlaceholder(new Label("Brak grafiku"));
        showHistorySchedule();
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import dao.EmployeeScheduleFacade;
import entity.view.WorkScheduleView;
import java.net.URL;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class TodayScheduleFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeScheduleFacade escF = new EmployeeScheduleFacade(emf);

    WorkScheduleView wsv = new WorkScheduleView();

    @FXML
    private Label lblToday;

    @FXML
    private VBox vKeeperA1;
    @FXML
    private VBox vKeeperA2A3;
    @FXML
    private VBox vKeeperB1B2;
    @FXML
    private VBox vKeeperC1C2;
    @FXML
    private VBox vKeeperD1D2;
    @FXML
    private VBox vKeeperE1E2;
    @FXML
    private VBox vKeeperF1;
    @FXML
    private VBox vVeterinarian;
    @FXML
    private VBox vWarehousemen;
    @FXML
    private VBox vSellar;

    public void positionSchedule(VBox box, WorkScheduleView w) {
        if (w != null) {
            box.getChildren().add(new Label(w.getEmployee().getFirstName() + " " + w.getEmployee().getLastName()));
        } else {
            box.getChildren().add(new Label("-----"));
        }
    }

    public void showTodaySchedule() {
        List<WorkScheduleView> lstWorkScheduleToday = wsv.workSchedule(escF.findEmployeeScheduleEntities(), Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        for (WorkScheduleView w : lstWorkScheduleToday) {
            switch (w.getPosition()) {
                case "Ssaki A1": {
                    positionSchedule(vKeeperA1, w);
                    break;
                }
                case "Ssaki A2/A3": {
                    positionSchedule(vKeeperA2A3, w);
                    break;
                }
                case "Ptaki B1/B2": {
                    positionSchedule(vKeeperB1B2, w);
                    break;
                }
                case "Gady C1/C2": {
                    positionSchedule(vKeeperC1C2, w);
                    break;
                }
                case "Płazy D1/D2": {
                    positionSchedule(vKeeperD1D2, w);
                    break;
                }
                case "Ryby E1/E2": {
                    positionSchedule(vKeeperE1E2, w);
                    break;
                }
                case "Bezkręgowce F1": {
                    positionSchedule(vKeeperF1, w);
                    break;
                }
                case "Weterynarz": {
                    positionSchedule(vVeterinarian, w);
                    break;
                }
                case "Magazynier": {
                    positionSchedule(vWarehousemen, w);
                    break;
                }
                case "Kasjer": {
                    positionSchedule(vSellar, w);
                    break;
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblToday.setText("Grafik na dziś " + UtilLogic.dateFormat(Calendar.getInstance().getTime()));

        showTodaySchedule();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import dao.EmployeeScheduleFacade;
import entity.view.WorkScheduleView;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.UtilView;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class PositionScheduleFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeScheduleFacade escF = new EmployeeScheduleFacade(emf);

    WorkScheduleView wsv = new WorkScheduleView();

    @FXML
    private Label lblPosition;
    @FXML
    private VBox boxWorkShirts;
    @FXML
    private VBox boxDay1;
    @FXML
    private VBox boxDay2;
    @FXML
    private VBox boxDay3;
    @FXML
    private VBox boxDay4;
    @FXML
    private VBox boxDay5;
    @FXML
    private VBox boxDay6;
    @FXML
    private VBox boxDay7;

    private LocalDate errorDate;

    public void getInfPosition(String position, String lbl1, String lbl2) {
        lblPosition.setText(position);
        boxWorkShirts.getChildren().add(new Separator(Orientation.HORIZONTAL));
        boxWorkShirts.getChildren().add(new Label(lbl1));
        boxWorkShirts.getChildren().add(new Separator(Orientation.HORIZONTAL));
        boxWorkShirts.getChildren().add(new Label(lbl2));
        boxWorkShirts.getChildren().add(new Separator(Orientation.HORIZONTAL));
    }

    public void getInfDate(VBox box, List<WorkScheduleView> w) {
        if (w.isEmpty()) {
            box.getChildren().add(new Label(errorDate.toString()));
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            box.getChildren().add(new Label("----"));
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            box.getChildren().add(new Label("----"));
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
        } else if (w.size() == 1) {
            String shift = w.get(0).getWorkShifts();
            if (shift.equals("Pierwsza")) {
                box.getChildren().add(new Label(w.iterator().next().getWorkDate()));
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
                Label lbl = new Label(w.get(0).getEmployee().getFirstName() + " " + w.get(0).getEmployee().getLastName());
                lbl.setTextAlignment(TextAlignment.CENTER);
                lbl.resize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                box.getChildren().add(lbl);
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
                box.getChildren().add(new Label("----"));
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            } else {
                box.getChildren().add(new Label(w.iterator().next().getWorkDate()));
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
                box.getChildren().add(new Label("----"));
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
                Label lbl = new Label(w.get(0).getEmployee().getFirstName() + " " + w.get(0).getEmployee().getLastName());
                lbl.setTextAlignment(TextAlignment.CENTER);
                lbl.resize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                box.getChildren().add(lbl);
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            }
        } else {
            box.getChildren().add(new Label(w.iterator().next().getWorkDate()));
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            for (WorkScheduleView ws : w) {
                Label lbl = new Label(ws.getEmployee().getFirstName() + " " + ws.getEmployee().getLastName());
                lbl.setTextAlignment(TextAlignment.CENTER);
                lbl.resize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                box.getChildren().add(lbl);
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            }
        }
    }

    public void getInfBox(String pos) {
        LocalDate ld = Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
        getInfDate(boxDay1, getDayWeekSchedule(pos, ld));
        getInfDate(boxDay2, getDayWeekSchedule(pos, ld.plusDays(1)));
        getInfDate(boxDay3, getDayWeekSchedule(pos, ld.plusDays(2)));
        getInfDate(boxDay4, getDayWeekSchedule(pos, ld.plusDays(3)));
        getInfDate(boxDay5, getDayWeekSchedule(pos, ld.plusDays(4)));
        getInfDate(boxDay6, getDayWeekSchedule(pos, ld.plusDays(5)));
        getInfDate(boxDay7, getDayWeekSchedule(pos, ld.plusDays(6)));
    }

    public void showWorkPosition(int pos) {
        switch (pos) {
            case 1: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ssaki A1");
                break;
            }
            case 2: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ssaki A2/A3");
                break;
            }
            case 3: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ptaki B1/B2");
                break;
            }
            case 4: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Gady C1/C2");
                break;
            }
            case 5: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Płazy D1/D2");
                break;
            }
            case 6: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ryby E1/E2");
                break;
            }
            case 7: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Bezkręgowce F1");
                break;
            }
            case 8: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Weterynarz");
                break;
            }
            case 9: {
                getInfPosition(UtilView.intToPosition(pos), "1", "1");
                getInfBox("Magazynier");
                break;
            }
            case 10: {
                getInfPosition(UtilView.intToPosition(pos), "1", "1");
                getInfBox("Kasjer");
                break;
            }
        }
    }

    public List<WorkScheduleView> getDayWeekSchedule(String position, LocalDate d) {
        errorDate = d;
        List<WorkScheduleView> lstWorkSchedule = wsv.workSchedule(escF.findEmployeeScheduleEntities(), d);
        List<WorkScheduleView> lst = new ArrayList();
        for (WorkScheduleView w : lstWorkSchedule) {
            if (w.getPosition().equals(position)) {
                lst.add(w);
            }
        }
        return lst;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showWorkPosition(UtilView.getPosition());
    }

}

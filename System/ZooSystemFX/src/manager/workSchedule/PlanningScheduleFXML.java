package manager.workSchedule;

import dao.EmployeeFacade;
import dao.EmployeeScheduleFacade;
import entity.Employee;
import entity.view.WorkScheduleView;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.UtilView;

public class PlanningScheduleFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    EmployeeScheduleFacade escF = new EmployeeScheduleFacade(emf);
    EmployeeFacade empF = new EmployeeFacade(emf);

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

    public void getInfDate(VBox box, List<WorkScheduleView> w, String empPosition, String position) {
        ObservableList<Employee> lst = FXCollections.observableList(empF.findEmployeePosition(empPosition));
        if (w.isEmpty()) {
            box.getChildren().add(new Label(errorDate.toString()));
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            ComboBox cb1 = new ComboBox();
            comboAction(cb1, position, "Pierwsza", errorDate);
            cb1.setItems(lst);
            box.getChildren().add(cb1);
            box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            ComboBox cb2 = new ComboBox();
            if (position.equals("Magazynier") || position.equals("Kasjer")) {
                comboAction(cb2, position, "Pierwsza", errorDate);
            } else {
                comboAction(cb2, position, "Druga", errorDate);
            }
            cb2.setItems(lst);
            box.getChildren().add(cb2);
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
                ComboBox cb2 = new ComboBox();
                comboAction(cb2, position, "Druga", errorDate);
                cb2.setItems(lst);
                box.getChildren().add(cb2);
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
            } else{
                box.getChildren().add(new Label(w.iterator().next().getWorkDate()));
                box.getChildren().add(new Separator(Orientation.HORIZONTAL));
                ComboBox cb2 = new ComboBox();
                comboAction(cb2, position, "Druga", errorDate);
                cb2.setItems(lst);
                box.getChildren().add(cb2);
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

    public void comboAction(ComboBox combo, String position, String shifts, LocalDate ld) {
        combo.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {
                UtilView.getNewSchedule().add(new WorkScheduleView(null, position,
                        (Employee) combo.getValue(), Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        shifts));
                System.out.println(UtilView.getNewSchedule());
            }
        });
    }

    public void getInfBox(String schedulePosition, String empPosition) {
        LocalDate ld = UtilView.getSellectedPlanningDate();
        getInfDate(boxDay1, getDayWeekSchedule(schedulePosition, ld), empPosition, schedulePosition);
        getInfDate(boxDay2, getDayWeekSchedule(schedulePosition, ld.plusDays(1)), empPosition, schedulePosition);
        getInfDate(boxDay3, getDayWeekSchedule(schedulePosition, ld.plusDays(2)), empPosition, schedulePosition);
        getInfDate(boxDay4, getDayWeekSchedule(schedulePosition, ld.plusDays(3)), empPosition, schedulePosition);
        getInfDate(boxDay5, getDayWeekSchedule(schedulePosition, ld.plusDays(4)), empPosition, schedulePosition);
        getInfDate(boxDay6, getDayWeekSchedule(schedulePosition, ld.plusDays(5)), empPosition, schedulePosition);
        getInfDate(boxDay7, getDayWeekSchedule(schedulePosition, ld.plusDays(6)), empPosition, schedulePosition);
    }

    public void getInfPosition(String schedulePositionView, String lbl1, String lbl2) {
        lblPosition.setText(schedulePositionView);
        boxWorkShirts.getChildren().add(new Separator(Orientation.HORIZONTAL));
        boxWorkShirts.getChildren().add(new Label(lbl1));
        boxWorkShirts.getChildren().add(new Separator(Orientation.HORIZONTAL));
        boxWorkShirts.getChildren().add(new Label(lbl2));
        boxWorkShirts.getChildren().add(new Separator(Orientation.HORIZONTAL));
    }

    public void showWorkPosition(int pos) {
        switch (pos) {
            case 1: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ssaki A1", "Opiekun");
                break;
            }
            case 2: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ssaki A2/A3", "Opiekun");
                break;
            }
            case 3: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ptaki B1/B2", "Opiekun");
                break;
            }
            case 4: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Gady C1/C2", "Opiekun");
                break;
            }
            case 5: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Płazy D1/D2", "Opiekun");
                break;
            }
            case 6: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Ryby E1/E2", "Opiekun");
                break;
            }
            case 7: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Bezkręgowce F1", "Opiekun");
                break;
            }
            case 8: {
                getInfPosition(UtilView.intToPosition(pos), "1", "2");
                getInfBox("Weterynarz", "Weterynarz");
                break;
            }
            case 9: {
                getInfPosition(UtilView.intToPosition(pos), "1", "1");
                getInfBox("Magazynier", "Magazynier");
                break;
            }
            case 10: {
                getInfPosition(UtilView.intToPosition(pos), "1", "1");
                getInfBox("Kasjer", "Kasjer");
                break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showWorkPosition(UtilView.getPosition());
    }

}

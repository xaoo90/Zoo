/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.holiday;

import dao.HolidayFacade;
import entity.Holiday;
import entity.view.HolidayView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.UtilView;

public class HolidayFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");

    HolidayFacade holF = new HolidayFacade(emf);

    @FXML
    private TableView<HolidayView> tablHolidayCurrent = new TableView();
    @FXML
    private TableColumn<HolidayView, Integer> holidayEmplCurrent;
    @FXML
    private TableColumn<HolidayView, Integer> dayCurrent;
    @FXML
    private TableColumn<HolidayView, String> holidayStartCurrent;
    @FXML
    private TableColumn<HolidayView, String> holidayEndCurrent;
    @FXML
    private TableColumn<HolidayView, String> holidayTypeCurrent;

    @FXML
    private TableView<HolidayView> tablHolidayHistory = new TableView();
    @FXML
    private TableColumn<HolidayView, Integer> holidayEmplHistory;
    @FXML
    private TableColumn<HolidayView, Integer> dayHistory;
    @FXML
    private TableColumn<HolidayView, String> holidayStartHistory;
    @FXML
    private TableColumn<HolidayView, String> holidayEndHistory;
    @FXML
    private TableColumn<HolidayView, String> holidayTypeHistory;

    @FXML
    private TableView<HolidayView> tablHolidayFuture = new TableView();
    @FXML
    private TableColumn<HolidayView, Integer> holidayEmplFuture;
    @FXML
    private TableColumn<HolidayView, Integer> dayFuture;
    @FXML
    private TableColumn<HolidayView, String> holidayStartFuture;
    @FXML
    private TableColumn<HolidayView, String> holidayEndFuture;
    @FXML
    private TableColumn<HolidayView, String> holidayTypeFuture;

    @FXML
    private TableView<HolidayView> tablHolidayRequest = new TableView();
    @FXML
    private TableColumn<HolidayView, Integer> holidayEmplRequest;
    @FXML
    private TableColumn<HolidayView, String> holidayTypeRequest;
    @FXML
    private TableColumn<HolidayView, Integer> dayRequest;
    @FXML
    private TableColumn<HolidayView, String> holidayStartRequest;
    @FXML
    private TableColumn<HolidayView, String> holidayEndRequest;

    @FXML
    private Button btnOk;
    @FXML
    private Button btnCan;

    HolidayView url = new HolidayView();

    List<HolidayView> lstHoliday = url.holiday(holF.findHolidayEntities());

    public void showHolidayCurrent() {
        Date d = Calendar.getInstance().getTime();
        ArrayList<HolidayView> lst = new ArrayList();
        for (HolidayView h : lstHoliday) {
            if (h.getStartDatee().before(d) && h.getEndDatee().after(d)) {
                lst.add(h);
            }
        }
        ObservableList<HolidayView> lstHolidayCurrent = FXCollections.observableArrayList(lst);
        holidayEmplCurrent.setCellValueFactory(new PropertyValueFactory("employee"));
        dayCurrent.setCellValueFactory(new PropertyValueFactory("day"));
        holidayStartCurrent.setCellValueFactory(new PropertyValueFactory("startDate"));
        holidayEndCurrent.setCellValueFactory(new PropertyValueFactory("endDate"));
        holidayTypeCurrent.setCellValueFactory(new PropertyValueFactory("type"));
        tablHolidayCurrent.setItems(lstHolidayCurrent);
    }

    public void showHolidayHistory() {
        Date d = Calendar.getInstance().getTime();
        ArrayList<HolidayView> lst = new ArrayList();
        for (HolidayView h : lstHoliday) {
            if (h.getEndDatee().before(d)) {
                lst.add(h);
            }
        }
        ObservableList<HolidayView> lstHolidayHistory = FXCollections.observableArrayList(lst);
        holidayEmplHistory.setCellValueFactory(new PropertyValueFactory("employee"));
        dayHistory.setCellValueFactory(new PropertyValueFactory("day"));
        holidayStartHistory.setCellValueFactory(new PropertyValueFactory("startDate"));
        holidayEndHistory.setCellValueFactory(new PropertyValueFactory("endDate"));
        holidayTypeHistory.setCellValueFactory(new PropertyValueFactory("type"));
        tablHolidayHistory.setItems(lstHolidayHistory);
    }

    public void showHolidayFuture() {
        Date d = Calendar.getInstance().getTime();
        ArrayList<HolidayView> lst = new ArrayList();
        String s;
        for (HolidayView h : lstHoliday) {
            if (h.getType().length() > "Pytanie".length()) {
                if (h.getStartDatee().after(d) && h.getType().substring(0, 7).equals("Pytanie") == false) {
                    lst.add(h);
                }
            }

            if (h.getStartDatee().after(d) && h.getType().equals("Pytanie") == false) {
                lst.add(h);
            }
        }
        ObservableList<HolidayView> lstHolidayFuture = FXCollections.observableArrayList(lst);
        holidayEmplFuture.setCellValueFactory(new PropertyValueFactory("employee"));
        dayFuture.setCellValueFactory(new PropertyValueFactory("day"));
        holidayStartFuture.setCellValueFactory(new PropertyValueFactory("startDate"));
        holidayEndFuture.setCellValueFactory(new PropertyValueFactory("endDate"));
        holidayTypeFuture.setCellValueFactory(new PropertyValueFactory("type"));
        tablHolidayFuture.setItems(lstHolidayFuture);
    }

    public void showHolidayRequest() {
        ArrayList<HolidayView> lst = new ArrayList();
        for (HolidayView h : lstHoliday) {
            if (h.getType().length() > "Pytanie".length()) {
                if (h.getType().substring(0, 7).equals("Pytanie")) {
                    h.setType(h.getType().substring(10));
                    lst.add(h);
                }
            }
        }
        ObservableList<HolidayView> lstHolidayRequest = FXCollections.observableArrayList(lst);
        holidayTypeRequest.setCellValueFactory(new PropertyValueFactory("type"));
        holidayEmplRequest.setCellValueFactory(new PropertyValueFactory("employee"));
        dayRequest.setCellValueFactory(new PropertyValueFactory("day"));
        holidayStartRequest.setCellValueFactory(new PropertyValueFactory("startDate"));
        holidayEndRequest.setCellValueFactory(new PropertyValueFactory("endDate"));
        tablHolidayRequest.setItems(lstHolidayRequest);
        disableButton(lstHolidayRequest);
    }

    public void disableButton(ObservableList lst) {
        if (lst.isEmpty()) {
            btnCan.setDisable(true);
            btnOk.setDisable(true);
        } else {
            btnCan.setDisable(false);
            btnOk.setDisable(false);
        }
    }

    @FXML
    public void addHoliday() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/holiday/AddHolidayFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie urlopu");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void acceptHolidey() throws Exception {
        Holiday h = UtilView.convertHolidayView(tablHolidayRequest.getSelectionModel().getSelectedItem());
        h.setType("Urlop");
        holF.edit(h);
    }

    @FXML
    public void cancelHolidey() throws Exception {
        Holiday h = UtilView.convertHolidayView(tablHolidayRequest.getSelectionModel().getSelectedItem());
        holF.destroy(h.getIdHoliday());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablHolidayCurrent.setPlaceholder(new Label("Brak pracowników przebywających aktualnie na urlopie"));
        tablHolidayFuture.setPlaceholder(new Label("Brak zaplanowanych urlopów"));
        tablHolidayHistory.setPlaceholder(new Label("Brak wpisów o urlopach"));
        tablHolidayRequest.setPlaceholder(new Label("Brak wniosków o urlop"));
        showHolidayHistory();
        showHolidayFuture();
        showHolidayRequest();
        showHolidayCurrent();
    }
}

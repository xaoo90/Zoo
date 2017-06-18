/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.employee;

import dao.EmployeeFacade;
import dao.EmployeeScheduleFacade;
import dao.OrderFeedFacade;
import dao.SectorFacade;
import entity.Cage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import entity.Employee;
import entity.MedExamination;
import entity.OrderFeed;
import entity.view.OrderPositionView;
import entity.Sector;
import entity.view.HolidayView;
import entity.view.OrderView;
import entity.view.WorkScheduleView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class EmployeeFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");

    EmployeeFacade empF = new EmployeeFacade(emf);
    SectorFacade secF = new SectorFacade(emf);
    OrderFeedFacade ordF = new OrderFeedFacade(emf);
    EmployeeScheduleFacade escF = new EmployeeScheduleFacade(emf);

    WorkScheduleView wsv = new WorkScheduleView();
    OrderView oView = new OrderView();

    ObservableList<Employee> lstEmployee = FXCollections.observableList(empF.findEmployeeEntities());

    @FXML
    private TextField txtFilter;

    @FXML
    private TableView<Employee> tableEmp = new TableView();
    @FXML
    private TableColumn<Employee, String> fName;
    @FXML
    private TableColumn<Employee, String> lName;

    @FXML
    private TableView<Sector> tableSector = new TableView();
    @FXML
    private TableColumn<Sector, String> sectorName;
    @FXML
    private TableColumn<Sector, String> sectorType;

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
    private TableView<Cage> tableCage = new TableView();
    @FXML
    private TableColumn<Cage, Integer> cageId;
    @FXML
    private TableColumn<Cage, String> cageType;
    @FXML
    private TableColumn<Cage, String> cageCondition;
    @FXML
    private TableColumn<Cage, String> cageSpace;

    @FXML
    private TableView<OrderView> tableOrderFeed = new TableView();
    @FXML
    private TableColumn<OrderView, Integer> orderFeedId;
    @FXML
    private TableColumn<OrderView, String> orderFeedDate;
    @FXML
    private TableColumn<OrderView, String> orderFeedCondition;
    @FXML
    private TableColumn<OrderView, String> orderFeedSum;

    @FXML
    private TableView<OrderPositionView> tablePosition = new TableView();
    @FXML
    private TableColumn<OrderPositionView, String> positionFeed;
    @FXML
    private TableColumn<OrderPositionView, BigDecimal> positionAmount;
    @FXML
    private TableColumn<OrderPositionView, BigDecimal> positionPrice;
    @FXML
    private TableColumn<OrderPositionView, BigDecimal> positionPriceAll;

    @FXML
    private TableView<HolidayView> tableHoliday = new TableView();
    @FXML
    private TableColumn<HolidayView, Integer> day;
    @FXML
    private TableColumn<HolidayView, DatePicker> empDate;
    @FXML
    private TableColumn<HolidayView, DatePicker> endDate;
    @FXML
    private TableColumn<HolidayView, String> holidayType;

    @FXML
    private TableView<MedExamination> tableMed = new TableView();
    @FXML
    private TableColumn<MedExamination, String> medDate;
    @FXML
    private TableColumn<MedExamination, String> medCondition;
    @FXML
    private TableColumn<MedExamination, String> medDescription;

    @FXML
    private Label lblLogin;
    @FXML
    private Label lblPosition;
    @FXML
    private Label lblEmpDate;
    @FXML
    private Label lblEndDate;
    @FXML
    private Label lblSalary;
    @FXML
    private Label lblBan;
    @FXML
    private Label lblHoliday;
    @FXML
    private Label lblMail;
    @FXML
    private Label lblCondition;

    @FXML
    private Tab tabSector;
    @FXML
    private Tab tabOrder;
    @FXML
    private Tab tabMed;

    public void showEmployee() {
        fName.setCellValueFactory(new PropertyValueFactory("firstName"));
        lName.setCellValueFactory(new PropertyValueFactory("lastName"));
        tableEmp.setItems(lstEmployee);
    }

    public void showEmployeeInfo() {
        tableEmp.getSelectionModel().selectFirst();
        tableEmp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableEmp.getSelectionModel().getSelectedIndex() >= 0) {
                        lblLogin.setText(tableEmp.getSelectionModel().getSelectedItem().getLogin());
                        lblPosition.setText(tableEmp.getSelectionModel().getSelectedItem().getPosition());
                        lblEmpDate.setText(UtilLogic.dateFormat(tableEmp.getSelectionModel().getSelectedItem().getEmploymentDate()));
                        if ((tableEmp.getSelectionModel().getSelectedItem().getEndingDate()) == null) {
                            lblEndDate.setText("Brak daty konca umowy");
                        } else {
                            lblEndDate.setText(UtilLogic.dateFormat(tableEmp.getSelectionModel().getSelectedItem().getEndingDate()));
                        }
                        lblSalary.setText(String.valueOf(tableEmp.getSelectionModel().getSelectedItem().getSalary()));
                        lblBan.setText(tableEmp.getSelectionModel().getSelectedItem().getBankAccountNumber());
                        lblHoliday.setText(String.valueOf(tableEmp.getSelectionModel().getSelectedItem().getHoliday()));
                        lblMail.setText(tableEmp.getSelectionModel().getSelectedItem().getEmail());
                        lblCondition.setText(tableEmp.getSelectionModel().getSelectedItem().getCondition());
                        showEmployeeHoliday(tableEmp.getSelectionModel().getSelectedItem());
                        showEmployeeSector(tableEmp.getSelectionModel().getSelectedItem());
                        showEmployeeOrder(tableEmp.getSelectionModel().getSelectedItem());
                        showEmployeeMed(tableEmp.getSelectionModel().getSelectedItem());
                        showHistorySchedule();
                        showTab();
                    }
                }
            }
        });
    }

    public void showEmployeeSector(Employee e) {
        ObservableList<Sector> lstSector = FXCollections.observableArrayList(empF.showSector(e));
        sectorName.setCellValueFactory(new PropertyValueFactory("name"));
        sectorType.setCellValueFactory(new PropertyValueFactory("type"));
        tableSector.setItems(lstSector);

        tableCage.setItems(FXCollections.emptyObservableList());

        showSectorCageInfo();
    }

    public void showSectorCageInfo() {
        tableSector.getSelectionModel().selectFirst();
        tableSector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() > 0) {
                    if (tableSector.getSelectionModel().getSelectedIndex() >= 0) {
                        ObservableList<Cage> lstCage = FXCollections.observableArrayList(secF.showCage(tableSector.getSelectionModel().getSelectedItem()));
                        cageId.setCellValueFactory(new PropertyValueFactory("idCage"));
                        cageType.setCellValueFactory(new PropertyValueFactory("type"));
                        cageCondition.setCellValueFactory(new PropertyValueFactory("condition"));
                        cageSpace.setCellValueFactory(new PropertyValueFactory("space"));
                        tableCage.setItems(lstCage);
                    }
                }
            }
        });
    }

    public void showEmployeeOrder(Employee e) {
        ObservableList<OrderView> lstOrder = FXCollections.observableArrayList(oView.order(empF.showOrderFeed(e)));
        orderFeedId.setCellValueFactory(new PropertyValueFactory("idOrder"));
        orderFeedDate.setCellValueFactory(new PropertyValueFactory("orderDate"));
        orderFeedCondition.setCellValueFactory(new PropertyValueFactory("condition"));
        orderFeedSum.setCellValueFactory(new PropertyValueFactory("sum"));
        tableOrderFeed.setItems(lstOrder);

        tablePosition.setItems(FXCollections.emptyObservableList());
        showOrderPositionInfo();
    }

    public void showOrderPositionInfo() {
        OrderPositionView pos = new OrderPositionView();
        tableOrderFeed.getSelectionModel().selectFirst();
        tableOrderFeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableOrderFeed.getSelectionModel().getSelectedIndex() >= 0) {
                        ObservableList<OrderPositionView> lstPosition = FXCollections.observableList(pos.position(ordF.showPosition(ordF.findOrderFeed(tableOrderFeed.getSelectionModel().getSelectedItem().getIdOrder()))));
                        positionFeed.setCellValueFactory(new PropertyValueFactory("feed"));
                        positionAmount.setCellValueFactory(new PropertyValueFactory("amount"));
                        positionPrice.setCellValueFactory(new PropertyValueFactory("price"));
                        positionPriceAll.setCellValueFactory(new PropertyValueFactory("cost"));
                        tablePosition.setItems(lstPosition);
                    }
                }
            }
        });
    }

    public void showEmployeeHoliday(Employee e) {
        HolidayView hol = new HolidayView();
        ObservableList<HolidayView> lstHoliday = FXCollections.observableArrayList(hol.holiday(empF.showHoliday(e)));
        day.setCellValueFactory(new PropertyValueFactory("day"));
        empDate.setCellValueFactory(new PropertyValueFactory("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory("endDate"));
        holidayType.setCellValueFactory(new PropertyValueFactory("type"));
        tableHoliday.setItems(lstHoliday);
    }

    public void showEmployeeMed(Employee e) {
        ObservableList<MedExamination> lstMed = FXCollections.observableArrayList(empF.showMed(e));
        medDate.setCellValueFactory(new PropertyValueFactory("medDate"));
        medCondition.setCellValueFactory(new PropertyValueFactory("condition"));
        medDescription.setCellValueFactory(new PropertyValueFactory("description"));
        tableMed.setItems(lstMed);
    }

    public void showTab() {
        if (tableEmp.getSelectionModel().getSelectedItem().getPosition().equals("Kierownik")) {
            tabSector.setDisable(false);
            tabOrder.setDisable(true);
            tabMed.setDisable(true);
        }
        if (tableEmp.getSelectionModel().getSelectedItem().getPosition().equals("Magazynier")) {
            tabOrder.setDisable(false);
            tabSector.setDisable(true);
            tabMed.setDisable(true);
        }
        if (tableEmp.getSelectionModel().getSelectedItem().getPosition().equals("Weterynarz")) {
            tabMed.setDisable(false);
            tabOrder.setDisable(true);
            tabSector.setDisable(true);
        }
        if (tableEmp.getSelectionModel().getSelectedItem().getPosition().equals("Kasjer")) {
            tabOrder.setDisable(true);
            tabSector.setDisable(true);
            tabMed.setDisable(true);
        }
        if (tableEmp.getSelectionModel().getSelectedItem().getPosition().equals("Opiekun")) {
            tabOrder.setDisable(true);
            tabSector.setDisable(true);
            tabMed.setDisable(true);
        }
    }

    @FXML
    public void addEmployee(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/employee/RegisterEmplFXML.fxml"));
        BorderPane border = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie pracownika");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(border);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void delEmployee(ActionEvent event) {
        empF.deleteEmployee(tableEmp.getSelectionModel().getSelectedItem().getIdEmployee());
    }

    @FXML
    public void editEmployee() throws IOException {
        util.UtilView.setSellectedEmployee(tableEmp.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/employee/EditEmplFXML.fxml"));
        BorderPane border = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja pracownika");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(border);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void filterEmployee() {
        FilteredList<Employee> lstEmployeeFilter = new FilteredList<Employee>(lstEmployee, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            lstEmployeeFilter.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();
                if (employee.getFirstName().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (employee.getLastName().toLowerCase().contains(lowerCase)) {
                    return true;
                }
                return false;
            });
        }
        );
        SortedList<Employee> lstEmployeeSort = new SortedList<Employee>(lstEmployeeFilter);
        lstEmployeeSort.comparatorProperty().bind(tableEmp.comparatorProperty());
        tableEmp.setItems(lstEmployeeSort);
    }

    public void showHistorySchedule() {
        ObservableList<WorkScheduleView> lstWorkSchedule = FXCollections.observableArrayList(wsv.workSchedule(new ArrayList(tableEmp.getSelectionModel().getSelectedItem().getEmployeeScheduleCollection()), pickerWorkDateHistory.getValue()));
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
        showEmployee();
        showEmployeeInfo();
        tableEmp.setPlaceholder(new Label("Brak pracowników"));
        tableHoliday.setPlaceholder(new Label("Brak informacji o urlopach"));
        tableSector.setPlaceholder(new Label("Brak sektorów"));
        tableCage.setPlaceholder(new Label("Brak klatek w wybranym sektorze"));
        tableOrderFeed.setPlaceholder(new Label("Brak zamówień"));
        tablePosition.setPlaceholder(new Label("Wybierz zamówienie"));
        tableMed.setPlaceholder(new Label("Brak zabiegów medycznych"));
        tablEmployeeSchedule.setPlaceholder(new Label("Brak grafiku"));
        filterEmployee();
    }
}

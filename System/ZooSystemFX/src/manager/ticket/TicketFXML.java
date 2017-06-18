/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.ticket;

import dao.TicketFacade;
import dao.TicketTypeFacade;
import entity.Ticket;
import entity.TicketType;
import entity.view.TicketYearMonthSumView;
import entity.view.TicketYearSumView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utilLogic.UtilLogic;
import util.UtilView;

public class TicketFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    TicketTypeFacade ttyF = new TicketTypeFacade(emf);
    TicketFacade ticF = new TicketFacade(emf);

    TicketYearSumView tysv = new TicketYearSumView();
    TicketYearMonthSumView tymsv = new TicketYearMonthSumView();

    ObservableList<TicketType> lstTicketTypes = FXCollections.observableArrayList(ttyF.findTicketTypeEntities());

    @FXML
    private TableView<TicketType> tableTicketType;
    @FXML
    private TableColumn<TicketType, String> ticketTypeName;
    @FXML
    private TableColumn<TicketType, BigDecimal> ticketTypePrice;

    @FXML
    private TableView<Ticket> tableTicket;
    @FXML
    private TableColumn<Ticket, String> ticketDate;
    @FXML
    private TableColumn<Ticket, String> ticketAmount;
    @FXML
    private TableColumn<Ticket, String> ticketCost;

    @FXML
    private DatePicker dateIssued;
    @FXML
    private TableView<Ticket> tableTicketDateDay;
    @FXML
    private TableColumn<Ticket, String> ticketTypeDateDay;
    @FXML
    private TableColumn<Ticket, String> ticketAmountDateDay;
    @FXML
    private TableColumn<Ticket, String> ticketCostDateDay;

    @FXML
    private BarChart<String, Number> barAmountDay;
    @FXML
    private BarChart<String, Number> barCostDay;
    @FXML
    private PieChart pieAmountDay;
    @FXML
    private PieChart pieCostDay;
    @FXML
    private ListView listAmount;
    @FXML
    private ListView listCost;

    @FXML
    private ComboBox comboYear;
    @FXML
    private ComboBox comboMonth;
    @FXML
    private TableView<TicketYearSumView> tableTicketDateMonth;
    @FXML
    private TableColumn<TicketYearSumView, String> ticketTypeDateMonth;
    @FXML
    private TableColumn<TicketYearSumView, String> ticketAmountDateMonth;
    @FXML
    private TableColumn<TicketYearSumView, String> ticketCostDateMonth;

    @FXML
    private BarChart<String, Number> barAmountYear;
    @FXML
    private BarChart<String, Number> barCostYear;
    @FXML
    private PieChart pieAmountYear;
    @FXML
    private PieChart pieCostYear;
    @FXML
    private ListView listAmountYear;
    @FXML
    private ListView listCostYear;
    @FXML
    private LineChart<String, Number> lineAmountYear;
    @FXML
    private LineChart<String, Number> lineCostYear;

    @FXML
    private BarChart<String, Number> barAmountMonth;
    @FXML
    private BarChart<String, Number> barCostMonth;
    @FXML
    private PieChart pieAmountMonth;
    @FXML
    private PieChart pieCostMonth;
    @FXML
    private ListView listAmountMonth;
    @FXML
    private ListView listCostMonth;
    @FXML
    private LineChart<String, Number> lineAmountMonth;
    @FXML
    private LineChart<String, Number> lineCostMonth;

    public void showTicketType() {
        ticketTypeName.setCellValueFactory(new PropertyValueFactory("name"));
        ticketTypePrice.setCellValueFactory(new PropertyValueFactory("price"));
        tableTicketType.setItems(lstTicketTypes);
        showTicketTypeInfo();
    }

    public void showTicketTypeInfo() {
        tableTicketType.getSelectionModel().selectFirst();
        tableTicketType.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 0) {
                    if (tableTicketType.getSelectionModel().getSelectedIndex() >= 0) {
                        ObservableList<Ticket> lstTicket = FXCollections.observableArrayList(ttyF.showTicket(tableTicketType.getSelectionModel().getSelectedItem()));
                        ticketDate.setCellValueFactory(new PropertyValueFactory("issuedDate"));
                        ticketAmount.setCellValueFactory(new PropertyValueFactory("amount"));
                        ticketCost.setCellValueFactory(new PropertyValueFactory("cost"));
                        tableTicket.setItems(lstTicket);
                    }
                }
            }
        });
    }

    @FXML
    public void addTicketType() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/ticket/AddTicketTypeFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Dodawanie rodzaju biletu");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editTicketType() throws IOException {
        UtilView.setSellectedTicketType(tableTicketType.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/ticket/EditTicketTypeFXML.fxml"));
        AnchorPane anchor = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edycja rodzaju biletu");
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(anchor);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void delTicketType() {
        ttyF.deleteTicketType(tableTicketType.getSelectionModel().getSelectedItem().getIdTicketType());
        tableTicketType.getItems().remove(tableTicketType.getSelectionModel().getSelectedIndex());
    }

    // statistic Day
    //
    //
    @FXML
    public void dayTicket() {
        Date d = Date.from(dateIssued.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        ObservableList<Ticket> lstTicket = FXCollections.observableList(ticF.showDateTicket(d));
        listDayTicket(lstTicket);
        showBarAmountDay(lstTicket, d);
        showBarCostDay(lstTicket, d);
        showPieAmountDay(lstTicket, d);
        showPieCostDay(lstTicket, d);

    }

    public void listDayTicket(ObservableList<Ticket> lstTicket) {
        ticketTypeDateDay.setCellValueFactory(new PropertyValueFactory("ticketType"));
        ticketAmountDateDay.setCellValueFactory(new PropertyValueFactory("amount"));
        ticketCostDateDay.setCellValueFactory(new PropertyValueFactory("cost"));
        tableTicketDateDay.setItems(lstTicket);
    }

    // Chart day
    //
    //
    public void showBarAmountDay(ObservableList<Ticket> ticket, Date d) {
        barAmountDay.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName(UtilLogic.dateFormat(d));

        for (Ticket t : ticket) {
            series.getData().add(new XYChart.Data(t.getTicketType().getName(), t.getAmount()));
        }
        barAmountDay.getData().add(series);
    }

    public void showBarCostDay(ObservableList<Ticket> ticket, Date d) {
        barCostDay.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName(UtilLogic.dateFormat(d));

        for (Ticket t : ticket) {
            series.getData().add(new XYChart.Data(t.getTicketType().getName(), t.getCost()));
        }
        barCostDay.getData().add(series);
    }

    public void showPieAmountDay(ObservableList<Ticket> ticket, Date d) {
        pieAmountDay.getData().clear();
        int amount = 0;

        for (Ticket t : ticket) {
            pieAmountDay.getData().add(new PieChart.Data(t.getTicketType().getName(), t.getAmount()));
            amount = amount + t.getAmount();
        }

        ObservableList<String> lstAmount = FXCollections.observableArrayList();
        lstAmount.add("% ≈");

        for (Ticket t : ticket) {
            lstAmount.add(String.format("%.2f", (float) t.getAmount() / amount * 100));
        }
        listAmount.setItems(lstAmount);
    }

    public void showPieCostDay(ObservableList<Ticket> ticket, Date d) {
        pieCostDay.getData().clear();
        double cost = 0.00;

        for (Ticket t : ticket) {
            pieCostDay.getData().add(new PieChart.Data(t.getTicketType().getName(), t.getCost().doubleValue()));
            cost = cost + t.getCost().doubleValue();
        }

        ObservableList<String> lstCost = FXCollections.observableArrayList();
        lstCost.add("% ≈");

        for (Ticket t : ticket) {
            lstCost.add(String.format("%.2f", (float) t.getCost().floatValue() / cost * 100));
        }
        listCost.setItems(lstCost);

    }

    //ststistic Year
    //
    //
    public List<String> getMonths() {
        List<String> lstMonth = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            lstMonth.add(UtilView.intToMonth(i));
        }
        return lstMonth;
    }

    public List<Integer> getYears() {
        List<Integer> lstYear = new ArrayList();
        for (int i = 2010; i <= 2030; i++) {
            lstYear.add(i);
        }
        return lstYear;
    }

    public void comboBoxitems() {
        ObservableList<String> lstMonth = FXCollections.observableList(getMonths());
        ObservableList<Integer> lstYear = FXCollections.observableList(getYears());
        comboMonth.setItems(lstMonth);
        comboYear.setItems(lstYear);
    }

    public void listMonthTicket(ObservableList<TicketYearSumView> lstTicket) {
        ticketTypeDateMonth.setCellValueFactory(new PropertyValueFactory("ticketType"));
        ticketAmountDateMonth.setCellValueFactory(new PropertyValueFactory("amount"));
        ticketCostDateMonth.setCellValueFactory(new PropertyValueFactory("cost"));
        tableTicketDateMonth.setItems(lstTicket);
    }

    public void yearTicket() {
        int year = comboYear.getSelectionModel().getSelectedItem().hashCode();
        clearChartMonth();
        comboMonth.setValue(null);
        ObservableList<TicketYearSumView> lstTicketView = FXCollections.observableList(tysv.sum(ttyF.findTicketTypeEntities(), ticF.showDateTicketYear(year)));
        listMonthTicket(lstTicketView);
        showBarAmount(lstTicketView, barAmountYear, comboYear);
        showBarCost(lstTicketView, barCostYear, comboYear);
        showPieAmount(lstTicketView, pieAmountYear, listAmountYear);
        showPieCost(lstTicketView, pieCostYear, listCostYear);
        lineAmountYear.getData().clear();
        lineCostYear.getData().clear();
        for (TicketType tt : lstTicketTypes) {
            ObservableList<TicketYearMonthSumView> lstTicket = FXCollections.observableList(tymsv.sumYearMonth(tt, ticF.showDateTicketYear(year)));
            showLineAmount(lstTicket, tt, lineAmountYear, "year");
            showLineCost(lstTicket, tt, lineCostYear, "year");
        }
    }

    public void showBarAmount(ObservableList<TicketYearSumView> ticket, BarChart bar, ComboBox combo) {
        bar.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName(combo.getValue().toString());

        for (TicketYearSumView t : ticket) {
            series.getData().add(new XYChart.Data(t.getTicketType().getName(), t.getAmount()));
        }
        bar.getData().add(series);
    }

    public void showBarCost(ObservableList<TicketYearSumView> ticket, BarChart bar, ComboBox combo) {
        bar.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName(combo.getValue().toString());

        for (TicketYearSumView t : ticket) {
            series.getData().add(new XYChart.Data(t.getTicketType().getName(), t.getCost()));
        }
        bar.getData().add(series);
    }

    public void showPieAmount(ObservableList<TicketYearSumView> ticket, PieChart pie, ListView list) {
        pie.getData().clear();
        int amount = 0;

        for (TicketYearSumView t : ticket) {
            pie.getData().add(new PieChart.Data(t.getTicketType().getName(), t.getAmount()));
            amount = amount + t.getAmount();
        }

        ObservableList<String> lstAmount = FXCollections.observableArrayList();
        lstAmount.add("% ≈");

        for (TicketYearSumView t : ticket) {
            lstAmount.add(String.format("%.2f", (float) t.getAmount() / amount * 100));
        }
        list.setItems(lstAmount);
    }

    public void showPieCost(ObservableList<TicketYearSumView> ticket, PieChart pie, ListView list) {
        pie.getData().clear();
        double cost = 0.00;

        for (TicketYearSumView t : ticket) {
            pie.getData().add(new PieChart.Data(t.getTicketType().getName(), t.getCost().doubleValue()));
            cost = cost + t.getCost().doubleValue();
        }

        ObservableList<String> lstCost = FXCollections.observableArrayList();
        lstCost.add("% ≈");

        for (TicketYearSumView t : ticket) {
            lstCost.add(String.format("%.2f", (float) t.getCost().floatValue() / cost * 100));
        }
        list.setItems(lstCost);
    }

    public void showLineAmount(ObservableList<TicketYearMonthSumView> ticket, TicketType ticketType, LineChart line, String s) {
        XYChart.Series series = new XYChart.Series();
        series.setName(ticketType.getName());
        for (TicketYearMonthSumView t : ticket) {
            if (ticketType.equals(t.getTicketType())) {
                switch (s) {
                    case "year":
                        series.getData().add(new XYChart.Data(UtilView.intToMonth(t.getMonthDay()), t.getAmount()));
                        break;
                    case "month":
                        series.getData().add(new XYChart.Data(String.valueOf(t.getMonthDay()), t.getAmount()));
                        break;
                }
            }
        }
        line.getData().add(series);
    }

    public void showLineCost(ObservableList<TicketYearMonthSumView> ticket, TicketType ticketType, LineChart line, String s) {
        XYChart.Series series = new XYChart.Series();
        series.setName(ticketType.getName());
        for (TicketYearMonthSumView t : ticket) {
            if (ticketType.equals(t.getTicketType())) {
                switch (s) {
                    case "year":
                        series.getData().add(new XYChart.Data(UtilView.intToMonth(t.getMonthDay()), t.getCost()));
                        break;
                    case "month":
                        series.getData().add(new XYChart.Data(String.valueOf(t.getMonthDay()), t.getCost()));
                        break;
                }
            }
        }
        line.getData().add(series);
    }

    //ststistic Month
    //
    //
    public void monthTicket() {
        int year = comboYear.getSelectionModel().getSelectedItem().hashCode();
        int month = 0;
        if (comboMonth.getSelectionModel().getSelectedItem() != null) {
            month = UtilView.monthToInt(comboMonth.getSelectionModel().getSelectedItem().toString());
        }
        ObservableList<TicketYearSumView> lstTicketView = FXCollections.observableList(tysv.sum(ttyF.findTicketTypeEntities(), ticF.showDateTicketMonth(year, month)));
        listMonthTicket(lstTicketView);
        if (comboMonth.getSelectionModel().getSelectedItem() != null) {
            showBarAmount(lstTicketView, barAmountMonth, comboMonth);
            showBarCost(lstTicketView, barCostMonth, comboMonth);
            showPieAmount(lstTicketView, pieAmountMonth, listAmountMonth);
            showPieCost(lstTicketView, pieCostMonth, listCostMonth);
            lineAmountMonth.getData().clear();
            lineCostMonth.getData().clear();

            for (TicketType tt : lstTicketTypes) {
                ObservableList<TicketYearMonthSumView> lstTicket = FXCollections.observableList(tymsv.sumMonthDay(tt, ticF.showDateTicketMonth(year, month)));
                showLineAmount(lstTicket, tt, lineAmountMonth, "month");
                showLineCost(lstTicket, tt, lineCostMonth, "month");
            }
        }
    }

    public void clearChartMonth() {
        barAmountMonth.getData().clear();
        barCostMonth.getData().clear();
        pieAmountMonth.getData().clear();
        pieCostMonth.getData().clear();
        lineAmountMonth.getData().clear();
        lineCostMonth.getData().clear();
        listAmountMonth.setItems(FXCollections.emptyObservableList());
        listAmountMonth.setItems(FXCollections.observableArrayList("% ≈"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTicketType();
        tableTicket.setPlaceholder(new Label("Wybierz typ biletu"));

        tableTicketDateDay.setPlaceholder(new Label("Brak biletów dla wybranego dnia"));
        LocalDate ld = Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dateIssued.setValue(ld);
        dayTicket();

        comboBoxitems();
        comboYear.setValue(ld.getYear());
        tableTicketDateMonth.setPlaceholder(new Label("Brak biletów dla wybranego roku/miesiaca"));
        yearTicket();

        comboMonth.setValue(UtilView.intToMonth(ld.getMonthValue()));
        monthTicket();

        tableTicketDateDay.setMouseTransparent(true);
        listAmount.setMouseTransparent(true);
        listCost.setMouseTransparent(true);

        tableTicketDateMonth.setMouseTransparent(true);
        listAmountYear.setMouseTransparent(true);
        listCostYear.setMouseTransparent(true);
    }

}

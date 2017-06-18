/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warehousemen.order;

import dao.FeedFacade;
import dao.OrderFeedFacade;
import dao.OrderPositionFacade;
import dao.exceptions.NonexistentEntityException;
import entity.Feed;
import entity.OrderFeed;
import entity.OrderPosition;
import entity.view.OrderPositionView;
import entity.view.OrderView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
public class OrderFeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    OrderFeedFacade ordF = new OrderFeedFacade(emf);
    OrderPositionFacade posF = new OrderPositionFacade(emf);
    FeedFacade feeF = new FeedFacade(emf);

    OrderView oView = new OrderView();

    @FXML
    private TableView<OrderFeed> tableOrderFeed = new TableView();
    @FXML
    private TableColumn<OrderFeed, Integer> orderFeedId;
    @FXML
    private TableColumn<OrderFeed, String> orderFeedDate;
    @FXML
    private TableColumn<OrderFeed, String> orderFeedEmpl;

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
    private TableView<OrderView> tableOrderFeedHistory = new TableView();
    @FXML
    private TableColumn<OrderView, Integer> orderFeedIdHistory;
    @FXML
    private TableColumn<OrderView, String> orderFeedDateHistory;
    @FXML
    private TableColumn<OrderView, String> orderFeedEmplHistory;
    @FXML
    private TableColumn<OrderView, String> orderFeedSumHistory;

    @FXML
    private TableView<OrderPositionView> tablePositionHistory = new TableView();
    @FXML
    private TableColumn<OrderPositionView, String> positionFeedHistory;
    @FXML
    private TableColumn<OrderPositionView, BigDecimal> positionAmountHistory;
    @FXML
    private TableColumn<OrderPositionView, BigDecimal> positionPriceHistory;
    @FXML
    private TableColumn<OrderPositionView, BigDecimal> positionPriceAllHistory;

    public void showOrderHistory() {
        ObservableList<OrderView> lstOrder = FXCollections.observableArrayList(oView.order(ordF.getOrderCondition("Zrealizowano")));
        orderFeedIdHistory.setCellValueFactory(new PropertyValueFactory("idOrder"));
        orderFeedDateHistory.setCellValueFactory(new PropertyValueFactory("orderDate"));
        orderFeedEmplHistory.setCellValueFactory(new PropertyValueFactory("warehouseMen"));
        orderFeedSumHistory.setCellValueFactory(new PropertyValueFactory("sum"));
        tableOrderFeedHistory.setItems(lstOrder);
        showOrderHistoryPositionInfo();
    }

    public void showOrder() {
        ObservableList<OrderFeed> lstOrder = FXCollections.observableArrayList(ordF.getOrderCondition("Zamówione"));
        orderFeedId.setCellValueFactory(new PropertyValueFactory("idOrder"));
        orderFeedDate.setCellValueFactory(new PropertyValueFactory("orderDate"));
        orderFeedEmpl.setCellValueFactory(new PropertyValueFactory("warehouseMen"));
        tableOrderFeed.setItems(lstOrder);
        showOrderPositionInfo();
    }

    public void showOrderHistoryPositionInfo() {
        OrderPositionView pos = new OrderPositionView();
        tableOrderFeedHistory.getSelectionModel().selectFirst();
        tableOrderFeedHistory.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<OrderPositionView> lstPosition = FXCollections.observableList(pos.position(ordF.showPosition(ordF.findOrderFeed(tableOrderFeedHistory.getSelectionModel().getSelectedItem().getIdOrder()))));
                positionFeedHistory.setCellValueFactory(new PropertyValueFactory("feed"));
                positionAmountHistory.setCellValueFactory(new PropertyValueFactory("amount"));
                positionPriceHistory.setCellValueFactory(new PropertyValueFactory("price"));
                positionPriceAllHistory.setCellValueFactory(new PropertyValueFactory("cost"));
                tablePositionHistory.setItems(lstPosition);
            }
        });
    }

    public void showOrderPositionInfo() {
        OrderPositionView pos = new OrderPositionView();
        tableOrderFeed.getSelectionModel().selectFirst();
        UtilView.setSellectedOrderFeed(tableOrderFeed.getSelectionModel().getSelectedItem());
        tableOrderFeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<OrderPositionView> lstPosition = FXCollections.observableList(pos.position(ordF.showPosition(tableOrderFeed.getSelectionModel().getSelectedItem())));
                positionFeed.setCellValueFactory(new PropertyValueFactory("feed"));
                positionAmount.setCellValueFactory(new PropertyValueFactory("amount"));
                positionPrice.setCellValueFactory(new PropertyValueFactory("price"));
                positionPriceAll.setCellValueFactory(new PropertyValueFactory("cost"));
                tablePosition.setItems(lstPosition);
            }
        });
    }

    @FXML
    public void pricePosition() throws IOException {
        UtilView.setSellectedPosition(posF.findOrderPosition(tablePosition.getSelectionModel().getSelectedItem().getIdOrderPosition()));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/warehousemen/order/PricePositionFXML.fxml"));
        BorderPane addEmpl = loader.load();
        Stage addDialogStage = new Stage();
        addDialogStage.setTitle("Ceny produktu");
        addDialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(addEmpl);
        addDialogStage.setScene(scene);
        addDialogStage.show();
    }

    public void acceptOrder() throws NonexistentEntityException, Exception {
        boolean price = true;
        OrderFeed of = tableOrderFeed.getSelectionModel().getSelectedItem();
        for (OrderPosition op : of.getOrderPositionCollection()) {
            if (op.getPrice() == null) {
                price = false;
            }
        }

        if (price) {
            for (OrderPosition op : of.getOrderPositionCollection()) {
                Feed f = op.getFeed();
                f.setAvailability(f.getAvailability().add(op.getAmount()));
                feeF.edit(f);
            }
            of.setCondition("Zrealizowano");

            if (ordF.editOrderFeed(of).equals("OK")) {
                tableOrderFeed.getItems().remove(tableOrderFeed.getSelectionModel().getSelectedIndex());
                tablePosition.getItems().clear();
                Alerts.setAlerts("Operacja potwierdzenia zamówienia zakończona powodzeniem\n");
                Alerts.returnAlert("Zatwierdźenie wykonane", null, AlertType.INFORMATION);
                Alerts.alertsClear();
            } else {
                Alerts.setAlerts("Operacja potwierdzenia zamówienia zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd zatwierdźenia", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        } else {
            Alerts.setAlerts("Nie wszystkie produkty posiadają wprowadzoną cenę\n");
            Alerts.returnAlert("Błąd zatwierdźenia", null, AlertType.ERROR);
            Alerts.alertsClear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablePositionHistory.setPlaceholder(new Label("Wybierz zamówienie"));
        tablePosition.setPlaceholder(new Label("Wybierz zamówienie"));
        tableOrderFeed.setPlaceholder(new Label("Brak zaplanowanych zamówień"));
        showOrderHistory();
        showOrder();
    }
}

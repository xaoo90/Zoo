/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warehousemen.feed;

import dao.FeedFacade;
import dao.OrderFeedFacade;
import dao.OrderPositionFacade;
import entity.OrderFeed;
import entity.OrderPosition;
import entity.view.FeedView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.Alerts;
import utilLogic.UtilLogic;

/**
 * FXML Controller class
 *
 * @author Xaoo
 */
public class FeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    FeedFacade feeF = new FeedFacade(emf);
    FeedView fView = new FeedView();
    OrderFeedFacade ordF = new OrderFeedFacade(emf);
    OrderPositionFacade oposF = new OrderPositionFacade(emf);

    ObservableList<OrderPosition> lstPosition = FXCollections.observableArrayList();

    @FXML
    private TableView<FeedView> tableFeed = new TableView();
    @FXML
    private TableColumn<FeedView, Integer> feedName;
    @FXML
    private TableColumn<FeedView, String> feedAv;
    @FXML
    private TableColumn<FeedView, String> feedUnit;
    @FXML
    private TableColumn<FeedView, String> feedDay;

    @FXML
    private TableView<OrderPosition> tableFeedPosition = new TableView();
    @FXML
    private TableColumn<OrderPosition, Integer> feedNamePosition;
    @FXML
    private TableColumn<OrderPosition, String> feedAvPosition;

    @FXML
    private Label lblAmount;
    @FXML
    private TextField txtAmount;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnOk;

    public void showFeed() {
        ObservableList<FeedView> lstFeed = FXCollections.observableArrayList(fView.feed(feeF.findFeedEntities()));
        feedName.setCellValueFactory(new PropertyValueFactory("name"));
        feedAv.setCellValueFactory(new PropertyValueFactory("availability"));
        feedUnit.setCellValueFactory(new PropertyValueFactory("unit"));
        feedDay.setCellValueFactory(new PropertyValueFactory("day"));
        tableFeed.setItems(lstFeed);
    }

    public void setDisable() {
        lblAmount.setDisable(false);
        txtAmount.setDisable(false);
        txtAmount.setText(null);
        btnAdd.setDisable(false);
        btnOk.setDisable(false);
        tableFeedPosition.setDisable(false);
    }

    public void setDisableTrue() {
        lblAmount.setDisable(true);
        txtAmount.setDisable(true);
        btnAdd.setDisable(true);
        btnOk.setDisable(true);
        tableFeedPosition.setDisable(true);
    }

    public void addFeed() {
        if (isValidate()) {
            OrderPosition op = new OrderPosition();
            op.setFeed(feeF.findFeed(tableFeed.getSelectionModel().getSelectedItem().getIdFeed()));
            op.setAmount(new BigDecimal(txtAmount.getText()));

            if (!lstPosition.add(op)) {
                Alerts.setAlerts("Operacja dodawania pożywienia zakończona niepowodzeniem\n");
                Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
                Alerts.alertsClear();
            }
        }
    }

    public boolean isValidate() {
        boolean val = true;
        if (Alerts.isNull(txtAmount.getText())) {
            Alerts.setAlerts("Pole Ilość jest wymagane\n");
            val = false;
        } else if (Alerts.isOnlyNumber(txtAmount.getText())) {
            Alerts.setAlerts("Ilość zawiera niedozwolone znaki (tylko cyfry)\n");
            val = false;
        }
        if (Alerts.isNull(tableFeed.getSelectionModel().getSelectedItem())) {
            Alerts.setAlerts("Nie wybrano produktu\n");
            val = false;
        }

        Alerts.returnAlert("Dodawanie pożywienia", "Błąd dodawania pożywienia", AlertType.ERROR);
        Alerts.alertsClear();
        return val;
    }

    public void addOrderFeed() throws Exception {
        
        OrderFeed order = new OrderFeed();
        order.setWarehouseMen(UtilLogic.getSessionEmployee());
        order.setOrderDate(Calendar.getInstance().getTime());
        order.setCondition("Zamówione");
        order = ordF.addOrderFeed(order);
        if (!Alerts.isNull(order)) {
            for (OrderPosition op : lstPosition) {
                op.setOrderFeed(order);
                oposF.addOrderPosition(op);
            }
            tableFeedPosition.getItems().clear();
            lstPosition.clear();
            setDisableTrue();
        } else {
            Alerts.setAlerts("Operacja dodawania zamówienia zakończona niepowodzeniem\n");
            Alerts.returnAlert("Błąd dodawania", null, AlertType.ERROR);
            Alerts.alertsClear();
        }
    }

    public void showFeedPosition() {
        feedNamePosition.setCellValueFactory(new PropertyValueFactory("feed"));
        feedAvPosition.setCellValueFactory(new PropertyValueFactory("amount"));
        tableFeedPosition.setItems(lstPosition);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableFeedPosition.setPlaceholder(new Label("Brak produktów"));
        showFeed();
        showFeedPosition();
    }
}

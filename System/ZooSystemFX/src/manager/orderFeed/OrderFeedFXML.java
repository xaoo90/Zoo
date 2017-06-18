/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.orderFeed;

import dao.OrderFeedFacade;
import entity.OrderFeed;
import entity.view.OrderPositionView;
import entity.view.OrderView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class OrderFeedFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    OrderFeedFacade ordF = new OrderFeedFacade(emf);
    OrderView oView = new OrderView();    

    ObservableList<OrderView> lstOrder = FXCollections.observableArrayList(oView.order(ordF.findOrderFeedEntities()));

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
    private Label lblMan;
    
    public void showOrder() {
        orderFeedId.setCellValueFactory(new PropertyValueFactory("idOrder"));
        orderFeedDate.setCellValueFactory(new PropertyValueFactory("orderDate"));
        orderFeedCondition.setCellValueFactory(new PropertyValueFactory("condition"));
        orderFeedSum.setCellValueFactory(new PropertyValueFactory("sum"));
        tableOrderFeed.setItems(lstOrder);
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
                        lblMan.setText("Zamówienie wprowadzone przez : " + tableOrderFeed.getSelectionModel().getSelectedItem().getWarehouseMen().getFirstName() + " " + tableOrderFeed.getSelectionModel().getSelectedItem().getWarehouseMen().getLastName());
                        ObservableList<OrderPositionView> lstPosition = FXCollections.observableArrayList(pos.position(ordF.showPosition(ordF.findOrderFeed(tableOrderFeed.getSelectionModel().getSelectedItem().getIdOrder()))));
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableOrderFeed.setPlaceholder(new Label("Brak zamówień"));
        tablePosition.setPlaceholder(new Label("Wybierz zamówienie"));
        showOrder();
    }

}

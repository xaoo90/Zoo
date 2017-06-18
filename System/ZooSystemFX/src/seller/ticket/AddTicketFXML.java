/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seller.ticket;

import dao.TicketFacade;
import dao.TicketTypeFacade;
import entity.Ticket;
import entity.TicketType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.UtilView;

public class AddTicketFXML implements Initializable {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    TicketTypeFacade ttypeF = new TicketTypeFacade(emf);
    TicketFacade ticF = new TicketFacade(emf);

    @FXML
    private Label lblName;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblSum;
    @FXML
    private TextField txtAmount;
    @FXML
    private VBox box;
    @FXML
    private Button btnAdd;

    public void amountUp() {
        int am = Integer.parseInt(txtAmount.getText());
        txtAmount.setText(String.valueOf(++am));
        Double d = new Double(lblPrice.getText());
        lblSum.setText(String.valueOf(d * am));
    }

    public void amountDown() {
        int am = Integer.parseInt(txtAmount.getText());
        if (am != 0) {
            txtAmount.setText(String.valueOf(--am));
            Double d = new Double(lblPrice.getText());
            lblSum.setText(String.valueOf(d * am));
        }
    }

    public void addButton() {
        for (TicketType tt : ttypeF.findTicketTypeEntities()) {
            Button b = new Button();
            b.setText(tt.getName().replaceAll("\\s","\n"));
            b.setTextAlignment(TextAlignment.CENTER);
            b.setPrefSize(100, 100);
            b.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    UtilView.setSellectedTicketType(tt);
                    lblName.setText(tt.getName());
                    lblPrice.setText(String.valueOf(tt.getPrice()));
                    txtAmount.setText("0");
                    lblSum.setText("00.00");
                    btnAdd.setDisable(false);
                }
            });
            box.getChildren().add(b);
        }
    }
    
    public void addTicket() throws Exception{
        Ticket t = new Ticket();
        t.setAmount(Integer.parseInt(txtAmount.getText()));
        t.setCost(new BigDecimal(lblSum.getText()));
        t.setIssuedDate(Calendar.getInstance().getTime());
        t.setTicketType(UtilView.getSellectedTicketType());
        ticF.addTicket(t);
    }
    
    @FXML
    private void clear() {
        lblName.setText(null);
        lblPrice.setText(null);
        txtAmount.setText("0");
        lblSum.setText("00.00");
        btnAdd.setDisable(true);
    }

    @FXML
    private void cancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtAmount.setText("0");
        lblSum.setText("00.00");
        addButton();
        btnAdd.setDisable(true);
    }

}

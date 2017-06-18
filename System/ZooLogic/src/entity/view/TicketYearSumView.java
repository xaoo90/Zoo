/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.view;

import entity.Ticket;
import entity.TicketType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xaoo
 */
public class TicketYearSumView {

    private TicketType ticketType;
    private Integer amount;
    private BigDecimal cost;

    public TicketYearSumView() {
    }

    public TicketYearSumView(TicketType ticketType, Integer amount, BigDecimal cost) {
        this.ticketType = ticketType;
        this.amount = amount;
        this.cost = cost;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ArrayList<TicketYearSumView> sum(List<TicketType> lstTicketType, List<Ticket> lstTicket) {
        ArrayList<TicketYearSumView> lstTicketSum = new ArrayList();
        Integer am = 0;
        BigDecimal co = new BigDecimal(0);
        for (TicketType tt : lstTicketType) {
            for (Ticket t : lstTicket) {
                if (tt.equals(t.getTicketType())) {
                    am = am + t.getAmount();
                    co = co.add(t.getCost());
                }
            }
            if (!am.equals(0) && !co.equals(new BigDecimal(0))) {
                lstTicketSum.add(new TicketYearSumView(tt, am, co));
                am = 0;
                co = new BigDecimal(0);
            }
        }
        return lstTicketSum;
    }
}

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
public class TicketYearMonthSumView {

    private TicketType ticketType;
    private int monthDay;
    private Integer amount;
    private BigDecimal cost;

    public TicketYearMonthSumView() {
    }

    public TicketYearMonthSumView(TicketType ticketType, int monthDay, Integer amount, BigDecimal cost) {
        this.ticketType = ticketType;
        this.monthDay = monthDay;
        this.amount = amount;
        this.cost = cost;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public int getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(int monthDay) {
        this.monthDay = monthDay;
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

    public ArrayList<TicketYearMonthSumView> sumYearMonth(TicketType ticketType, List<Ticket> lstTicket) {
        ArrayList<TicketYearMonthSumView> lstTicketSum = new ArrayList();
        Integer am = 0;
        BigDecimal co = new BigDecimal(0);
        for (int i = 0; i <= 12; i++) {
            for (Ticket t : lstTicket) {
                if (ticketType.equals(t.getTicketType()) && t.getIssuedDatee().getMonth() == i) {
                    am = am + t.getAmount();
                    co = co.add(t.getCost());
                }
            }
            if (!am.equals(0) && !co.equals(new BigDecimal(0))) {
                lstTicketSum.add(new TicketYearMonthSumView(ticketType, i + 1, am, co));
                am = 0;
                co = new BigDecimal(0);
            }
        }
        return lstTicketSum;
    }

    public ArrayList<TicketYearMonthSumView> sumMonthDay(TicketType ticketType, List<Ticket> lstTicket) {
        ArrayList<TicketYearMonthSumView> lstTicketSum = new ArrayList();
        Integer am = 0;
        BigDecimal co = new BigDecimal(0);
        for (int i = 0; i <= 31; i++) {
            for (Ticket t : lstTicket) {
                if (ticketType.equals(t.getTicketType()) && t.getIssuedDatee().getDate() == i) {
                    am = am + t.getAmount();
                    co = co.add(t.getCost());
                }
            }
            if (!am.equals(0) && !co.equals(new BigDecimal(0))) {
                lstTicketSum.add(new TicketYearMonthSumView(ticketType, i + 1, am, co));
                am = 0;
                co = new BigDecimal(0);
            }
        }
        return lstTicketSum;
    }

    @Override
    public String toString() {
        return "ticketType=" + ticketType + ", month=" + monthDay + ", amount=" + amount + ", cost=" + cost + '}';
    }

}

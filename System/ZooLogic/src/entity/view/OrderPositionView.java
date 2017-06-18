/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.view;

import entity.Feed;
import entity.OrderFeed;
import entity.OrderPosition;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderPositionView implements Serializable {

    private Integer idOrderPosition;
    private Feed feed;
    private OrderFeed orderFeed;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal cost;

    public OrderPositionView() {

    }

    public OrderPositionView(Integer idOrderPosition, Feed feed, OrderFeed orderFeed, BigDecimal amount, BigDecimal price) {
        this.idOrderPosition = idOrderPosition;
        this.feed = feed;
        this.orderFeed = orderFeed;
        this.amount = amount;
        this.price = price;
    }

    public OrderPositionView(Integer idOrderPosition, Feed feed, OrderFeed orderFeed, BigDecimal amount, BigDecimal price, BigDecimal cost) {
        this.idOrderPosition = idOrderPosition;
        this.feed = feed;
        this.orderFeed = orderFeed;
        this.amount = amount;
        this.price = price;
        this.cost = cost;
    }

    public Integer getIdOrderPosition() {
        return idOrderPosition;
    }

    public void setIdOrderPosition(Integer idOrderPosition) {
        this.idOrderPosition = idOrderPosition;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public OrderFeed getOrderFeed() {
        return orderFeed;
    }

    public void setOrderFeed(OrderFeed orderFeed) {
        this.orderFeed = orderFeed;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ArrayList<OrderPositionView> position(List<OrderPosition> position) {
        ArrayList<OrderPositionView> lstOrderPosition = new ArrayList();
        for (OrderPosition p : position) {
            if (p.getPrice() != null) {
                lstOrderPosition.add(new OrderPositionView(p.getIdOrderPosition(), p.getFeed(), p.getOrderFeed(),
                        p.getAmount(), p.getPrice(), p.getAmount().multiply(p.getPrice())));
            } else {
                lstOrderPosition.add(new OrderPositionView(p.getIdOrderPosition(), p.getFeed(), p.getOrderFeed(),
                        p.getAmount(), p.getPrice()));
            }
        }
        return lstOrderPosition;
    }
}

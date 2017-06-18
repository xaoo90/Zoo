/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.view;

import entity.Employee;
import entity.OrderFeed;
import entity.OrderPosition;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderView {

    private Integer idOrder;
    private String orderDate;
    private Employee warehouseMen;
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    private BigDecimal sum;

    public OrderView() {
    }

    public OrderView(Integer idOrder, String orderDate, Employee warehouseMen, String condition, BigDecimal sum) {
        this.idOrder = idOrder;
        this.orderDate = orderDate;
        this.warehouseMen = warehouseMen;
        this.condition = condition;
        this.sum = sum;
    }

    

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Employee getWarehouseMen() {
        return warehouseMen;
    }

    public void setWarehouseMen(Employee warehouseMen) {
        this.warehouseMen = warehouseMen;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal addPosition(OrderFeed of) {
        BigDecimal sum = new BigDecimal(0.00);
        for (OrderPosition op : of.getOrderPositionCollection()) {
            if (op.getPrice() != null && op.getAmount() !=null) {
                sum = sum.add(op.getPrice().multiply(op.getAmount()));
            }
        }
        return sum;
    }

    public ArrayList<OrderView> order(List<OrderFeed> order) {
        ArrayList<OrderView> lstOrder = new ArrayList();
        for (OrderFeed o : order) {
            lstOrder.add(new OrderView(o.getIdOrder(), o.getOrderDate(), o.getWarehouseMen(), o.getCondition(), addPosition(o)));

        }
        return lstOrder;
    }

}

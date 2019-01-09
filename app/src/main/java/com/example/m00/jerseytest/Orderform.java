package com.example.m00.jerseytest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Orderform entity. @author MyEclipse Persistence Tools
 */

public class Orderform implements java.io.Serializable {

    // Fields

    private String orderId;
    private String orderType;
    private Integer orderPrice;
    private String orderStatus;
    private Date orderDate;
    private String delivAddres;
    private List<Orderinvoice> orderinvoiceList = new ArrayList<>();

    // Constructors

    /** default constructor */
    public Orderform() {
    }

    // Property accessors

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderPrice() {
        return this.orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getDelivAddres() {
        return this.delivAddres;
    }

    public void setDelivAddres(String delivAddres) {
        this.delivAddres = delivAddres;
    }

    public List<Orderinvoice> getOrderinvoiceList() {
        return orderinvoiceList;
    }

    public void setOrderinvoiceList(List<Orderinvoice> orderinvoiceList) {
        this.orderinvoiceList = orderinvoiceList;
    }

}

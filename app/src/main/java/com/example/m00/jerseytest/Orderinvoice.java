package com.example.m00.jerseytest;

/**
 * Orderinvoice entity. @author MyEclipse Persistence Tools
 */

public class Orderinvoice implements java.io.Serializable {

    // Fields

    private String invoId;
    private String orderId;
    private String menuId;
    private String invoStatus;
    private Integer quantity;
    private Orderform orderform;
    private Menu menu;

    // Constructors

    /** default constructor */
    public Orderinvoice() {
    }

    // Property accessors

    public String getInvoId() {
        return invoId;
    }

    public void setInvoId(String invoId) {
        this.invoId = invoId;
    }

    public Orderform getOrderform() {
        return orderform;
    }

    public void setOrderform(Orderform orderform) {
        this.orderform = orderform;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getInvoStatus() {
        return invoStatus;
    }

    public void setInvoStatus(String invoStatus) {
        this.invoStatus = invoStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
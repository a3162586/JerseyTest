package com.example.m00.jerseytest;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu entity. @author MyEclipse Persistence Tools
 */

public class Menu implements java.io.Serializable {

    // Fields

    private String menuId;
    private String menuName;
    private String menuType;
    private Integer menuPrice;
    private String menuIntro;
    private String menuPhoto;
    private String menuStatus;
    private List<Orderinvoice> orderinvoiceList = new ArrayList<>();

    // Constructors

    /** default constructor */
    public Menu() {
    }

    // Property accessors

    public String getMenuId() {
        return this.menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return this.menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Integer getMenuPrice() {
        return this.menuPrice;
    }

    public void setMenuPrice(Integer menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuIntro() {
        return this.menuIntro;
    }

    public void setMenuIntro(String menuIntro) {
        this.menuIntro = menuIntro;
    }

    public String getMenuPhoto() {
        return this.menuPhoto;
    }

    public void setMenuPhoto(String menuPhoto) {
        this.menuPhoto = menuPhoto;
    }

    public String getMenuStatus() {
        return this.menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public List<Orderinvoice> getOrderinvoiceList() {
        return orderinvoiceList;
    }

    public void setOrderinvoiceList(List<Orderinvoice> orderinvoiceList) {
        this.orderinvoiceList = orderinvoiceList;
    }

}



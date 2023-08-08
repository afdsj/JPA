package com.ohgiraffers.section03.projection;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable /* 정의해놓고 사용하면 재사용성이 높다 */
public class MenuInfo {

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    public MenuInfo() {
    }

    public MenuInfo(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    @Override
    public String toString() {
        return "MenuInfo{" +
                "menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                '}';
    }
}

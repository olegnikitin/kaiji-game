package com.softserveinc.ita.kaiji.model;

public class Star {

    private Integer quantity;

    public Star(){}

    public Star(Integer quantity){
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

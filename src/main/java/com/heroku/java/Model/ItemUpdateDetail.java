package com.heroku.java.Model;

public class ItemUpdateDetail {
    private String itemName;
    private int oldQuantity;
    private int newQuantity;

    public ItemUpdateDetail(String itemName, int oldQuantity, int newQuantity) {
        this.itemName = itemName;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(int oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}

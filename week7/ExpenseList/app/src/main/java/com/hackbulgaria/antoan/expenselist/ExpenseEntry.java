package com.hackbulgaria.antoan.expenselist;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class ExpenseEntry {

    private int id;
    private String label;
    private float price;

    public ExpenseEntry(int id, String label, float price) {
        this.id = id;
        this.label = label;
        this.price = price;
    }

    public ExpenseEntry(String label, float price) {
        this.id = -1;
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "( _id=" + this.id + ", label=" + label + ", price=" + price + ")";
    }
}

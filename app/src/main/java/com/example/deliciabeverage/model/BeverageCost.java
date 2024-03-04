package com.example.deliciabeverage.model;

import java.text.DecimalFormat;

public class BeverageCost{

    // declare string variables for name, email, phone, beverage-type, beverage-flavor, beverage-size, additional, region, store, sale-date
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String beverageType;
    private String beverageSize;
    private String additional;
    private String beverageFlavor;
    private String region;
    private String store;
    private String salesDate;

    // declare double variable for milk, sugar, size cost.
    double milkCost;
    double sugarCost;
    double sizeCost;

    // Constructor of the model
    public BeverageCost() {

    }

    // getter and setter method for customerName
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // getter and setter method for customerEmail
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    // getter and setter method for customerPhone
    public String getCustomerPhone() {
        return customerPhone;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    // getter and setter method for beverageType
    public String getBeverageType() {
        return beverageType;
    }
    public void setBeverageType(String beverageType) {
        this.beverageType = beverageType;
    }

    // getter and setter method for beverageSize
    public String getBeverageSize() {
        return beverageSize;
    }
    public void setBeverageSize(String beverageSize) {
        this.beverageSize = beverageSize;
    }

    // getter and setter method for additional
    public String getAdditional() {
        return additional;
    }
    public void setAdditional(String additional) {
        this.additional = additional;
    }

    // getter and setter method for beverageFlavor
    public String getBeverageFlavor() {
        return beverageFlavor;
    }
    public void setBeverageFlavor(String beverageFlavor) {
        this.beverageFlavor = beverageFlavor;
    }

    // getter and setter method for region
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    // getter and setter method for store
    public String getStore() {
        return store;
    }
    public void setStore(String store) {
        this.store = store;
    }

    // getter and setter method for salesDate
    public String getSalesDate() {
        return salesDate;
    }
    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    // getter and setter method for milkCost
    public double getMilkCost() {
        return milkCost;
    }
    public void setMilkCost(double milkCost) {
        this.milkCost = milkCost;
    }

    // getter and setter method for sugarCost
    public double getSugarCost() {
        return sugarCost;
    }
    public void setSugarCost(double sugarCost) {
        this.sugarCost = sugarCost;
    }

    // getter and setter method for sizeCost
    public double getSizeCost() {
        return sizeCost;
    }
    public void setSizeCost(double sizeCost) {
        this.sizeCost = sizeCost;
    }



    // return bill of the customer
    public String getCustomerBill(){
        return "Name: " + customerName +
                "\nPhone: " + customerPhone +
                "\nBeverage Type: " + beverageType +
                "\nAdditional: " + additional +
                "\nBeverage Size: " + beverageSize +
                "\nFlavoring: " + beverageFlavor +
                "\nRegion: " + region +
                "\nStore: " + store +
                "\nTotal Bill: $" + String.valueOf(new DecimalFormat("##.##").format(getFinalCost()));
    }



    // calculate flavor cost
    public double getFlavorCost(){
        double flavoringCost = 0;

        switch (beverageFlavor)
        {
            case "None":
                flavoringCost = 0;
                break;
            case "Lemon":
                flavoringCost = 0.25;
                break;
            case "Ginger":
                flavoringCost = 0.75;
                break;
            case "Pumpkin Spice":
                flavoringCost = 0.50;
                break;
            case "Chocolate":
                flavoringCost = 0.75;
                break;
        }

        return flavoringCost;
    }



    // calculate additional cost
    public double getAdditionalCost(){
        return milkCost + sugarCost;
    }



    // calculate total cost
    public double getTotal(){
        return  sizeCost + getAdditionalCost() + getFlavorCost();
    }



    // calculate tax on total cost
    public double getTax(){
        return  getTotal() * 0.13;
    }



    // calculate final bill with tax
    public double getFinalCost(){
        return  getTotal() + getTax();
    }
}

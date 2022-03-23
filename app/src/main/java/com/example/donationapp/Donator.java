package com.example.donationapp;

public class Donator {
    private String name;
    private String amount;
    private String method;

    public Donator(String dName, String dAmount, String dMethod){
        name = dName;
        amount = dAmount;
        method = dMethod;
    }

    public String getName(){
        return name;
    }

    public void setName(String sName){
        name = sName;
    }

    public String getAmount(){
        return amount;
    }

    public void setAmount(String sAmount){
        amount = sAmount;
    }

    public String getMethod(){
        return method;
    }

    public void setMethod(String sMethod){
        method = sMethod;
    }

    @Override
    public String toString() {
        return ("Donator Name: "+ this.getName() + "\nAmount: " + this.getAmount() + "\nMethod: " + this.getMethod());
    }
}

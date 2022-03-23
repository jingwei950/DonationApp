package com.example.donationapp;

public class Donator {
    private String name;
    private String amount;

    public Donator(String dName, String dAmount){
        name = dName;
        amount = dAmount;
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

    @Override
    public String toString() {
        return ("Donator Name: "+ this.getName() + "\nAmount: " + this.getAmount());
    }
}

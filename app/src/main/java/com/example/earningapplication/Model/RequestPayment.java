package com.example.earningapplication.Model;

public class RequestPayment {
    String name;
    String email;
    String number;
    String paytmNumber;
    String date;
    int balance;

    public RequestPayment(){

    }
    public RequestPayment(String name,String email,String number,String paytmNumber,String date, int balance){
        this.name=name;
        this.email=email;
        this.number=number;
        this.paytmNumber=paytmNumber;
        this.date=date;
        this.balance=balance;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getPaytmNumber() {
        return paytmNumber;
    }

    public String getDate() {
        return date;
    }

    public int getBalance() {
        return balance;
    }
}

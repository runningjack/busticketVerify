package com.busticket.amedora.busticketverify.model;

/**
 * Created by Amedora on 7/20/2015.
 */
public class Bank {

    int id;
    public String bank_name;
    public String short_name;
    public String sort_code;
    public String address;

    public Bank(){
    }

    public Bank(String bank_name, String short_name, String sort_code, String address){
        this.bank_name      =   bank_name;
        this.short_name     =   short_name;
        this.sort_code      =   sort_code;
        this.address        =   address;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setBank_name(String bank_name){ this.bank_name = bank_name;}
    public void setShort_name(String short_name){this.short_name = short_name;}
    public void setSort_code(String sort_code){this.sort_code = sort_code;}
    public void setAddress(String address){this.address = address;}

    public int getId(){
        return  this.id;
    }
    public String getBank_name(){return this.bank_name;}
    public String getShort_name(){return this.short_name;}
    public String getSort_code(){return this.sort_code;}
    public String getAddress(){return this.address;}
}

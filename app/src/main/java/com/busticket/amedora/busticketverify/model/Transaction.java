package com.busticket.amedora.busticketverify.model;

import java.util.Random;

/**
 * Created by Amedora on 8/3/2015.
 */
public class Transaction {
    int id;
    String app_id;
    int status;
    String transId;
    Double trans_amount;

    String trans_type;
    String account_no;
    String narration;
    String updated_at;
    String created_at;


    public Transaction(){};

    public Transaction(int id, Double trans_amount, String app_id, String transId, String created_at, String updated_at, int status, String trans_type, String account_no, String narration){
        this.app_id = app_id;
        this.status = status;
        this.transId = transId;
        this.trans_amount = trans_amount;
        this.account_no = account_no;
        this.trans_type = trans_type;
        this.narration = narration;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.id = id;
    }

    public void setId(int id){this.id = id; }

    public void setApp_id(String app_id){ this.app_id = app_id; }

    public void setStatus(int status){this.status = status; }

    public void setTransId(String transId){this.transId = transId; }

    public void setUpdated_at(String updated_at){this.updated_at = updated_at; }

    public void setCreated_at(String created_at){this.created_at = created_at; }

    public void setTrans_amount(Double trans_amount){ this.trans_amount = trans_amount; }

    public void setTrans_type( String trans_type){this.trans_type = trans_type;}

    public void setAccount_no(String account_no){this.account_no = account_no;}

    public void setNarration(String narration){this.narration = narration;}

    public String getApp_id(){ return this.app_id;}

    public String getTrans_type(){return this.trans_type;}

    public String getAccount_no(){return  this.account_no;}

    public String getNarration(){return this.narration;}

    public String getTransId(){ return this.transId; }

    public int getStatus(){ return this.status;}

    public String getCreated_at(){ return this.created_at; }

    public String getUpdated_at(){ return this.updated_at; }

    public double getTrans_amount(){ return this.trans_amount; }

    public int getId(){ return this.id; }

    public static String mTransId(int mystring){
        Random rand = new Random();
        String transId="";
        int  n = rand.nextInt(50) + 1;
        transId = String.format("%010d", (mystring));
        return transId;
    }

}

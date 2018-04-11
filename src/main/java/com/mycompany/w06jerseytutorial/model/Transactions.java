/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.model;

import java.util.Date;



/**
 *
 * @author fintan
 */
public class Transactions {
    
    private Date timeStamp;
    private String transType; // debit or credit
    private String transDescritpion;
    private int postTransBal;
    
    

    public Transactions() {
    }

    public Transactions(Date timeStamp, String transType, String transDescritpion, int postTransBal) {
        this.timeStamp = timeStamp;
        this.transType = transType;
        this.transDescritpion = transDescritpion;
        this.postTransBal = postTransBal;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDescritpion() {
        return transDescritpion;
    }

    public void setTransDescritpion(String transDescritpion) {
        this.transDescritpion = transDescritpion;
    }

    public int getPostTransBal() {
        return postTransBal;
    }

    public void setPostTransBal(int postTransBal) {
        this.postTransBal = postTransBal;
    }
    
    
    
    


    
    
    
    

    
    
    
    
}

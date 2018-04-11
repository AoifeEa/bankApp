/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.model;

/**
 *
 * @author fintan
 */
public class SavingsAccount extends Account {

    int savingsBalance;
    int apr;

    public SavingsAccount() {
    }
    
    

    public SavingsAccount(int accountNo, int sortCode) {
        super(accountNo, sortCode);

    }

    public int getSavingsBalance() {
        return savingsBalance;
    }

    public void setSavingsBalance(int savingsBalance) {
        this.savingsBalance = savingsBalance;
    }

    public int getApr() {
        return apr;
    }

    public void setApr(int apr) {
        this.apr = apr;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" + "savingsBalance=" + savingsBalance + ", apr=" + apr + '}';
    }
    
    

}

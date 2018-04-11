/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fintan
 */
public class Account {

    private int accountNo;
    private int sortCode;
    private int accBalance;
    private List<Transactions> transactions;

    public Account() {
    }

    public void lodge(int amount) {
        int currentBalance = getAccBalance();
        setAccBalance(amount + currentBalance);
    }
    
    public void transferFunds(Account targetTransfer, int amount){
        int targetTransBal = targetTransfer.getAccBalance();
        targetTransfer.setAccBalance(targetTransBal + amount);
    }

    public void withdrawal(int amount) {
        int currentBalance = getAccBalance();
        setAccBalance(currentBalance - amount);
    }

    public Account(List<Transactions> transactions) {
        this.transactions = new ArrayList();
    }

    public void addTransaction(Transactions e) {
        transactions.add(e);
    }

    public Account(int accBalance) {
        this.accBalance = accBalance;
    }

    public Account(int accountNo, int sortCode) {
        this.accountNo = accountNo;
        this.sortCode = sortCode;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getSortCode() {
        return sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    public int getAccBalance() {
        return accBalance;
    }

    public void setAccBalance(int accBalance) {
        this.accBalance = accBalance;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{" + "accountNo=" + accountNo + "\n sortCode=" + sortCode + '}';
    }

}

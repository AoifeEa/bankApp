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
public class Customer {
    
    private int id;
    private String firstName;
    private String secondName;
    private String address;
    private String email;
    private String password;
    private List<Account> accounts;

    public Customer() {
    }

    public Customer(int id, String firstName, String secondName, String address, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.accounts = new ArrayList();
    }
    
    public void addAccount(Account e){
        accounts.add(e);
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", firstName=" + firstName + ", secondName=" + secondName + ", address=" + address + ", email=" + email + ", password=" + password + ", accounts=" + accounts + '}';
    }
    
    
    
    


    


    
    
    
    
}

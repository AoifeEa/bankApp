/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.service;

import com.mycompany.w06jerseytutorial.model.Account;
import com.mycompany.w06jerseytutorial.model.Customer;
import com.mycompany.w06jerseytutorial.model.SavingsAccount;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fintan
 */
public class CustomerService {

    public static List<Customer> list = new ArrayList<>();

    public static Account account = new Account();
    public static Customer customer = new Customer();

    public CustomerService() {
        Customer c1 = new Customer(1, "Fintan", "Sealy", "Dublin", "fintansealy@gmail.com", "cat1234");
        c1.addAccount(new Account(12345678, 445566));
        c1.addAccount(new SavingsAccount(123456789, 445566));

        Customer c2 = new Customer(2, "John", "Sealy", "Dublin", "fintansealy@gmail.com", "cat1234");
        c2.addAccount(new Account(11223344, 445577));

        list.add(c1);
        list.add(c2);
    }
    
    public void addNewCustomer(Customer newCustomer){
        list.add(newCustomer);
    }

    public List<Customer> getAllCustomers() {
        return list;
    }

    public Customer getCustomerById(int id) {
        return list.get(id - 1);
    }

    public List<Account> getAccountsById(int id) {
        Customer target = getCustomerById(id);
        return target.getAccounts();

    }

    // search for customer by ID to get thier associated accounts, then search these accounts by accNo to find that account.
    public Account getAccountByAccNo(int id, int accNo) {
        Customer target = getCustomerById(id);
        List<Account> acc = target.getAccounts();
        for (Account account1 : acc) {
            if (account1.getAccountNo() == accNo) {
                return account1;
            }
        }
        return null;
    }
    
//    public Account getAccountByAccNoOnly(int accNo){
//        Customer target = new Customer();
//        List<Account> acc = target.getAccounts();
//        for (Account account1 : acc) {
//            if (account1.getAccountNo() == accNo) {
//                return account1;
//            }
//        }
//        return null;
//    }
    

    public void lodge(int amount) {
        int currentBalance = account.getAccBalance();
        account.setAccBalance(amount + currentBalance);
    }
    
    public boolean sufficientFunds(Account target, int withdraw){

        int currentBalance = target.getAccBalance();
        if(withdraw > currentBalance){
            return false;
        }
        return true;     
    }

}

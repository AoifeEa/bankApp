/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.service;

import com.mycompany.w06jerseytutorial.model.Account;
import com.mycompany.w06jerseytutorial.model.Customer;
import com.mycompany.w06jerseytutorial.model.SavingsAccount;
import com.mycompany.w06jerseytutorial.model.Transactions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public void addNewCustomer(Customer newCustomer) {
        list.add(newCustomer);
    }

    public List<Account> getAllAccounts() {
        // List<Account> allAccounts = new ArrayList<>();
        for (Customer customer1 : list) {
            List<Account> accs = customer1.getAccounts();
            return accs;
        }
        return null;
    }

//    public boolean existingAccount(int accNumber) {
//        // List<Account> allAccounts = new ArrayList<>();
//        for (Customer customer1 : list) {
//            List<Account> accs = customer1.getAccounts();
//            for (Account acc : accs) {
//                if(accNumber == acc.getAccountNo())
//                    return true;
//            }
//        }
//        return false;
//    }
    
//        public int existingAccount() {
//        // List<Account> allAccounts = new ArrayList<>();
//        int rando = accNoGenerator();
//        for (Customer customer1 : list) {
//            List<Account> accs = customer1.getAccounts();
//            for (Account acc : accs) {
//                if(accNumber == acc.getAccountNo())
//                    return true;
//            }
//        }
//        return false;
//    }
    
    

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

    public void lodge(int amount) {
        int currentBalance = account.getAccBalance();
        account.setAccBalance(amount + currentBalance);
    }

    public boolean sufficientFunds(Account target, int withdraw) {

        int currentBalance = target.getAccBalance();
        if (withdraw > currentBalance) {
            return false;
        }
        return true;
    }

    public Customer getCustomerByName(String firstName, String lastName) {
        for (Customer customer1 : list) { //searches list 
            if (firstName.equalsIgnoreCase(customer1.getFirstName()) && lastName.equalsIgnoreCase(customer1.getSecondName())) { // searches name and compares
                return customer1;
            }
        }
        return null;
    }

    public int accNoGenerator() {
        Random rand = new Random();
        int n = rand.nextInt(99999999) + 10000000; // create 8 digit acc no

        return n;
    }

}

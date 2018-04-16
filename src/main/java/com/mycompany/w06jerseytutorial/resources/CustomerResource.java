/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.resources;

import com.google.gson.Gson;
import com.mycompany.w06jerseytutorial.model.Account;
import com.mycompany.w06jerseytutorial.model.Customer;
import com.mycompany.w06jerseytutorial.model.SavingsAccount;
import com.mycompany.w06jerseytutorial.model.Transactions;
import com.mycompany.w06jerseytutorial.service.CustomerService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author fintan
 */
@Path("/customer")
public class CustomerResource {

    CustomerService customerService = new CustomerService();
    public static List<Transactions> transList = new ArrayList<>();

    /**
     *
     * @return all customers
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers(); // gets all customers
    }

    /**
     *
     * @param id
     * @return customer with corresponding ID
     */
    @GET
    @Path("/id/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerByIDJSON(@PathParam("param") int id) {
        return customerService.getCustomerById(id); // gets customer by ID
    }

    /**
     *
     * @return accounts for all users
     */
    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> getAllAccounts() {
        return customerService.getAllAccounts(); // gets customer by ID
    }

    /**
     *
     * @param id
     * @param sortCode
     * @return response indicating customer has created new account
     * @throws JSONException
     */
    @POST
    @Path("/id/{param}/create/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNormalAccount(@PathParam("param") int id, String sortCode) throws JSONException {
        Customer target = customerService.getCustomerById(id); // gets customer by ID
        JSONObject jsonObj = new JSONObject(sortCode);
        int sort = jsonObj.getInt("sortCode");
        int newAccNo = customerService.accNoGenerator();
        int uniqueAcc = customerService.generateUniqueAccNo(newAccNo); // generates unique acc no if needed
        
        target.addAccount(new Account(uniqueAcc, sort)); // new account with random gen number and sort code from json user input
        JSONObject output = new JSONObject();
        output.put("account_created", "true");
        output.put("account_number", uniqueAcc);
        return Response.status(Response.Status.ACCEPTED).entity(output.toString()).build();
    }

    /**
     *
     * @param id
     * @param sortCode
     * @return response indicating a new savings account has been created for
     * that customer
     * @throws JSONException
     */
    @POST
    @Path("/id/{param}/create/savings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSavingsAccount(@PathParam("param") int id, String sortCode) throws JSONException {
        Customer target = customerService.getCustomerById(id); // gets customer by ID
        JSONObject jsonObj = new JSONObject(sortCode);
        int sort = jsonObj.getInt("sortCode");
        int newAccNo = customerService.accNoGenerator();
        int uniqueAcc = customerService.generateUniqueAccNo(newAccNo); // generates unique acc no if needed

        target.addAccount(new SavingsAccount(uniqueAcc, sort)); // new account with random gen number and sort code from json user input
        JSONObject output = new JSONObject();
        output.put("account_created", "true");
        output.put("account_number", newAccNo);
        return Response.status(Response.Status.ACCEPTED).entity(output.toString()).build();
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @return customers who with matching first and last names
     */
    @GET
    @Path("/name/{first}/{last}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerByName(@PathParam("first") String firstName, @PathParam("last") String lastName) {
        return customerService.getCustomerByName(firstName, lastName); // gets customer by name
    }

    /**
     *
     * @param id
     * @return account associated with ID
     */
    @GET
    @Path("/{id}/account")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> getAccById(@PathParam("id") int id) {
        return customerService.getAccountsById(id); // gets all accounts associated with customer ID
    }

    /**
     *
     * @param id
     * @param accNo
     * @return final account. search for customer by ID to get their associated
     * accounts, then search these accounts by accNo to find that account.
     */
    @GET
    @Path("/{id}/account/{accountno}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccByAccNo(@PathParam("id") int id, @PathParam("accountno") int accNo) {
        return customerService.getAccountByAccNo(id, accNo); // gets specific account by acc no
    }

    /**
     *
     * @param id
     * @param accNo
     * @return accountBalance
     * @throws JSONException
     */
    @GET
    @Path("/{id}/account/{accountno}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response displayBalance(@PathParam("id") int id, @PathParam("accountno") int accNo) throws JSONException {
        Account target = customerService.getAccountByAccNo(id, accNo);
        int currentBalance = target.getAccBalance(); // gets balance
        JSONObject output = new JSONObject();
        output.put("balance", currentBalance);
        return Response.status(Response.Status.OK).entity(output.toString()).build();
    }

    /**
     *
     * @param body
     * @return JSON string with new user info. ID is updated to end of list
     * index
     * @throws JSONException
     * @throws ParseException
     */
    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(String body) throws JSONException, ParseException {
        Gson gson = new Gson();
        Customer newCustomer = gson.fromJson(body, Customer.class
        ); //gives customer new id for the index at end of the list of customers
        int newId = getAllCustomers().size() + 1; //gives customer new id for the index at end of the list of customers
        for (Customer cust : getAllCustomers()) { //customer must have unique email to register
            if (newCustomer.getEmail().equalsIgnoreCase(cust.getEmail())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        }
        newCustomer.setId(newId);
        customerService.addNewCustomer(newCustomer);
        Map<String, String> map = new HashMap<>();
        map.put("customer_created", "true");
        //shows new customer ID
        map.put("uri", "/api/customer/id/" + newCustomer.getId());
        return Response.status(Response.Status.CREATED).entity(gson.toJson(map)).build();
    }

    /**
     *
     * @param id
     * @param accNo
     * @param lodgementAmount
     * @return final accountBalance after Customer makes lodgement
     * @throws JSONException
     */
    @PUT
    @Path("/{id}/account/{accountno}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response lodge(@PathParam("id") int id, @PathParam("accountno") int accNo, String lodgementAmount) throws JSONException {

        Account target = customerService.getAccountByAccNo(id, accNo);
        JSONObject jsonObj = new JSONObject(lodgementAmount);
        int lodgement = jsonObj.getInt("lodgementAmount");
        target.lodge(lodgement); // sets balance of account in order to lodge funds

        int newBalance = target.getAccBalance(); // balance updated
        target.addTransaction(new Transactions(new Date(), "Credit", "Lodgement", newBalance));
        JSONObject output = new JSONObject();
        output.put("lodgement_made", "true");
        output.put("lodgment_amount", lodgement);
        output.put("new_balance", newBalance);

        return Response.status(Response.Status.ACCEPTED).entity(output.toString()).build();
    }

    /**
     *
     * @param id
     * @param accNo
     * @param withdrawalAmount
     * @return new account balance after funds have been withdrawn - requires
     * sufficient funds
     * @throws JSONException
     */
    @PUT
    @Path("/{id}/account/{accountno}/withdraw")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response withdraw(@PathParam("id") int id, @PathParam("accountno") int accNo, String withdrawalAmount) throws JSONException {

        Account target = customerService.getAccountByAccNo(id, accNo);
        JSONObject jsonObj = new JSONObject(withdrawalAmount);
        int withdraw = jsonObj.getInt("withdrawalAmount");
        if (customerService.sufficientFunds(target, withdraw) == true) { // customer must have funds >= withdrawal amount in order to withdraw
            target.withdrawal(withdraw); // withdraws funds
            int newBalance = target.getAccBalance();
            target.addTransaction(new Transactions(new Date(), "Debit", "Withdrawal", newBalance));
            JSONObject output = new JSONObject();
            output.put("withdrawal_made", "true");
            output.put("withdrawal_amount", withdraw);
            output.put("new_balance", newBalance);

            return Response.status(Response.Status.ACCEPTED).entity(output.toString()).build();
        }
        int balance = target.getAccBalance();
        JSONObject rejectOutput = new JSONObject();
        rejectOutput.put("withdrawal_made", "false");
        rejectOutput.put("reason", "insufficient_funds");
        rejectOutput.put("balance", balance);
        return Response.status(Response.Status.FORBIDDEN).entity(rejectOutput.toString()).build();
    }

    /**
     *
     * @param id
     * @param accNo
     * @param targetId
     * @param transferAcc
     * @param transferAmount
     * @return status of transfer. successful transaction will transfer funds to desired account and will update both account funds appropriately 
     * @throws JSONException
     */
    @PUT
    @Path("/{id}/account/{accountno}/tranfer/{targetid}/{targetaccountno}") // localhost:49000/api/customer/1/account/12345678/tranfer/2/11223344
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("id") int id, @PathParam("accountno") int accNo, @PathParam("targetid") int targetId, @PathParam("targetaccountno") int transferAcc, String transferAmount) throws JSONException {

        Account current = customerService.getAccountByAccNo(id, accNo);
        JSONObject jsonObj = new JSONObject(transferAmount);
        int trans = jsonObj.getInt("transferAmount");
        Account targetTransfer = customerService.getAccountByAccNo(targetId, transferAcc);
        if (customerService.sufficientFunds(current, trans) == true) { // customers funds must be >= transaction amount to transfer funds
            current.transferFunds(targetTransfer, trans); // funds tranferred, target account balance is updated
            current.withdrawal(trans); // transfereed funds are withdrawm from account

            int newBalance = current.getAccBalance();
            int transTargetNewBal = targetTransfer.getAccBalance();
            current.addTransaction(new Transactions(new Date(), "Debit", "Transfer", newBalance)); // transaction added to customer who transfers funds
            targetTransfer.addTransaction(new Transactions(new Date(), "Credit", "Transfer", newBalance)); // transaction added to customer who recieves transferred funds
            JSONObject output = new JSONObject();
            output.put("transfer_made", "true");
            output.put("transfer_amount", trans);
            output.put("new_balance", newBalance);
            output.put("trans_target", transTargetNewBal);

            return Response.status(Response.Status.ACCEPTED).entity(output.toString()).build();
        }

        int balance = current.getAccBalance();
        JSONObject rejectOutput = new JSONObject();
        rejectOutput.put("transfer_made", "false");
        rejectOutput.put("reason", "insufficient_funds");
        rejectOutput.put("balance", balance);
        return Response.status(Response.Status.FORBIDDEN).entity(rejectOutput.toString()).build();

    }

    /**
     *
     * @param id
     * @param accNo
     * @return list of transactions for searched account
     */
    @GET
    @Path("/{id}/account/{accountno}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transactions> displayTransactions(@PathParam("id") int id, @PathParam("accountno") int accNo) {
        Account target = customerService.getAccountByAccNo(id, accNo);
        return target.getTransactions(); // returns all teansactions for specified account
    }

}

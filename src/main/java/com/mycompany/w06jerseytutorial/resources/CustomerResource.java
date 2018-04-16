/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.w06jerseytutorial.resources;

import com.google.gson.Gson;
import com.mycompany.w06jerseytutorial.model.Account;
import com.mycompany.w06jerseytutorial.model.Customer;
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
        return customerService.getAllCustomers();
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
        return customerService.getCustomerById(id);
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
        return customerService.getAccountsById(id);
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
        return customerService.getAccountByAccNo(id, accNo);
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
        int currentBalance = target.getAccBalance();
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
        Customer newCustomer = gson.fromJson(body, Customer.class);
        int newId = getAllCustomers().size() + 1;
        for (Customer cust : getAllCustomers()) {
            if (newCustomer.getEmail().equalsIgnoreCase(cust.getEmail())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        }
        newCustomer.setId(newId);
        customerService.addNewCustomer(newCustomer);
        Map<String, String> map = new HashMap<>();
        map.put("customer_created", "true");
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
        target.lodge(lodgement);

        int newBalance = target.getAccBalance();
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
        if (customerService.sufficientFunds(target, withdraw) == true) {
            target.withdrawal(withdraw);
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

    @PUT
    @Path("/{id}/account/{accountno}/tranfer/{targetid}/{targetaccountno}") // localhost:49000/api/customer/1/account/12345678/tranfer/2/11223344
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("id") int id, @PathParam("accountno") int accNo, @PathParam("targetid") int targetId, @PathParam("targetaccountno") int transferAcc, String transferAmount) throws JSONException {

        Account current = customerService.getAccountByAccNo(id, accNo);
        JSONObject jsonObj = new JSONObject(transferAmount);
        int trans = jsonObj.getInt("transferAmount");
        Account targetTransfer = customerService.getAccountByAccNo(targetId, transferAcc);
        if (customerService.sufficientFunds(current, trans) == true) {
            current.transferFunds(targetTransfer, trans);
            current.withdrawal(trans);

            int newBalance = current.getAccBalance();
            int transTargetNewBal = targetTransfer.getAccBalance();
            current.addTransaction(new Transactions(new Date(), "Debit", "Transfer", newBalance));
            targetTransfer.addTransaction(new Transactions(new Date(), "Credit", "Transfer", newBalance));
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

    @GET
    @Path("/{id}/account/{accountno}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transactions> displayTransactions(@PathParam("id") int id, @PathParam("accountno") int accNo) {
        Account target = customerService.getAccountByAccNo(id, accNo);
        return target.getTransactions();
    }

}

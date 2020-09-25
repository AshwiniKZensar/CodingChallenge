package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.dto.TransferMoneyRequest;
import com.db.awmd.challenge.exception.AccountNotExistsException;
import com.db.awmd.challenge.exception.InsufficientFundsException;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;

@Service
public class AccountsService {

  @Autowired 
  private NotificationService notificationService;
	
  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  
  
  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  public Account transferMoney(TransferMoneyRequest transferMoneyReq) throws InsufficientFundsException,AccountNotExistsException{
	  
	  //check if source account and destination account exists
	  Account srcAccount = accountsRepository.getAccount(transferMoneyReq.getFromAccountId());
	  Account destAccount = accountsRepository.getAccount(transferMoneyReq.getToAccountId());
	  
	  //if the account not present throw exception
	  if(destAccount ==null) {
		  throw new AccountNotExistsException(transferMoneyReq.getToAccountId() +" ::  Account not present"); 
	  }  
	  
	  //if the account not present throw exception
	  if(srcAccount ==null) {
		  throw new AccountNotExistsException(transferMoneyReq.getFromAccountId() +" ::  Account not present"); 
	  }  
		
	  //check if account balance > amount to be transfered. We can check with threshold or minimum balance as well if required
	  BigDecimal difference= srcAccount.getBalance().subtract(transferMoneyReq.getAmount());
	  if(difference.compareTo(BigDecimal.ZERO)>0) {	
		  Account updatedACcount = updateBalances(srcAccount,destAccount,transferMoneyReq.getAmount());
		  //send notifications
		  notificationService.notifyAboutTransfer(updatedACcount, "Rs. " +transferMoneyReq.getAmount() +" has been transferred to destination account "+destAccount.getAccountId());
		  notificationService.notifyAboutTransfer(destAccount, "Rs. " +transferMoneyReq.getAmount() +" has been credited to you from account "+updatedACcount.getAccountId());

		  return updatedACcount; 		  
      } else {
    	  throw new InsufficientFundsException("InsufficientFunds Present in account ::"+transferMoneyReq.getFromAccountId());
      }  
	  
   }
  
  
  private synchronized Account updateBalances(Account srcAccount, Account destinationAccount, BigDecimal amount) {
	  
	  srcAccount.setBalance(srcAccount.getBalance().subtract(amount));
	  destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
	  Account updatedSrcAccount = this.accountsRepository.updateAccountBalance(srcAccount);
	  this.accountsRepository.updateAccountBalance(destinationAccount);

	  return updatedSrcAccount;
	   
  }
  
}

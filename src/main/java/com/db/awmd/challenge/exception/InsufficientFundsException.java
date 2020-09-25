package com.db.awmd.challenge.exception;


/**
 * @author Ashwini Khadkikar
 * Newly added exception if the account does not have Insufficient Funds
 *
 */
public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException(String errorMessage){
		super(errorMessage);
	}
	
}

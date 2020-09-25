package com.db.awmd.challenge.exception;


/**
 * @author Ashwini Khadkikar
 * Newly added exception if the account not exists. 
 *
 */
public class AccountNotExistsException extends RuntimeException {

	public AccountNotExistsException(String errorMessage){
		super(errorMessage);
	}
	
}

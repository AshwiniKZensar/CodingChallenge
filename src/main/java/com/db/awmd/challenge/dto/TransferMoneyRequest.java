package com.db.awmd.challenge.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class TransferMoneyRequest {
	
	
	
	@NotNull
	@NotEmpty
	String fromAccountId;	
	
	
	@NotNull
	@NotEmpty
	String toAccountId;	
	
	@NotNull
	@Min(value = 0, message = "Initial balance must be positive.")
	BigDecimal amount;	
	
   public TransferMoneyRequest(String fromAcc, String toAcc, BigDecimal amt){
		this.fromAccountId = fromAcc;
		this.toAccountId = toAcc;
		this.amount= amt;
	}

}

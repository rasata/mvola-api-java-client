package com.codeandscale.payment.mvola.v2.client;

import lombok.Data;

@Data
public class PaymentRequest
{
	private String id;
	private String label;
	private Double amount = 0.0D;
}

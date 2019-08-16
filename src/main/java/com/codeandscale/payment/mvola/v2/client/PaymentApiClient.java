package com.codeandscale.payment.mvola.v2.client;

public interface PaymentApiClient
{
	PaymentResponse requestPayment(PaymentRequest request) throws PaymentApiClientException;

	PaymentStatus getPaymentStatus(String token) throws PaymentApiClientException;
}

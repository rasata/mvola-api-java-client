package com.codeandscale.payment.mvola.v2.client;

public interface PaymentApiClient
{
	PaymentToken requestPayment(PaymentRequest request) throws PaymentApiClientException;

	PaymentStatus getPaymentStatus(PaymentToken token) throws PaymentApiClientException;
}

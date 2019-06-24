package com.codeandscale.payment.mvola.v2.client;

public interface PaymentApiClient
{
	PaymentToken requestPayment(PaymentRequest request) throws PaymentClientException;

	PaymentStatus getPaymentStatus(PaymentToken token) throws PaymentClientException;
}

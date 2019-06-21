package com.codeandscale.payment.mvola.v2.client;

public interface PaymentClient
{
	PaymentToken requestPayment(PaymentRequest request) throws PaymentClientException;

	PaymentStatus getPaymentStatus(PaymentToken token) throws PaymentClientException;
}

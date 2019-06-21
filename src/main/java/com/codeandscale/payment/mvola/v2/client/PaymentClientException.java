package com.codeandscale.payment.mvola.v2.client;

public class PaymentClientException extends Exception
{
	public PaymentClientException()
	{
		super();
	}

	public PaymentClientException(String message)
	{
		super(message);
	}

	public PaymentClientException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PaymentClientException(Throwable cause)
	{
		super(cause);
	}

	protected PaymentClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

package com.codeandscale.payment.mvola.v2.client;

public class PaymentApiClientException extends Exception
{
	public PaymentApiClientException()
	{
		super();
	}

	public PaymentApiClientException(String message)
	{
		super(message);
	}

	public PaymentApiClientException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PaymentApiClientException(Throwable cause)
	{
		super(cause);
	}

	protected PaymentApiClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

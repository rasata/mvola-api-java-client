package com.codeandscale.payment.mvola.v2.client.impl;

import com.codeandscale.payment.mvola.v2.ApiEndpoint;
import com.codeandscale.payment.mvola.v2.Authentication;
import com.codeandscale.payment.mvola.v2.client.*;
import https.www_telma_net.mpgw.v2.ws.mpgwapi.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.net.MalformedURLException;

public class SoapPaymentApiClientImpl implements PaymentApiClient
{
	private final Authentication authentication;

	public SoapPaymentApiClientImpl(Authentication authentication)
	{
		this.authentication = authentication;
	}

	@Override
	public PaymentToken requestPayment(PaymentRequest paymentRequest) throws PaymentClientException
	{
		try
		{
			MPGwCoreBundleServiceResponsePaymentRequestRequest request = new MPGwCoreBundleServiceResponsePaymentRequestRequest();
			request.setLoginWS(authentication.getLogin());
			request.setPasswordWS(authentication.getPassword());
			request.setHashCodeWS(authentication.getHash());
			request.setShopTransactionID(paymentRequest.getId());
			request.setShopTransactionLabel(paymentRequest.getLabel());
			request.setShopTransactionAmount(paymentRequest.getAmount().intValue());

			MPGwCoreBundleServiceResponsePaymentRequestResponse response = getApiPortType().wsMPGwPaymentRequest(ApiEndpoint.VERSION, request);

			responseSanityCheck(response);

			return new PaymentToken(response.getMPGwTokenID());
		}
		catch (MalformedURLException | IllegalAccessException e)
		{
			throw new PaymentClientException(e);
		}
	}

	@Override
	public PaymentStatus getPaymentStatus(PaymentToken token) throws PaymentClientException
	{
		try
		{
			MPGwCoreBundleServiceResponseCheckTransactionStatusRequest request = new MPGwCoreBundleServiceResponseCheckTransactionStatusRequest();
			request.setLoginWS(authentication.getLogin());
			request.setPasswordWS(authentication.getPassword());
			request.setHashCodeWS(authentication.getHash());
			request.setMPGwTokenID(token.getId());

			MPGwCoreBundleServiceResponseCheckTransactionStatusResponse response = getApiPortType().wsMPGwCheckTransactionStatus(ApiEndpoint.VERSION, request);
			responseSanityCheck(response);

			return getPaymentStatusByStatusCode(response.getTransactionStatusCode());
		}
		catch (MalformedURLException | IllegalAccessException e)
		{
			throw new PaymentClientException(e);
		}
	}

	private void responseSanityCheck(Object response) throws PaymentClientException, IllegalAccessException
	{
		if (response != null)
		{
			if (response instanceof MPGwCoreBundleServiceResponsePaymentRequestResponse || response instanceof MPGwCoreBundleServiceResponseCheckTransactionStatusResponse)
			{
				String apiVersion = (String) FieldUtils.readDeclaredField(response, "apiVersion", true);

				if (!ApiEndpoint.VERSION.equals(apiVersion))
				{
					throw new PaymentClientException(PaymentClientExceptionMessage.VERSION_MISMATCH);
				}
			}
			else
			{
				throw new PaymentClientException(PaymentClientExceptionMessage.REMOTE_RESPONSE_ERROR);
			}
		}
		else
		{
			throw new PaymentClientException(PaymentClientExceptionMessage.REMOTE_RESPONSE_ERROR);
		}
	}

	private PaymentStatus getPaymentStatusByStatusCode(int code)
	{
		PaymentStatus status;

		switch (code)
		{
			case 2 :
				status = PaymentStatus.PENDING;
				break;
			case 3 :
				status = PaymentStatus.CANCELLED;
				break;
			case 4 :
				status = PaymentStatus.DONE;
				break;
			default:
				status = PaymentStatus.INITIALIZED;
		}

		return status;
	}

	private MPGwApiPortType getApiPortType() throws MalformedURLException
	{
		return new MPGwApiService(ApiEndpoint.getWsdlURL()).getMPGwApiPort();
	}
}

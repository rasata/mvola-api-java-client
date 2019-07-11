package com.codeandscale.payment.mvola.v2.client.impl;

import com.codeandscale.payment.mvola.v2.ApiAuthentication;
import com.codeandscale.payment.mvola.v2.ApiEndpoint;
import com.codeandscale.payment.mvola.v2.client.*;
import https.www_telma_net.mpgw.v2.ws.mpgwapi.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.net.MalformedURLException;

public class SoapPaymentApiClientImpl implements PaymentApiClient
{
	private final ApiAuthentication apiAuthentication;

	public SoapPaymentApiClientImpl(ApiAuthentication apiAuthentication)
	{
		this.apiAuthentication = apiAuthentication;
	}

	@Override
	public PaymentToken requestPayment(PaymentRequest paymentRequest) throws PaymentApiClientException
	{
		try
		{
			MPGwCoreBundleServiceResponsePaymentRequestRequest request = new MPGwCoreBundleServiceResponsePaymentRequestRequest();
			request.setLoginWS(apiAuthentication.getLogin());
			request.setPasswordWS(apiAuthentication.getPassword());
			request.setHashCodeWS(apiAuthentication.getHash());
			request.setShopTransactionID(paymentRequest.getId());
			request.setShopTransactionLabel(paymentRequest.getLabel());
			request.setShopTransactionAmount(paymentRequest.getAmount().intValue());

			MPGwCoreBundleServiceResponsePaymentRequestResponse response = getApiPortType().wsMPGwPaymentRequest(ApiEndpoint.VERSION, request);

			responseSanityCheck(response);

			if (!"Error".equals(response.getMPGwTokenID()))
			{
				return new PaymentToken(response.getMPGwTokenID());
			}
			else
			{
				throw new PaymentApiClientException(response.getResponseCodeDescription());
			}
		}
		catch (MalformedURLException | IllegalAccessException e)
		{
			throw new PaymentApiClientException(e);
		}
	}

	@Override
	public PaymentStatus getPaymentStatus(PaymentToken token) throws PaymentApiClientException
	{
		try
		{
			MPGwCoreBundleServiceResponseCheckTransactionStatusRequest request = new MPGwCoreBundleServiceResponseCheckTransactionStatusRequest();
			request.setLoginWS(apiAuthentication.getLogin());
			request.setPasswordWS(apiAuthentication.getPassword());
			request.setHashCodeWS(apiAuthentication.getHash());
			request.setMPGwTokenID(token.getId());

			MPGwCoreBundleServiceResponseCheckTransactionStatusResponse response = getApiPortType().wsMPGwCheckTransactionStatus(ApiEndpoint.VERSION, request);
			responseSanityCheck(response);

			return getPaymentStatusByStatusCode(response.getTransactionStatusCode());
		}
		catch (MalformedURLException | IllegalAccessException e)
		{
			throw new PaymentApiClientException(e);
		}
	}

	private void responseSanityCheck(Object response) throws PaymentApiClientException, IllegalAccessException
	{
		if (response != null)
		{
			if (response instanceof MPGwCoreBundleServiceResponsePaymentRequestResponse || response instanceof MPGwCoreBundleServiceResponseCheckTransactionStatusResponse)
			{
				String apiVersion = (String) FieldUtils.readDeclaredField(response, "apiVersion", true);

				if (!ApiEndpoint.VERSION.equals(apiVersion))
				{
					throw new PaymentApiClientException(PaymentApiClientExceptionMessage.VERSION_MISMATCH);
				}
			}
			else
			{
				throw new PaymentApiClientException(PaymentApiClientExceptionMessage.REMOTE_RESPONSE_ERROR);
			}
		}
		else
		{
			throw new PaymentApiClientException(PaymentApiClientExceptionMessage.REMOTE_RESPONSE_ERROR);
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

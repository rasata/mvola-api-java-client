package com.codeandscale.payment.mvola.v2.client.impl;

import com.codeandscale.payment.mvola.v2.ApiEndpoint;
import com.codeandscale.payment.mvola.v2.Authentication;
import com.codeandscale.payment.mvola.v2.client.*;
import https.www_telma_net.mpgw.v2.ws.mpgwapi.*;

import java.net.MalformedURLException;

public class SoapPaymentClientImpl implements PaymentClient
{
	private final Authentication authentication;

	public SoapPaymentClientImpl(Authentication authentication)
	{
		this.authentication = authentication;
	}

	@Override
	public PaymentToken requestPayment(PaymentRequest request) throws PaymentClientException
	{
		try
		{
			MPGwCoreBundleServiceResponsePaymentRequestRequest req = new MPGwCoreBundleServiceResponsePaymentRequestRequest();
			req.setLoginWS(authentication.getLogin());
			req.setPasswordWS(authentication.getPassword());
			req.setHashCodeWS(authentication.getHash());
			req.setShopTransactionID(request.getId());
			req.setShopTransactionLabel(request.getLabel());
			req.setShopTransactionAmount(request.getAmount().intValue());

			MPGwCoreBundleServiceResponsePaymentRequestResponse res = getApiPortType().wsMPGwPaymentRequest(ApiEndpoint.VERSION, req);

			return res != null ? new PaymentToken(res.getMPGwTokenID()) : null;
		}
		catch (MalformedURLException e)
		{
			throw new PaymentClientException(e);
		}
	}

	@Override
	public PaymentStatus getPaymentStatus(PaymentToken token) throws PaymentClientException
	{
		try
		{
			MPGwCoreBundleServiceResponseCheckTransactionStatusRequest req = new MPGwCoreBundleServiceResponseCheckTransactionStatusRequest();
			req.setLoginWS(authentication.getLogin());
			req.setPasswordWS(authentication.getPassword());
			req.setHashCodeWS(authentication.getHash());
			req.setMPGwTokenID(token.getId());

			MPGwCoreBundleServiceResponseCheckTransactionStatusResponse res = getApiPortType().wsMPGwCheckTransactionStatus(ApiEndpoint.VERSION, req);
			return res != null ? getPaymentStatusByStatusCode(res.getTransactionStatusCode()) : null;
		}
		catch (MalformedURLException e)
		{
			throw new PaymentClientException(e);
		}

	}

	private PaymentStatus getPaymentStatusByStatusCode(int code)
	{
		PaymentStatus status = null;

		switch (code)
		{
			case 2 :
				status = PaymentStatus.ONGOING;
				break;
			case 3 :
				status = PaymentStatus.CANCELLED;
				break;
			case 4 :
				status = PaymentStatus.DONE;
				break;
			default:
				status = PaymentStatus.PENDING;
		}

		return status;
	}

	private MPGwApiPortType getApiPortType() throws MalformedURLException
	{
		MPGwApiService apiService = new MPGwApiService(ApiEndpoint.getWsdlURL());
		return apiService.getMPGwApiPort();
	}
}

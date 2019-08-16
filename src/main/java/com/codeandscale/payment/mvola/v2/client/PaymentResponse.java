package com.codeandscale.payment.mvola.v2.client;

import com.codeandscale.payment.mvola.v2.ApiEndpoint;
import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;

@Data
public class PaymentResponse
{
    private String token;

    public PaymentResponse()
    {
    }

    public PaymentResponse(String token)
    {
        this.token = token;
    }

    public URL getPaymentURL()
    {
        try
        {
            return new URL(ApiEndpoint.TRANSACTION + token);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

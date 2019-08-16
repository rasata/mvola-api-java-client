package com.codeandscale.payment.mvola.v2.client;

import lombok.Data;

@Data
public class PaymentToken
{
    private String id;

    public PaymentToken(String id)
    {
        this.id = id;
    }

    public PaymentToken()
    {
    }
}

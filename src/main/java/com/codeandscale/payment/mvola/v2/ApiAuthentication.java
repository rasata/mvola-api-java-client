package com.codeandscale.payment.mvola.v2;

import lombok.Data;

@Data
public class ApiAuthentication
{
	private String login;
	private String password;
	private String hash;

	public ApiAuthentication(String login, String password, String hash)
	{
		this.login = login;
		this.password = password;
		this.hash = hash;
	}

	public ApiAuthentication()
	{
	}
}

package com.codeandscale.payment.mvola.v2;

import lombok.Data;

@Data
public class Authentication
{
	private String login;
	private String password;
	private String hash;

	public Authentication(String login, String password, String hash)
	{
		this.login = login;
		this.password = password;
		this.hash = hash;
	}
}

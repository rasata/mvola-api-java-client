package com.codeandscale.payment.mvola.v2;

import java.net.MalformedURLException;
import java.net.URL;

public class ApiEndpoint
{
	public static final String VERSION = "2.0.0";
	public static final String BASE = "https://www.telma.net/mpgw/v2";
	public static final String TRANSACTION = BASE + "/transaction/";
	public static final String WS = BASE + "/ws/MPGwApi";

	public static URL getWsdlURL() throws MalformedURLException
	{
		return new URL(WS);
	}
}

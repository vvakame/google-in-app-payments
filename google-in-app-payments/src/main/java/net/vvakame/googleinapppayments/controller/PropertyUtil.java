package net.vvakame.googleinapppayments.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	private PropertyUtil() {
	}

	static final Properties PROPERTIES;

	static {
		try {
			PROPERTIES = new Properties();
			InputStream is = PreController.class
					.getResourceAsStream("/payments.properties");
			PROPERTIES.load(is);
		} catch (IOException e) {
			throw new RuntimeException("/payments.properties required", e);
		}
	}

	static final String ISSUER = PROPERTIES.getProperty("ISSUER");
	static final String SIGNING_KEY = PROPERTIES.getProperty("SIGNING_KEY");

	public static String getIssuer() {
		return ISSUER;
	}

	public static String getSigningKey() {
		return SIGNING_KEY;
	}
}

package net.vvakame.googleinapppayments.controller;

import java.security.SignatureException;
import java.util.List;
import java.util.logging.Logger;

import net.oauth.jsontoken.Checker;
import net.oauth.jsontoken.Clock;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.SystemClock;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.StringUtil;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

public class PostController extends Controller {

	static final Logger logger = Logger.getLogger(PostController.class
			.getName());

	static final String ISSUER = PropertyUtil.getIssuer();
	static final String SIGNING_KEY = PropertyUtil.getSigningKey();

	static Clock clock = new SystemClock();
	static VerifierProviders locators;

	static {
		try {
			final Verifier hmacVerifier = new HmacSHA256Verifier(
					SIGNING_KEY.getBytes());

			VerifierProvider hmacLocator = new VerifierProvider() {
				@Override
				public List<Verifier> findVerifier(String signerId, String keyId) {
					return Lists.newArrayList(hmacVerifier);
				}
			};

			locators = new VerifierProviders();
			locators.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Navigation run() throws Exception {
		String jwt = asString("jwt");
		final boolean test;
		if (StringUtil.isEmpty(asString("test"))) {
			test = false;
		} else {
			test = true;
		}

		if (jwt == null) {
			response.setStatus(400);
			return null;
		}

		logger.info(jwt);

		JsonTokenParser parser = new JsonTokenParser(clock, locators,
				new Checker() {
					@Override
					public void check(JsonObject payload)
							throws SignatureException {
					}
				});

		JsonToken token;
		if (test) {
			token = parser.deserialize(jwt);
		} else {
			token = parser.verifyAndDeserialize(jwt);
			logger.info("signature verified.");
		}

		JsonObject json = token.getPayloadAsJsonObject();

		if (json.get("response") == null
				|| json.getAsJsonObject("response").get("orderId") == null) {
			response.setStatus(400);
			return null;
		}

		String orderId = json.getAsJsonObject("response").get("orderId")
				.getAsString();

		response.getWriter().write(orderId);
		response.flushBuffer();

		return null;
	}
}

package net.vvakame.googleinapppayments.controller;

import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Locale;

import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.sf.json.JSONObject;

import org.joda.time.Instant;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.gson.JsonObject;

public class PreController extends Controller {

	static final String ISSUER = PropertyUtil.getIssuer();
	static final String SIGNING_KEY = PropertyUtil.getSigningKey();

	@Override
	protected Navigation run() throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		PrintWriter writer = response.getWriter();

		JSONObject json = new JSONObject();
		json.put("jwt", getJWT());

		writer.write(json.toString());

		response.flushBuffer();

		return null;
	}

	String getJWT() throws InvalidKeyException, SignatureException {
		return createToken().serializeAndSign();
	}

	JsonToken createToken() throws InvalidKeyException {
		// Configure JSON token
		JsonToken token = createHeader();
		// Configure request object
		JsonObject request;
		{
			String itemName = "vvakame";
			String itemDescription = "super hakka.";
			double price = 0.50;
			String currencyCode = "USD";
			String sellerData = ":-P";

			request = createRequest(itemName, itemDescription, price,
					currencyCode, sellerData);
		}

		JsonObject payload = token.getPayloadAsJsonObject();
		payload.add("request", request);
		return token;
	}

	JsonToken createHeader() throws InvalidKeyException {
		// Current time and signing algorithm
		Calendar cal = Calendar.getInstance();
		HmacSHA256Signer signer = new HmacSHA256Signer(ISSUER, null,
				SIGNING_KEY.getBytes());
		// Configure JSON token
		JsonToken token = new JsonToken(signer);
		token.setAudience("Google");
		token.setParam("typ", "google/payments/inapp/item/v1");
		token.setIssuedAt(new Instant(cal.getTimeInMillis()));
		token.setExpiration(new Instant(cal.getTimeInMillis() + 60000L));

		return token;
	}

	JsonObject createRequest(String itemName, String itemDescription,
			double price, String currencyCode, String sellerData) {

		// price は 9.98 とかでもいいけど、小数点以下は2桁まで
		String priceStr = String.format(Locale.ENGLISH, "%.2f", price);

		// currencyCode は 今のところ USD だけぽい
		if (!"USD".equals(currencyCode)) {
			throw new IllegalArgumentException();
		}

		// sellerData は自由書式 処理終了後に使うデータ 200文字まで
		if (sellerData != null && sellerData.length() > 200) {
			throw new IllegalArgumentException();
		}

		JsonObject request = new JsonObject();
		request.addProperty("name", itemName);
		request.addProperty("description", itemDescription);
		request.addProperty("price", priceStr);
		request.addProperty("currencyCode", currencyCode);
		request.addProperty("sellerData", sellerData);

		return request;
	}
}

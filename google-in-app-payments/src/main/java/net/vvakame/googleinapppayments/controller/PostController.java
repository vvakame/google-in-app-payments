package net.vvakame.googleinapppayments.controller;

import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.repackaged.org.json.JSONObject;

public class PostController extends Controller {

	static final Logger logger = Logger.getLogger(PostController.class
			.getName());

	@Override
	protected Navigation run() throws Exception {
		String jwt = asString("jwt");

		if (jwt == null) {
			response.setStatus(400);
			return null;
		}

		logger.info(jwt);

		JSONObject json = new JSONObject(jwt);

		if (json.getJSONObject("response") == null
				|| json.getJSONObject("response").getString("orderId") == null) {
			response.setStatus(400);
			return null;
		}

		String orderId = json.getJSONObject("response").getString("orderId");

		response.getWriter().write(orderId);
		response.flushBuffer();

		return null;
	}
}

package net.vvakame.googleinapppayments.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PostControllerTest extends ControllerTestCase {

	static final String PATH = "/post";

	@Test
	public void test() throws Exception {
		tester.request.setMethod("POST");
		tester.request
				.addParameter(
						"jwt",
						"{ \"iss\" : \"Google\",\"aud\" : \"1337133713371337\",\"typ\" : \"google/payments/inapp/item/v1/postback/buy\",\"iat\" : \"1309988959\",\"exp\" : \"1409988959\",\"request\" :{\"name\" : \"Piece of Cake\",\"description\" : \"Virtual chocolate cake to fill your virtual tummy\",\"price\" : \"10.50\",\"currencyCode\" : \"USD\",\"sellerData\" : \"user_id:1224245,offer_code:3098576987,affiliate:aksdfbovu9j\"},\"response\": {\"orderId\": \"3485709183457474939449\"}}");

		tester.start(PATH);

		assertThat(tester.getController(), instanceOf(PostController.class));
		assertThat(tester.response.getStatus(), is(200));

		String outputAsString = tester.response.getOutputAsString();

		assertThat(outputAsString, is("3485709183457474939449"));

		System.out.println(outputAsString);
	}

	LocalServiceTestHelper helper;

	@Override
	public void setUp() throws Exception {
		LocalDatastoreServiceTestConfig dsConfig = new LocalDatastoreServiceTestConfig();
		dsConfig.setNoStorage(true);
		dsConfig.setNoIndexAutoGen(true);
		helper = new LocalServiceTestHelper(dsConfig);
		helper.setUp();
		super.setUp();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		helper.tearDown();
	}
}

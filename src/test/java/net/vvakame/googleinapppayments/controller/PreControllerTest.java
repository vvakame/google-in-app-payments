package net.vvakame.googleinapppayments.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PreControllerTest extends ControllerTestCase {

	static final String PATH = "/pre";

	@Test
	public void test() throws Exception {
		tester.start(PATH);

		assertThat(tester.getController(), instanceOf(PreController.class));
		assertThat(tester.response.getStatus(), is(200));

		String outputAsString = tester.response.getOutputAsString();

		JSONObject json = new JSONObject(outputAsString);
		assertThat(json.getString("jwt"), notNullValue());

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

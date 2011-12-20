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
						"eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJHb29nbGUiLCJhdWQiOiIwODU3ODA2NTUwMDAzNTcxODM1OCIsInR5cCI6Imdvb2dsZS9wYXltZW50cy9pbmFwcC9pdGVtL3YxL3Bvc3RiYWNrL2J1eSIsImlhdCI6MTMyNDM2MDM2MCwiZXhwIjoxMzI0MzYwMzY1LCJyZXF1ZXN0Ijp7ImN1cnJlbmN5Q29kZSI6IlVTRCIsInByaWNlIjoiMC41IiwibmFtZSI6InZ2YWthbWUiLCJzZWxsZXJEYXRhIjoidXNlcl9pZDoxMjI0MjQ1LG9mZmVyX2NvZGU6MzA5ODU3Njk4NyxhZmZpbGlhdGU6YWtzZGZib3Z1OWoiLCJkZXNjcmlwdGlvbiI6InN1cGVyIGhha2thLiJ9LCJyZXNwb25zZSI6eyJvcmRlcklkIjoiMTI1NDI0NTc4MTc0MDIwMjM2OTAuQy4xNjg4MTE1MTQyMTYwMTIxIn19.uKw-PesbY-cnoHo9hQ8RCDVWHbC30Ejjnr0Rkkow3lk");

		tester.start(PATH);

		assertThat(tester.getController(), instanceOf(PostController.class));
		assertThat(tester.response.getStatus(), is(200));

		String outputAsString = tester.response.getOutputAsString();

		assertThat(outputAsString,
				is("12542457817402023690.C.1688115142160121"));

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

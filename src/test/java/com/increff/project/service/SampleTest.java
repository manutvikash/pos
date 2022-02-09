package com.increff.project.service;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

public class SampleTest {

	@Test
	public void testFiles() {
		InputStream is = null;
		is = SampleTest.class.getResourceAsStream("/brandSample.tsv");
		assertNotNull(is);
		is = SampleTest.class.getResourceAsStream("/inventorySample.tsv");
		assertNotNull(is);
		is = SampleTest.class.getResourceAsStream("/productSample.tsv");
		assertNotNull(is);
	}

}

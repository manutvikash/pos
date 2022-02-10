package com.increff.project.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.project.spring.AbstractUnitTest;

public class ErrorMessageTest extends AbstractUnitTest {
	@Test
	public void testErrorMessage() {
		MessageData error_message = new MessageData();
		error_message.setMessage("Api Exception");
		assertEquals(error_message.getMessage(),"Api Exception");
	}
}

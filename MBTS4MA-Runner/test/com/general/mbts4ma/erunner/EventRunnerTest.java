package com.general.mbts4ma.erunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class EventRunnerTest {

	@Test
	public void test01() {
		EventRunner runner = new EventRunner();

		boolean actual = runner.executeCompleteEventSequence(new MyAdaptor(), "a,b,c");

		assertTrue(actual);
	}

	@Test
	public void test02() {
		EventRunner runner = new EventRunner();

		boolean actual = runner.executeCompleteEventSequence(new MyAdaptor(), "a,b,c,e");

		assertFalse(actual);
	}

	@Test
	public void test03() {
		EventRunner runner = new EventRunner();

		boolean actual = runner.executeCompleteEventSequence(new MyAdaptor(), "a,b,c,f");

		assertFalse(actual);
	}

	@Test
	public void test04() {
		EventRunner runner = new EventRunner();

		boolean actual = runner.executeCompleteEventSequence(new MyAdaptor(), "a,a,d#R1,d#R2,c");

		assertTrue(actual);
	}

}

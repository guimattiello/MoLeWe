package com.general.mbts4ma.erunner;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventRunner {

	protected static String EMPTY = "EMPTY";
	protected static Logger logger = Logger.getLogger(EventRunner.class.getName());
	MethodSearcher methodSearcher;

	public EventRunner() {
		logger.setLevel(Level.INFO);
	}

	private String getRuleLabel(String eventLabel) {
		String tokens[] = eventLabel.split("#");

		if (tokens.length > 1) {
			return tokens[1];
		} else {
			return EMPTY;
		}
	}

	public boolean executeCompleteEventSequence(Object adaptor, String testSequence) {
		String eventLabels[] = testSequence.split(",");

		for (String eventLabel : eventLabels) {
			String eventRule = this.getRuleLabel(eventLabel);

			if (!eventRule.equals(EMPTY)) {
				eventLabel = this.getRule(eventLabel);
			}

			logger.info(eventLabel + "#" + eventRule);

			if (!this.executeEvent(adaptor, eventLabel, eventRule)) {
				logger.info("Faulty event!");

				return false;
			}
		}

		return true;
	}

	private String getRule(String eventLabel) {
		String tokens[] = eventLabel.split("#");

		return tokens[0];
	}

	public boolean executeEvent(Object adaptor, String eventLabel, String eventRule) {
		this.methodSearcher = new MethodSearcher(adaptor.getClass().getMethods());

		Method method = null;

		if (eventRule.equals("EMPTY")) {
			method = this.methodSearcher.getMethodsEventAnnotatedWith(eventLabel);
		} else {
			method = this.methodSearcher.getMethodsEventAnnotatedWith(eventLabel, eventRule);
		}

		if (method == null) {
			logger.info("Event " + eventLabel + " (rule: " + eventRule + ") cannot be found in the adaptor " + adaptor.getClass().getName() + ".");

			return false;
		}

		try {
			return (Boolean) method.invoke(adaptor);
		} catch (Exception e) {
			logger.info(e.getMessage());

			return false;
		}
	}
}

package com.general.mbts4ma.event;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.general.mbts4ma.erunner.MethodSearcher;

/**
 * Created by andreendo on 28/06/2017.
 */

public class EventRunner {
	
	protected static String EMPTY = "EMPTY";
	protected static Logger logger = Logger.getLogger(EventRunner.class.getName());
	MethodSearcher methodSearcher;

	public EventRunner() {
		logger.setLevel(Level.INFO);
	}
	
	private String getRuleLabel(String eventLabel) {
		String tokens[] = eventLabel.split("#");
		if(tokens.length > 1)
			return tokens[1];
		else
			return EMPTY;
	}
	
	public boolean executeCompleteEventSequence(Class adapterClass, String testSequence) {
        Object adaptor = null;
        try {
            adaptor = adapterClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
		String eventLabels[] = testSequence.split(",");
		for(String eventLabel : eventLabels) {
            eventLabel = eventLabel.trim();
			String eventRule = getRuleLabel(eventLabel);
			if(! eventRule.equals(EMPTY))
				eventLabel = getRule(eventLabel);
			
			logger.info(eventLabel + "#" + eventRule);
			if( ! executeEvent(adaptor, eventLabel, eventRule) ) {
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
		methodSearcher = new MethodSearcher(adaptor.getClass().getMethods());
		
		Method method = null;
		if(eventRule.equals("EMPTY"))
			method = methodSearcher.getMethodsEventAnnotatedWith(eventLabel);
		else
			method = methodSearcher.getMethodsEventAnnotatedWith(eventLabel, eventRule);
		
		if(method == null) {
			logger.info("Event "+eventLabel+" (rule: "+eventRule+") cannot be found in the adapter "+adaptor.getClass().getName()+".");
			return false;			
		}
		
		try {
			return (Boolean) method.invoke(adaptor);
		} 
		catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}
}
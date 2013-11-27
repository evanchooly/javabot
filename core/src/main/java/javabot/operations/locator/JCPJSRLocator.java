package javabot.operations.locator;

import com.google.inject.ImplementedBy;
import javabot.operations.locator.impl.JCPJSRLocatorImpl;

@ImplementedBy(JCPJSRLocatorImpl.class)
public interface JCPJSRLocator extends Locator {
	String findInformation(int jsr);
}

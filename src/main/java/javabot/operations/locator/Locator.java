package javabot.operations.locator;

import java.util.Map;

public interface Locator {
	Map<String, String> locate(Map<String, String> inputs);
}

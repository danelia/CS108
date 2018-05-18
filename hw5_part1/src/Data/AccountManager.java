package Data;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
	Map<String, String> list;

	public AccountManager() {
		list = new HashMap<String, String>();
		add("Patrick", "1234");
		add("Molly", "FloPup");
	}

	public boolean add(String key, String value) {
		if (list.containsKey(key))
			return false;
		list.put(key, value);
		return true;
	}

	public boolean check(String key, String value) {
		return (list.containsKey(key) && list.get(key).equals(value));
	}
}

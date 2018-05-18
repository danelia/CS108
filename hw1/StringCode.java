package assign1;

import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run. A a run is a
	 * series of adajcent chars that are the same.
	 * 
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int maxCount = 0;
		int currCount = 0;
		for (int i = 0; i < str.length(); i++) {
			currCount++;
			if (currCount > maxCount)
				maxCount = currCount;
			if (i != str.length() - 1 && str.charAt(i) != str.charAt(i + 1))
				currCount = 0;
		}
		return maxCount;
	}

	/**
	 * Given a string, for each digit in the original string, replaces the digit
	 * with that many occurrences of the character following. So the string
	 * "a3tx2z" yields "attttxzzz".
	 * 
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String result = "";
		char curr;
		for (int i = 0; i < str.length(); i++) {
			curr = str.charAt(i);
			if (i == str.length() - 1) {
				if (!Character.isDigit(curr))
					result += curr;
				break;
			}
			if (Character.isDigit(curr)) {
				int currNum = Character.getNumericValue(curr);
				for (int j = 0; j < currNum; j++) {
					result += str.charAt(i + 1);
				}
				continue;
			}
			result += curr;
		}
		return result;
	}

	/**
	 * Given 2 strings, consider all the substrings within them of length len.
	 * Returns true if there are any such substrings which appear in both
	 * strings. Compute this in linear time using a HashSet. Len will be 1 or
	 * more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (len > a.length() || len > b.length())
			return false;

		HashSet<String> subStrings = new HashSet<String>();

		for (int i = 0; i <= a.length() - len; i++)
			subStrings.add(a.substring(i, i + len));

		for (int i = 0; i <= b.length() - len; i++)
			if (subStrings.contains(b.substring(i, i + len)))
				return true;

		return false;
	}
}

package assign1;

import java.util.*;

public class Appearances {

	/**
	 * Returns the number of elements that appear the same number of times in
	 * both collections. Static method. (see handout).
	 * 
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int count = 0;

		Map<T, Integer> map = new HashMap<T, Integer>();

		for (T elem : a)
			if (map.containsKey(elem))
				map.put(elem, map.get(elem) + 1);
			else
				map.put(elem, 1);

		for (T elem : b)
			if (map.containsKey(elem))
				map.put(elem, map.get(elem) - 1);

		for (Integer elem : map.values())
			if (elem == 0)
				count++;

		return count;
	}

}

/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/
package assign1;

import java.util.*;

public class Taboo<T> {

	private List<T> taboo;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * 
	 * @param rules
	 *            rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		taboo = rules;
	}

	/**
	 * Returns the set of elements which should not follow the given element.
	 * 
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		Set<T> result = new HashSet<T>();

		for (int i = 0; i < taboo.size() - 1; i++)
			if (taboo.get(i) == elem && taboo.get(i + 1) != null)
				result.add(taboo.get(i + 1));

		if (result.isEmpty())
			return Collections.emptySet();

		return result;
	}

	/**
	 * Removes elements from the given list that violate the rules (see
	 * handout).
	 * 
	 * @param list
	 *            collection to reduce
	 */
	public void reduce(List<T> list) {
		for (int i = 0; i < list.size() - 1;) {
			Set<T> bla = noFollow(list.get(i));
			if (bla.contains(list.get(i + 1)))
				list.remove(i + 1);
			else
				i++;
		}
	}
}

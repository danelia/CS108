// TabooTest.java
// Taboo class tests -- nothing provided.
package assign1;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class TabooTest {

	@Test
	public void tabooTest1(){
		List<Character> list = Arrays.asList('a', 'c', 'a', 'b');
		Taboo<Character> taboo = new Taboo<Character>(list);
		
		List<Character> result = new ArrayList<Character>(taboo.noFollow('a'));
		List<Character> expected = Arrays.asList('b', 'c');
		List<Character> ReduceList = new ArrayList<Character>(Arrays.asList('a', 'c', 'b', 'x', 'c', 'a'));
		List<Character> expectedReduce = Arrays.asList('a', 'x', 'c');
		
		assertTrue(result.equals(expected));
		
		taboo.reduce(ReduceList);
		
		assertTrue(expectedReduce.equals(ReduceList));
		
	}
	
	@Test
	public void tabooTest2(){
		List<Integer> list = Arrays.asList(7, 27, null, 27, 2, 27, 72);
		Taboo<Integer> taboo = new Taboo<Integer>(list);
		
		List<Integer> result = new ArrayList<Integer>(taboo.noFollow(27));
		List<Integer> expected = Arrays.asList(2, 72);
		List<Integer> ReduceList = new ArrayList<Integer>(Arrays.asList(27, 2, 127, 72, 27, 2, 7));
		List<Integer> expectedReduce = Arrays.asList(27, 127, 72, 27, 7);
		
		assertTrue(result.equals(expected));
		
		taboo.reduce(ReduceList);
		
		assertTrue(expectedReduce.equals(ReduceList));
	}
}

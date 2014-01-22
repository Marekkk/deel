package org.deel.test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class SimpleJavaTest {

	@Test
	public void listTest() throws Exception {
		List<Integer> list = new LinkedList<Integer>();
		list.add(0);
		
		for (ListIterator<Integer> i = list.listIterator();i.hasNext(); ) {
			Integer e = i.next();
			if (e == 0)
				list.add(1);
			System.out.println(e);
		}
	}

}

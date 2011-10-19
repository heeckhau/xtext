/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.tests.lib;

import static com.google.common.collect.Lists.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.xtext.xbase.lib.Functions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import junit.framework.TestCase;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class IterableExtensionsTest extends TestCase {

	public void testFindLast() {
		List<String> list = Lists.newArrayList("a", "b", "c");
		String last = IterableExtensions.findLast(list, new Functions.Function1<String, Boolean>() {
			public Boolean apply(String p) {
				return true;
			}
		});
		assertEquals("c", last);
	}
	
	public void testLast() {
		Set<String> emptySet = Sets.newTreeSet();
		String last = IterableExtensions.last(emptySet);
		assertNull(last);
	}
	
	public void testJoinWithNull() {
		List<String> list = Lists.newArrayList("a", null, "c");
		String string = IterableExtensions.join(list, ",");
		assertEquals("a,null,c", string);
	}
	
	public void testSortBy() throws Exception {
		List<? extends CharSequence> list = newArrayList("foo","bar","baz");
		List<? extends CharSequence> sorted = IterableExtensions.sortBy(list, new Functions.Function1<CharSequence, String>() {
			public String apply(CharSequence p) {
				return p.toString();
			}
		});
		assertNotSame(list, sorted);
		assertEquals(sorted, newArrayList("bar","baz","foo"));
	}
	
	public void testFilterNull() throws Exception {
		Iterator<String> iter = IterableExtensions.filterNull(newArrayList("foo", null, "bar")).iterator();
		assertEquals("foo", iter.next());
		assertEquals("bar", iter.next());
		assertFalse(iter.hasNext());
	}
	
	public void testJoinWithBeforeAndAfter() throws Exception {
		ArrayList<String> list = newArrayList("foo", "bar");
		ArrayList<String> singletonList = newArrayList("foo");
		ArrayList<String> emptylist = new ArrayList<String>();
		
		final Functions.Function1<String, String> function = new Functions.Function1<String, String>() {
			public String apply(String p) {
				return p;
			}
		};
		assertEquals("<foo,bar>", IterableExtensions.join(list, "<", ",", ">", function));
		assertEquals("<foo>", IterableExtensions.join(singletonList, "<", ",", ">", function));
		assertEquals("", IterableExtensions.join(emptylist, "<", ",", ">", function));
		
		assertEquals("foo,bar>", IterableExtensions.join(list, null, ",", ">", function));
		assertEquals("foo>", IterableExtensions.join(singletonList, null, ",", ">", function));
		assertEquals("", IterableExtensions.join(emptylist, null, ",", ">", function));
		
		assertEquals("<foobar>", IterableExtensions.join(list, "<", null, ">", function));
		assertEquals("<foo>", IterableExtensions.join(singletonList, "<", null, ">", function));
		assertEquals("", IterableExtensions.join(emptylist, "<", null, ">", function));
		
		assertEquals("<foo,bar", IterableExtensions.join(list, "<", ",", null, function));
		assertEquals("<foo", IterableExtensions.join(singletonList, "<", ",", null, function));
		assertEquals("", IterableExtensions.join(emptylist, "<", ",", null, function));
	}
}

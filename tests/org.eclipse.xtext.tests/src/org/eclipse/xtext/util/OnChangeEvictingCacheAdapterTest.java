/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.util;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.xtext.util.OnChangeEvictingCache.CacheAdapter;

/**
 * @author Sven Efftinge - Initial contribution and API
 */
public class OnChangeEvictingCacheAdapterTest extends TestCase {
	String key = "foo";
	String value = "bar";
	
	public void testAdapterIsCleared() throws Exception {
		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EClass eClass = factory.createEClass();
		Resource resource = new ResourceImpl();
		resource.getContents().add(eClass);
		CacheAdapter ca = new OnChangeEvictingCache().getOrCreate(resource);
		setValue(ca);
		EAttribute attribute = factory.createEAttribute();
		assertIsSet(ca);
		eClass.getEStructuralFeatures().add(attribute);
		assertIsNull(ca);
		setValue(ca);
		attribute.setName("Foo");
		assertIsNull(ca);
		setValue(ca);
	}
	
	private void assertIsSet(CacheAdapter ca) {
		assertEquals(value, ca.get(key));
	}

	private void assertIsNull(CacheAdapter ca) {
		assertNull(ca.get(key));
	}

	private void setValue(CacheAdapter ca) {
		ca.set(key, value);
		assertIsSet(ca);
	}
}

/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.common.types.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author Sven Efftinge - Initial contribution and API
 */
public class JavaReflectAccessTest extends TestCase {
	private ResourceSet resourceSet;
	private ClasspathTypeProvider typeProvider;

	private JvmDeclaredType getType(Class<?> class1) {
		return (JvmDeclaredType) typeProvider.findTypeByName(class1.getName());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resourceSet = new ResourceSetImpl();
		typeProvider = new ClasspathTypeProvider(getClass().getClassLoader(), resourceSet, null);
	}

	@Override
	protected void tearDown() throws Exception {
		resourceSet = null;
		typeProvider = null;
		super.tearDown();
	}

	public void testGetRawType_1() throws Exception {
		JvmDeclaredType type = getType(String.class);
		assertEquals(String.class, getJavaReflectAccess().getRawType(type));
	}

	public void testGetRawType_2() throws Exception {
		JvmDeclaredType type = getType(List.class);
		assertEquals(List.class, getJavaReflectAccess().getRawType(type));
	}

	public void testGetMethod_1() throws Exception {
		JvmDeclaredType type = getType(List.class);
		JvmMember addMethod = Iterables.find(type.getMembers(), new Predicate<JvmMember>() {
			public boolean apply(JvmMember input) {
				if (input instanceof JvmOperation) {
					return input.getSimpleName().equals("add") && ((JvmOperation) input).getParameters().size() == 1;
				}
				return false;
			}
		});

		Method method = List.class.getDeclaredMethod("add", Object.class);
		assertEquals(method, getJavaReflectAccess().getMethod((JvmOperation)addMethod));
	}

	public void testGetMethod_2() throws Exception {
		JvmDeclaredType type = getType(X.class);
		JvmMember addMethod = Iterables.find(type.getMembers(), new Predicate<JvmMember>() {
			public boolean apply(JvmMember input) {
				if (input instanceof JvmOperation) {
					return input.getSimpleName().equals("a");
				}
				return false;
			}
		});

		Method method = X.class.getDeclaredMethod("a", Comparable.class, CharSequence.class);
		assertEquals(method, getJavaReflectAccess().getMethod((JvmOperation)addMethod));
	}
	
	public void testGetConstructor_1() throws Exception {
		JvmGenericType type = (JvmGenericType) getType(X.class);
		JvmMember firstConstructor = Iterables.find(type.getDeclaredConstructors(),new Predicate<JvmConstructor>() {
			public boolean apply(JvmConstructor input) {
				return input.getParameters().size() == 1;
			}
		});
		Constructor<?> expectation = X.class.getDeclaredConstructor(int.class);
		assertEquals(expectation, getJavaReflectAccess().getConstructor((JvmConstructor)firstConstructor));
	}
	
	public void testGetConstructor_2() throws Exception {
		JvmGenericType type = (JvmGenericType) getType(X.class);
		JvmMember secondConstructor = Iterables.find(type.getDeclaredConstructors(),new Predicate<JvmConstructor>() {
			public boolean apply(JvmConstructor input) {
				return input.getParameters().size() == 2;
			}
		});
		Constructor<?> expectation = X.class.getDeclaredConstructor(Object.class, List.class);
		assertEquals(expectation, getJavaReflectAccess().getConstructor((JvmConstructor)secondConstructor));
	}

	public void testGetField_1() throws Exception {
		JvmDeclaredType type = getType(Y.class);
		JvmMember addMethod = Iterables.find(type.getMembers(), new Predicate<JvmMember>() {
			public boolean apply(JvmMember input) {
				if (input instanceof JvmField) {
					return input.getSimpleName().equals("z");
				}
				return false;
			}
		});

		Field field = Y.class.getDeclaredField("z");

		assertEquals(field, getJavaReflectAccess().getField((JvmField) addMethod));
	}

	protected JavaReflectAccess getJavaReflectAccess() {
		JavaReflectAccess javaReflectAccess = new JavaReflectAccess();
		javaReflectAccess.setClassLoader(getClass().getClassLoader());
		return javaReflectAccess;
	}

	public static class X<T extends Comparable<CharSequence>> {
		public X(int i) {}
		public X(Object o, List<String> list) {}
		public <T2 extends CharSequence> void a(T t, T2 t2) {
		}
	}

	public static class Y<T extends Comparable<CharSequence>> {
		T z;
	}
}

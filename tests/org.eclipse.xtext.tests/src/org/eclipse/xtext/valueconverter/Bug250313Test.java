/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.valueconverter;

import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.ParserTestHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.valueconverter.Bug250313RuntimeModule.Converters;

import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Bug250313Test extends AbstractXtextTests {

	private ParserTestHelper helper;
	private INode node;
	private String lexerRule;
	private int convertCallCount;
	private String string;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(new Bug250313StandaloneSetup() {
			@Override
			public Injector createInjector() {
				return Guice.createInjector(new MyBug250313RuntimeModule(Bug250313Test.this));
			}
		});
		helper = new ParserTestHelper(getResourceFactory(), getParser(), get(Keys.RESOURCE_SET_KEY),getCurrentFileExtension());
	}

	@Override
	protected void tearDown() throws Exception {
		string = null;
		node = null;
		lexerRule = null;
		convertCallCount = 0;
		super.tearDown();
	}
	
	@Override
	protected boolean shouldTestSerializer(XtextResource resource) {
		return false;
	}

	public static class MyBug250313RuntimeModule extends Bug250313RuntimeModule {

		private final Bug250313Test test;

		public MyBug250313RuntimeModule(Bug250313Test test) {
			this.test = test;
		}

		@Override
		public void configure(Binder binder) {
			super.configure(binder);
			binder.bind(Bug250313Test.class).toInstance(test);
		}

		@Override
		public Class<? extends IValueConverterService> bindIValueConverterService() {
			return MyConverterService.class;
		}

	}

	public static class MyConverterService implements IValueConverterService {

		@Inject
		private Converters delegate;

		@Inject
		private Bug250313Test test;

		public String toString(Object value, String lexerRule) {
			return delegate.toString(value, lexerRule);
		}

		public Object toValue(String string, String lexerRule, INode node) throws ValueConverterException {
			test.toValueCalled(string, lexerRule, node);
			return delegate.toValue(string, lexerRule, node);
		}

	}

	public void toValueCalled(String string, String lexerRule, INode node) {
		this.string = string;
		this.node = node;
		this.lexerRule = lexerRule;
		this.convertCallCount++;
	}

	@Override
	public XtextResource doGetResource(InputStream in, URI uri) throws Exception {
		return helper.getResourceFromStream(in);
	}
	
	public void testSTRINGConversion_01() throws Exception {
		EObject model = getModel("1 'str'");
		assertWithXtend("'str'", "this.value", model);
		assertNotNull(lexerRule);
		assertEquals("STRING", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("'str'", string);
		assertEquals(1, convertCallCount);
	}

	public void testSTRINGConversion_02() throws Exception {
		EObject model = getModel("'str'");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("STRING", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("'str'", string);
		assertEquals(1, convertCallCount);
	}

	public void testSTRINGConversion_03() throws Exception {
		EObject model = getModel("2 'str'");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("STRING", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("'str'", string);
		assertEquals(1, convertCallCount);
	}

	public void testSTRINGConversion_04() throws Exception {
		EObject model = getModel("1+ 'str'");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertNotNull(lexerRule);
		assertEquals("STRING", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("'str'", string);
		assertEquals(1, convertCallCount);
	}

	public void testSTRINGConversion_05() throws Exception {
		EObject model = getModel("2+ 'str'");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("STRING", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("'str'", string);
		assertEquals(1, convertCallCount);
	}

	public void testIDConversion_01() throws Exception {
		EObject model = getModel("1 str");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("ID", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("str", string);
		assertEquals(1, convertCallCount);
	}

	public void testIDConversion_02() throws Exception {
		EObject model = getModel("1 ^str");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("ID", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("^str", string);
		assertEquals(1, convertCallCount);
	}

	public void testIDConversion_03() throws Exception {
		EObject model = getModel("str");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("ID", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("str", string);
		assertEquals(1, convertCallCount);
	}

	public void testIDConversion_04() throws Exception {
		EObject model = getModel("^str");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("ID", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("^str", string);
		assertEquals(1, convertCallCount);
	}

	public void testIDConversion_05() throws Exception {
		EObject model = getModel("1+ str");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("ID", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("str", string);
		assertEquals(1, convertCallCount);
	}

	public void testIDConversion_06() throws Exception {
		EObject model = getModel("1+ ^str");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("ID", lexerRule);
		assertTrue(node instanceof ILeafNode);
		assertEquals("^str", string);
		assertEquals(1, convertCallCount);
	}

	public void testDatatypeConversion_01() throws Exception {
		EObject model = getModel("1 foo - bar");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("Datatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(6, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testDatatypeConversion_02() throws Exception {
		EObject model = getModel("foo - bar");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("Datatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(5, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testDatatypeConversion_03() throws Exception {
		EObject model = getModel("3 foo - bar");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("Datatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(6, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testDatatypeConversion_04() throws Exception {
		EObject model = getModel("1+ foo - bar");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("Datatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(6, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testDatatypeConversion_05() throws Exception {
		EObject model = getModel("3+ foo - bar");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("Datatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(6, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_01() throws Exception {
		EObject model = getModel("1 zonk + foo - bar");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(5, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk + foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_02() throws Exception {
		EObject model = getModel("1 zonk +");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(4, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk +", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_03() throws Exception {
		EObject model = getModel("zonk + foo - bar");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(4, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk + foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_04() throws Exception {
		EObject model = getModel("zonk +");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(3, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk +", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_05() throws Exception {
		EObject model = getModel("4 zonk + foo - bar");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(5, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk + foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_06() throws Exception {
		EObject model = getModel("4 zonk +");
		assertWithXtend("'str'", "this.value", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(4, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk +", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_07() throws Exception {
		EObject model = getModel("1+ zonk + foo - bar");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(5, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk + foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_08() throws Exception {
		EObject model = getModel("1+ zonk +");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(4, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk +", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_09() throws Exception {
		EObject model = getModel("4+ zonk + foo - bar");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(5, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk + foo - bar", string);
		assertEquals(1, convertCallCount);
	}

	public void testNestedDatatypeConversion_10() throws Exception {
		EObject model = getModel("4+ zonk +");
		assertWithXtend("{'str'}", "this.multiValue", model);
		assertEquals("NestedDatatype", lexerRule);
		assertTrue(node instanceof ICompositeNode);
		assertEquals(4, Iterables.size(((ICompositeNode)node).getChildren()));
		assertEquals("zonk +", string);
		assertEquals(1, convertCallCount);
	}

	public void testKeywordConversion_01() throws Exception {
		EObject model = getModel("1 mykeyword1");
		assertWithXtend("'mykeyword1'", "this.value", model);
		// XXX value converter is not called for keywords?
		// if this is a bug, all assertions 'assertEquals(1, convertCallCount)' have to be increased
		assertEquals(lexerRule, 0, convertCallCount);
	}

	public void testKeywordConversion_02() throws Exception {
		EObject model = getModel("mykeyword1");
		assertWithXtend("'mykeyword1'", "this.value", model);
		assertEquals(lexerRule, 0, convertCallCount);
	}

	public void testKeywordConversion_03() throws Exception {
		EObject model = getModel("1+ mykeyword1");
		assertWithXtend("{'mykeyword1'}", "this.multiValue", model);
		// XXX value converter is not called for keywords?
		// if this is a bug, all assertions 'assertEquals(1, convertCallCount)' have to be increased
		assertEquals(lexerRule, 0, convertCallCount);
	}

	public void testChild1_01() throws Exception {
		// tests only, if crossrefs in alternatives work
		EObject model = getModel("content str ref 'str'");
		assertWithXtend("org::eclipse::xtext::valueconverter::bug250313::impl::Child1Impl", "this.ref.metaType", model);
		assertWithXtend("org::eclipse::xtext::valueconverter::bug250313::impl::Child1Impl", "this.children.metaType", model);
		assertWithXtend("'str'", "this.ref.name", model);
		assertEquals(lexerRule, 3, convertCallCount);
	}

	public void testChild2_01() throws Exception {
		// tests only, if crossrefs in alternatives work
		EObject model = getModel("content 'str' ref str");
		assertWithXtend("org::eclipse::xtext::valueconverter::bug250313::impl::Child2Impl", "this.ref.metaType", model);
		assertWithXtend("org::eclipse::xtext::valueconverter::bug250313::impl::Child2Impl", "this.children.metaType", model);
		assertWithXtend("'str'", "this.ref.name", model);
		assertEquals(lexerRule, 3, convertCallCount);
	}
}

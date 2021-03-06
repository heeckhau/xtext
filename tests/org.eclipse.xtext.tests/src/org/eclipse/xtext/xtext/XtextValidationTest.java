/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xtext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreValidator;
import org.eclipse.xtext.AbstractMetamodelDeclaration;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CompoundElement;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.EnumRule;
import org.eclipse.xtext.GeneratedMetamodel;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.Group;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.ReferencedMetamodel;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.TypeRef;
import org.eclipse.xtext.UnorderedGroup;
import org.eclipse.xtext.XtextFactory;
import org.eclipse.xtext.XtextStandaloneSetup;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.validation.AbstractValidationMessageAcceptingTestCase;
import org.eclipse.xtext.validation.AbstractValidationMessageAcceptor;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 * @author Michael Clay
 */
public class XtextValidationTest extends AbstractValidationMessageAcceptingTestCase {

	private String lastMessage;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(XtextStandaloneSetup.class);
		EValidator.Registry.INSTANCE.put(EcorePackage.eINSTANCE, EcoreValidator.INSTANCE);
	}
	
	/**
	 * see https://bugs.eclipse.org/bugs/show_bug.cgi?id=348052
	 */
	public void testGrammarHasNoNamespace() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"Model: name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}

	public void testRulenamesAreNotEqualIgnoreCase() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"Model: name=ID ref=[Model|ID];\n" +
				"moDel: name=ID ref='foo';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
//	public void testBug322875_01() throws Exception {
//		String testGrammar = "grammar foo.Bar with org.eclipse.xtext.common.Terminals\n " +
//				" import 'classpath:/org/eclipse/xtext/xtext/XtextValidationTest.ecore'  " +
//				" import 'http://www.eclipse.org/2008/Xtext' as xtext\n" +
//				"Bug322875 returns Bug322875: referencesETypeFromClasspathPackage=[xtext::Grammar];";
//		XtextResource resource = getResourceFromStringAndExpect(testGrammar,1);
//		assertFalse(resource.getErrors().toString(), resource.getErrors().isEmpty());
//		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
//		assertNotNull("diag", diag);
//		assertEquals(diag.toString(), 1, diag.getChildren().size());
//		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
//	}
	
//	public void testBug322875_02() throws Exception {
//		String testGrammar = "grammar foo.Bar with org.eclipse.xtext.common.Terminals\n " +
//				" import 'platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore'  " +
//				"Model returns EClass: name=ID;";
//		XtextResource resource = getResourceFromString(testGrammar);
//		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
//		assertNotNull("diag", diag);
//		assertEquals(diag.toString(), 1, diag.getChildren().size());
//		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
//	}
	
	public void testBug322875_03() throws Exception {
		String testGrammar = "grammar foo.Bar with org.eclipse.xtext.common.Terminals\n " +
				" import 'http://www.eclipse.org/emf/2002/Ecore'  " +
				"Model returns EClass: name=ID;";
		XtextResource resource = getResourceFromString(testGrammar);
		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.toString(), 0, diag.getChildren().size());
	}
	
//	public void testBug322875_04() throws Exception {
//		String testGrammarOk = "grammar foo.Bar with org.eclipse.xtext.common.Terminals\n " +
//				" import 'http://www.eclipse.org/emf/2002/Ecore'  " +
//				"Model returns EClass: name=ID;";
//		String testGrammarWithError = "grammar foo.Bar with org.eclipse.xtext.common.Terminals\n " +
//		" import 'platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore'  " +
//		"Model returns EClass: name=ID;";
//		XtextResource resourceOk = getResourceFromString(testGrammarOk);
//		XtextResource resourceError = (XtextResource) resourceOk.getResourceSet().createResource(URI.createURI("unused.xtext"));
//		resourceError.load(new StringInputStream(testGrammarWithError), null);
//		Diagnostic diagOK = Diagnostician.INSTANCE.validate(resourceOk.getContents().get(0));
//		assertNotNull("diag", diagOK);
//		assertEquals(diagOK.toString(), 0, diagOK.getChildren().size());
//		Diagnostic diagError = Diagnostician.INSTANCE.validate(resourceError.getContents().get(0));
//		assertNotNull("diag", diagError);
//		assertEquals(diagError.toString(), 1, diagError.getChildren().size());
//		assertEquals("diag.isError", diagError.getSeverity(), Diagnostic.ERROR);
//	}
	
	public void testBug_282852_01() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"Model: name=ID ref='bar';\n" +
				"Model: name=ID ref='foo';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 2, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testBug_282852_02() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"Model: name=ID ref='bar';\n" +
				"terminal Id: 'a'..'z'+;");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}

	public void testBug_282852_03() throws Exception {
		Grammar base = XtextFactory.eINSTANCE.createGrammar();
		Grammar child = XtextFactory.eINSTANCE.createGrammar();
		child.getUsedGrammars().add(base);
		AbstractRule ruleFoo = XtextFactory.eINSTANCE.createParserRule();
		ruleFoo.setName("Foo");
		base.getRules().add(ruleFoo);
		AbstractRule subRuleFoo = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo.setName("Foo");
		child.getRules().add(subRuleFoo);

		XtextValidator validator = get(XtextValidator.class);
		validator.setMessageAcceptor(this);
		validator.checkRuleName(subRuleFoo);
		assertNull(lastMessage);
	}
	
	public void testBug_282852_04() throws Exception {
		Grammar base = XtextFactory.eINSTANCE.createGrammar();
		Grammar child = XtextFactory.eINSTANCE.createGrammar();
		child.getUsedGrammars().add(base);
		AbstractRule ruleFoo = XtextFactory.eINSTANCE.createParserRule();
		ruleFoo.setName("Foo");
		base.getRules().add(ruleFoo);
		AbstractRule subRuleFoo = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo.setName("foo");
		child.getRules().add(subRuleFoo);

		XtextValidator validator = get(XtextValidator.class);
		validator.setMessageAcceptor(this).getState().currentObject = subRuleFoo;
		validator.checkRuleName(subRuleFoo);
		assertEquals("A rule's name has to be unique even case insensitive. A used grammar contains another rule 'Foo'.", lastMessage);
	}
	
	public void testBug_282852_05() throws Exception {
		Grammar base = XtextFactory.eINSTANCE.createGrammar();
		AbstractRule ruleFoo = XtextFactory.eINSTANCE.createParserRule();
		ruleFoo.setName("Foo");
		base.getRules().add(ruleFoo);
		AbstractRule subRuleFoo = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo.setName("foo");
		base.getRules().add(subRuleFoo);

		XtextValidator validator = get(XtextValidator.class);
		validator.setMessageAcceptor(this).getState().currentObject = subRuleFoo;
		validator.checkRuleName(subRuleFoo);
		assertEquals("A rule's name has to be unique even case insensitive. This grammar contains another rule 'Foo'.", lastMessage);
	}
	
	public void testBug_282852_06() throws Exception {
		Grammar base = XtextFactory.eINSTANCE.createGrammar();
		AbstractRule ruleFoo = XtextFactory.eINSTANCE.createParserRule();
		ruleFoo.setName("Foo");
		base.getRules().add(ruleFoo);
		AbstractRule subRuleFoo = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo.setName("Foo");
		base.getRules().add(subRuleFoo);

		XtextValidator validator = get(XtextValidator.class);
		validator.setMessageAcceptor(this).getState().currentObject = subRuleFoo;
		validator.checkRuleName(subRuleFoo);
		assertEquals("A rule's name has to be unique.", lastMessage);
	}
	
	public void testBug_282852_07() throws Exception {
		Grammar base = XtextFactory.eINSTANCE.createGrammar();
		AbstractRule ruleFoo = XtextFactory.eINSTANCE.createParserRule();
		ruleFoo.setName("Foo");
		base.getRules().add(ruleFoo);
		AbstractRule subRuleFoo = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo.setName("foo");
		AbstractRule subRuleFoo2 = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo2.setName("Foo");
		AbstractRule subRuleFoo3 = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo3.setName("FOO");
		base.getRules().add(subRuleFoo);
		base.getRules().add(subRuleFoo2);
		base.getRules().add(subRuleFoo3);

		XtextValidator validator = get(XtextValidator.class);
		validator.setMessageAcceptor(this).getState().currentObject = subRuleFoo;
		validator.checkRuleName(subRuleFoo);
		assertEquals("A rule's name has to be unique even case insensitive. The conflicting rules are 'FOO' and 'Foo'.", lastMessage);
	}
	
	public void testBug_282852_08() throws Exception {
		Grammar base = XtextFactory.eINSTANCE.createGrammar();
		AbstractRule ruleFoo = XtextFactory.eINSTANCE.createParserRule();
		ruleFoo.setName("Foo");
		base.getRules().add(ruleFoo);
		AbstractRule subRuleFoo = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo.setName("foo");
		AbstractRule subRuleFoo2 = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo2.setName("fOO");
		AbstractRule subRuleFoo3 = XtextFactory.eINSTANCE.createParserRule();
		subRuleFoo3.setName("FOO");
		base.getRules().add(subRuleFoo);
		base.getRules().add(subRuleFoo2);
		base.getRules().add(subRuleFoo3);

		XtextValidator validator = get(XtextValidator.class);
		validator.setMessageAcceptor(this).getState().currentObject = subRuleFoo;
		validator.checkRuleName(subRuleFoo);
		assertEquals("A rule's name has to be unique even case insensitive. The conflicting rules are 'FOO', 'Foo' and 'fOO'.", lastMessage);
	}
	
	public void testBug_283534_01() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"Model: name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 0, diag.getChildren().size());
		assertEquals("diag.isOk", diag.getSeverity(), Diagnostic.OK);
	}

	public void testBug_283534_02() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"enum EnumRule: Zonk;\n"+
				"Model: name=STRING;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testBug_283534_03() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"terminal TERMINAL: ID;\n" +
				"Model: name=STRING;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testBug_283534_04() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"Model: ID;\n" +
				"SecondModel: name=STRING;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testBug_283534_05() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate metamodel 'myURI'\n" +
				"enum EnumRule: Zonk;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 0, diag.getChildren().size());
		assertEquals("diag.isOk", diag.getSeverity(), Diagnostic.OK);
	}
	
	public void testBug_283534_06() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"terminal TERMINAL: ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 0, diag.getChildren().size());
		assertEquals("diag.isOk", diag.getSeverity(), Diagnostic.OK);
	}
	
	public void testBug_283534_07() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"Model: ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 0, diag.getChildren().size());
		assertEquals("diag.isOk", diag.getSeverity(), Diagnostic.OK);
	}
	
	public void testDuplicateEnumLiterals() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"import 'classpath:/org/eclipse/xtext/enumrules/enums.ecore'\n" +
				"generate testLanguage 'http://www.eclipse.org/2009/tmf/xtext/validation/literal/1'\n" +
				"Model: enumValue=ExistingEnum;\n" +
				"enum ExistingEnum: SameName | DifferentName='SameName';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 2, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testDefinedLiteralTwice() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate testLanguage 'http://www.eclipse.org/2009/tmf/xtext/validation/literal/2'\n" +
				"Model: enumValue=GeneratedEnum;\n" +
				"enum GeneratedEnum: SameName ='value' | SameName='otherValue';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isWarning", diag.getSeverity(), Diagnostic.WARNING);
	}
	
	public void testEnumWithEmptyLiteralGenerated() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate testLanguage 'http://www.eclipse.org/2009/tmf/xtext/validation/literal/2'\n" +
				"Model: enumValue=GeneratedEnum;\n" +
				"enum GeneratedEnum: NoLiteral | ValidLiteral='literal' | EmptyLiteral='';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}

	public void testEnumWithEmptyLiteralImported() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"import 'classpath:/org/eclipse/xtext/enumrules/enums.ecore'\n" +
				"generate test 'http://foo'\n" +
				"Model: name=ID;\n" +
				"enum ExistingEnum: SameName | DifferentName='Diff' | OverriddenLiteral='';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testEnumRuleReturnsEClass() throws Exception {
		XtextResource resource = getResourceFromStringAndExpect(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate testLanguage 'http://www.eclipse.org/2011/xtext/bug348749'\n" +
				"Model: element=Enum;\n" +
				"enum Enum returns Model: A | B;", 1);
		assertEquals(resource.getErrors().toString(), 1, resource.getErrors().size());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 0, diag.getChildren().size());
		assertEquals("diag.isOk", diag.getSeverity(), Diagnostic.OK);
	}
	
	public void testBug_280413_01() throws Exception {
		XtextResource resource = getResourceFromStringAndExpect(
				"grammar org.foo.Bar with org.eclipse.xtext.testlanguages.SimpleExpressionsTestLanguage\n" +
				"import 'classpath:/org/eclipse/xtext/testlanguages/SimpleExpressionsTestLanguage.ecore' as mm\n" +
				"Atom returns mm::Atom: name = ID;", 1);
//		System.out.println(resource.getErrors());
		assertEquals(resource.getErrors().toString(), 1, resource.getErrors().size());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
		ReferencedMetamodel metamodel = (ReferencedMetamodel) diag.getChildren().get(0).getData().get(0);
		assertNotNull(metamodel);
	}
	
	public void testBug_280413_02() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.Xtext\n" +
				"import 'http://www.eclipse.org/2008/Xtext' as xtext\n" +
				"ParserRule returns xtext::ParserRule: name = ID;");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}
	
	//TODO this one should yield a warning, because two different instances of a package might be referenced.
//	public void testBug_280413_03() throws Exception {
//		XtextResource resource = getResourceFromString(
//				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
//				"import 'classpath:/org/eclipse/xtext/Xtext.ecore' as xtext\n" +
//				"ParserRule returns xtext::ParserRule: name = ID;");
//		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
//		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());
//
//		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
//		assertNotNull("diag", diag);
//		assertEquals(diag.getSeverity(), Diagnostic.OK);
//		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
//	}
	
	public void testBug_281660() throws Exception {
		XtextResource resource = getResourceFromStringAndExpect(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : foo += [RuleB] ('->' foo+=RuleB)*;\n" +
				"RuleB : 'holla' name=ID;", 1);
		assertTrue(resource.getErrors().toString(), resource.getErrors().size()==1);
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}
	
	public void testLeftRecursion_Bug_285605_01() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleA=RuleA;");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 2, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_02() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleA=RuleA;");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 3, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_03() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleC=RuleC;\n" +
				"Unused : something=Unused;\n" +
				"RuleC : ruleA=RuleA;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 3, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_04() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : RuleA|RuleB;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 6, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_05() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : ruleA=RuleA ruleB=RuleB;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_06() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (ruleA=RuleA)? ruleB=RuleB;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 6, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_07() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (ruleA+=RuleA | '_')* name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_08() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : '_' (ruleA+=RuleA)* name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}
	
	public void testLeftRecursion_Bug_285605_09() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : '_'?  ( ( '__' | ruleA+=RuleA )* '___')? name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_10() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"Y: x+=X? x+=X;\n" +
				"X: name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}
	
	public void testLeftRecursion_Bug_285605_11() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : ruleA=RuleA & ruleB=RuleB;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 6, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_12() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : '_'?  ( ( '__' & ruleA+=RuleA )* '___')? name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_13() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (ruleA+=RuleA & '_')* name=ID;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_14() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (name=ID? & value=STRING?) ruleA=RuleA;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_15() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (name=ID & value=STRING)? ruleA=RuleA;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getChildren().toString(), 4, diag.getChildren().size());
		assertEquals("diag.isError", diag.getSeverity(), Diagnostic.ERROR);
	}
	
	public void testLeftRecursion_Bug_285605_16() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (name=ID & value=STRING) ruleA=RuleA;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}

	public void testLeftRecursion_Bug_285605_17() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (name=ID? & value=STRING) ruleA=RuleA;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.toString(), diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}
	
	public void testLeftRecursion_Bug_285605_18() throws Exception {
		XtextResource resource = getResourceFromString(
				"grammar org.foo.Bar with org.eclipse.xtext.common.Terminals\n" +
				"generate foo 'http://foo/bar'\n" +
				"RuleA : ruleB=RuleB;\n" +
				"RuleB : ruleC=RuleC;\n" +
				"RuleC : (name=ID & value=STRING?) ruleA=RuleA;\n");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.toString(), diag.getSeverity(), Diagnostic.OK);
		assertTrue(diag.getChildren().toString(), diag.getChildren().isEmpty());
	}
	
	public void testBug_286683() throws Exception {
		XtextResource resource = getResourceFromString("grammar org.xtext.example.MyDsl with org.xtext.example.MyDsl\n"+
				"generate myDsl 'http://www.xtext.org/example/MyDsl'\n"+
				"Model : {Model} 'name';");
		assertTrue(resource.getErrors().toString(), resource.getErrors().isEmpty());
		assertTrue(resource.getWarnings().toString(), resource.getWarnings().isEmpty());

		Diagnostic diag = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		assertNotNull("diag", diag);
		assertEquals(diag.getSeverity(), Diagnostic.ERROR);
		assertEquals(diag.getChildren().toString(), 1, diag.getChildren().size());
	}
	
	public void testBug_293110() throws Exception {
		XtextResource resource = doGetResource(new org.eclipse.xtext.util.StringInputStream("A: value=B; B: name=ID;"),URI.createURI("testBug_293110"));
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(resource.getContents().get(0));
		Collection<Diagnostic> runtimeExceptions = Collections2.filter(diagnostic.getChildren(), new Predicate<Diagnostic>(){
			public boolean apply(Diagnostic input) {
				return input.getException() instanceof RuntimeException;
			}});
		assertTrue(runtimeExceptions.isEmpty());
	}
	
	public void testCheckCrossReferenceTerminal_01() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		CrossReference reference = XtextFactory.eINSTANCE.createCrossReference();
		RuleCall call = XtextFactory.eINSTANCE.createRuleCall();
		reference.setTerminal(call);
		ParserRule rule = XtextFactory.eINSTANCE.createParserRule();
		call.setRule(rule);
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		rule.setType(typeRef);
		typeRef.setClassifier(EcorePackage.Literals.ESTRING);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkCrossReferenceTerminal(reference);
		messageAcceptor.validate();
	}
	
	public void testCheckCrossReferenceTerminal_02() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		CrossReference reference = XtextFactory.eINSTANCE.createCrossReference();
		RuleCall call = XtextFactory.eINSTANCE.createRuleCall();
		reference.setTerminal(call);
		ParserRule rule = XtextFactory.eINSTANCE.createParserRule();
		call.setRule(rule);
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		rule.setType(typeRef);
		typeRef.setClassifier(EcorePackage.Literals.EINT);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(call, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkCrossReferenceTerminal(reference);
		messageAcceptor.validate();
	}
	
	public void testCheckCrossReferenceTerminal_05() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		CrossReference reference = XtextFactory.eINSTANCE.createCrossReference();
		Keyword keyword = XtextFactory.eINSTANCE.createKeyword();
		reference.setTerminal(keyword);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkCrossReferenceTerminal(reference);
		messageAcceptor.validate();
	}
	
	public void testCheckActionInUnorderedGroup_01() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		UnorderedGroup unorderedGroup = XtextFactory.eINSTANCE.createUnorderedGroup();
		Action action = XtextFactory.eINSTANCE.createAction();
		unorderedGroup.getElements().add(action);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(action, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkActionInUnorderedGroup(action);
		messageAcceptor.validate();
	}
	
	public void testCheckRuleCallInUnorderedGroup_01() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		UnorderedGroup unorderedGroup = XtextFactory.eINSTANCE.createUnorderedGroup();
		RuleCall ruleCall = XtextFactory.eINSTANCE.createRuleCall();
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		typeRef.setClassifier(EcorePackage.Literals.EOBJECT);
		ParserRule parserRule = XtextFactory.eINSTANCE.createParserRule();
		parserRule.setType(typeRef);
		ruleCall.setRule(parserRule);
		unorderedGroup.getElements().add(ruleCall);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(ruleCall, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkRuleCallInUnorderedGroup(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testCheckRuleCallInUnorderedGroup_02() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		UnorderedGroup unorderedGroup = XtextFactory.eINSTANCE.createUnorderedGroup();
		RuleCall ruleCall = XtextFactory.eINSTANCE.createRuleCall();
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		typeRef.setClassifier(EcorePackage.Literals.EBIG_DECIMAL);
		ParserRule parserRule = XtextFactory.eINSTANCE.createParserRule();
		parserRule.setType(typeRef);
		ruleCall.setRule(parserRule);
		unorderedGroup.getElements().add(ruleCall);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkRuleCallInUnorderedGroup(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testCheckRuleCallInUnorderedGroup_03() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		UnorderedGroup unorderedGroup = XtextFactory.eINSTANCE.createUnorderedGroup();
		RuleCall ruleCall = XtextFactory.eINSTANCE.createRuleCall();
		EnumRule enumRule = XtextFactory.eINSTANCE.createEnumRule();
		ruleCall.setRule(enumRule);
		unorderedGroup.getElements().add(ruleCall);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkRuleCallInUnorderedGroup(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testCheckRuleCallInUnorderedGroup_04() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		UnorderedGroup unorderedGroup = XtextFactory.eINSTANCE.createUnorderedGroup();
		RuleCall ruleCall = XtextFactory.eINSTANCE.createRuleCall();
		TerminalRule terminalRule = XtextFactory.eINSTANCE.createTerminalRule();
		ruleCall.setRule(terminalRule);
		unorderedGroup.getElements().add(ruleCall);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkRuleCallInUnorderedGroup(ruleCall);
		messageAcceptor.validate();
	}

	public void testCheckRuleCallInUnorderedGroup_05() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		UnorderedGroup unorderedGroup = XtextFactory.eINSTANCE.createUnorderedGroup();
		RuleCall ruleCall = XtextFactory.eINSTANCE.createRuleCall();
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		typeRef.setClassifier(EcorePackage.Literals.EOBJECT);
		ParserRule parserRule = XtextFactory.eINSTANCE.createParserRule();
		parserRule.setType(typeRef);
		ruleCall.setRule(parserRule);
		Assignment assignment = XtextFactory.eINSTANCE.createAssignment();
		assignment.setTerminal(ruleCall);
		unorderedGroup.getElements().add(assignment);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkRuleCallInUnorderedGroup(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testBug318424_01() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		CrossReference reference = XtextFactory.eINSTANCE.createCrossReference();
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		reference.setType(typeRef);
		typeRef.setClassifier(EcorePackage.Literals.EBOOLEAN);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(typeRef, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkCrossReferenceType(reference);
		messageAcceptor.validate();
	}
	
	public void testBug318424_02() throws Exception {
		XtextValidator validator = get(XtextValidator.class);
		Action action = XtextFactory.eINSTANCE.createAction();
		TypeRef typeRef = XtextFactory.eINSTANCE.createTypeRef();
		action.setType(typeRef);
		typeRef.setClassifier(EcorePackage.Literals.EBOOLEAN);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(typeRef, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkInstantiatedType(action);
		messageAcceptor.validate();
	}
	
	public void testCycleInTypeHierarchy() throws Exception {
		String grammarAsText = "grammar test with org.eclipse.xtext.common.Terminals" +
				" generate test 'http://test'";
		grammarAsText += " RuleA: RuleB;";
		grammarAsText += " RuleB: RuleC;";
		grammarAsText += " RuleC: RuleA;";
		grammarAsText += " RuleD: RuleA;";

		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(grammar.getRules().get(0).getType(), true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(1).getType(),
				grammar.getRules().get(2).getType()
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackage((GeneratedMetamodel) metamodelDeclaration, Diagnostician.INSTANCE, Collections.EMPTY_MAP);
		messageAcceptor.validate();
	}
	
	public void testDuplicateFeatures_01() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"Model: Parent1 | Parent2 | NoParent;\n" + 
				"NoParent: foo=ID;" +
				"Parent1: Sub1 | Sub2;\n" + 
				"Parent2: Sub2 | Sub3;\n" + 
				"Sub1: x=ID;\n" + 
				"Sub2: x=ID;\n" + 
				"Sub3: x=ID;\n";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(2).getType(),
				grammar.getRules().get(3).getType(),
				grammar.getRules().get(5).getType(),
				grammar.getRules().get(4).getAlternatives(),
				grammar.getRules().get(5).getAlternatives(),
				grammar.getRules().get(6).getAlternatives()
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackage((GeneratedMetamodel) metamodelDeclaration, Diagnostician.INSTANCE, Collections.EMPTY_MAP);
		messageAcceptor.validate();
	}

	public void testDuplicateFeatures_02() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"A : (b+=B)*;\n" + 
				"B : C | D;\n" +
				"C : 'c' name=ID ('e' e+=E)+;\n" + 
				"E : name=ID;\n" + 
				"F : C | E;\n" + 
				"D : 'd' name=ID 'ref' ref=[F];";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(1).getType(),
				grammar.getRules().get(2).getType(),
				grammar.getRules().get(4).getType(),
				((CompoundElement) grammar.getRules().get(2).getAlternatives()).getElements().get(1),
				grammar.getRules().get(3).getAlternatives(),
				((CompoundElement) grammar.getRules().get(5).getAlternatives()).getElements().get(1)
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackage((GeneratedMetamodel) metamodelDeclaration, Diagnostician.INSTANCE, Collections.EMPTY_MAP);
		messageAcceptor.validate();
	}
	
	public void testNameClash_01() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"PRINTF: vars=PRINTF_Vars;\n" +
				"PRINTF_Vars: arg1=ID;";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getAlternatives(),
				grammar.getRules().get(1).getType()
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackageForNameClashes((GeneratedMetamodel) metamodelDeclaration);
		messageAcceptor.validate();
	}
	
	public void testNameClash_02() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"Class returns Class: {Class_} name=ID;\n";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		CompoundElement element = (CompoundElement) grammar.getRules().get(0).getAlternatives();
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getType(),
				((Action) element.getElements().get(0)).getType()
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackage((GeneratedMetamodel) metamodelDeclaration, Diagnostician.INSTANCE, Collections.EMPTY_MAP);
		messageAcceptor.validate();
	}
	
	public void testNameClash_03() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"Foo: myVars=ID my_vars=ID;\n";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		CompoundElement element = (CompoundElement) grammar.getRules().get(0).getAlternatives();
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getType(),
				element.getElements().get(0),
				element.getElements().get(1)
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackage((GeneratedMetamodel) metamodelDeclaration, Diagnostician.INSTANCE, Collections.EMPTY_MAP);
		messageAcceptor.validate();
	}
	
	public void testGeneratedPackageNotEmpty() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"Foo: 'a';\n";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		AbstractMetamodelDeclaration metamodelDeclaration = grammar.getMetamodelDeclarations().get(0);
		
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(metamodelDeclaration, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkGeneratedPackageNotEmpty((GeneratedMetamodel) metamodelDeclaration);
		messageAcceptor.validate();
	}
	
	public void testHiddenTokenIsATerminal_01() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals hidden(Model)\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(grammar, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkHiddenTokenIsNotAFragment(grammar);
		messageAcceptor.validate();
	}
	
	public void testHiddenTokenIsATerminal_02() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model hidden(Foo): name=ID;\n"+
			"enum Foo: A | B;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule rule = (ParserRule) grammar.getRules().get(0);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(rule, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkHiddenTokenIsNotAFragment(rule);
		messageAcceptor.validate();
	}
	
	public void testHiddenTokenIsATerminal_03() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals hidden(Fragment)\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n" +
			"terminal fragment Fragment: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(grammar, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkHiddenTokenIsNotAFragment(grammar);
		messageAcceptor.validate();
	}
	
	public void testHiddenTokenIsATerminal_04() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model hidden(Fragment): name=ID;\n" +
			"terminal fragment Fragment: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule rule = (ParserRule) grammar.getRules().get(0);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(rule, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkHiddenTokenIsNotAFragment(rule);
		messageAcceptor.validate();
	}
	
	public void testHiddenTokenIsATerminal_05() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals hidden(WS)\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkHiddenTokenIsNotAFragment(grammar);
		messageAcceptor.validate();
	}
	
	public void testHiddenTokenIsATerminal_06() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model hidden(WS): name=ID;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule rule = (ParserRule) grammar.getRules().get(0);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkHiddenTokenIsNotAFragment(rule);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_01() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n"+
			"terminal MyTerminal: Fragment;\n"+
			"terminal fragment Fragment: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		TerminalRule rule = (TerminalRule) grammar.getRules().get(1);
		RuleCall ruleCall = (RuleCall) rule.getAlternatives();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_02() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n"+
			"terminal fragment FirstFragment: SecondFragment;\n"+
			"terminal fragment SecondFragment: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		TerminalRule rule = (TerminalRule) grammar.getRules().get(1);
		RuleCall ruleCall = (RuleCall) rule.getAlternatives();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_03() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n"+
			"terminal fragment Fragment: Terminal;\n"+
			"terminal Terminal: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		TerminalRule rule = (TerminalRule) grammar.getRules().get(1);
		RuleCall ruleCall = (RuleCall) rule.getAlternatives();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_04() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=Terminal;\n"+
			"terminal Terminal: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule rule = (ParserRule) grammar.getRules().get(0);
		RuleCall ruleCall = (RuleCall) ((Assignment) rule.getAlternatives()).getTerminal();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, false, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_05() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=Fragment;\n"+
			"terminal fragment Fragment: 'a'..'z'+;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule rule = (ParserRule) grammar.getRules().get(0);
		RuleCall ruleCall = (RuleCall) ((Assignment) rule.getAlternatives()).getTerminal();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(ruleCall, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_06() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=Terminal;\n"+
			"terminal Terminal: Enum;\n" +
			"enum Enum: A | B;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		TerminalRule rule = (TerminalRule) grammar.getRules().get(1);
		RuleCall ruleCall = (RuleCall) rule.getAlternatives();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(ruleCall, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_07() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model: name=ID;\n"+
			"terminal Terminal: Model;";
	
		Grammar grammar = (Grammar) getModel(grammarAsText);
		TerminalRule rule = (TerminalRule) grammar.getRules().get(1);
		RuleCall ruleCall = (RuleCall) rule.getAlternatives();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(ruleCall, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_08_335692() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model : value=ID FRAGMENT;\n"+
			"SubModel : FRAGMENT;\n"+
			"terminal fragment FRAGMENT: ';';";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule datatypeRule = (ParserRule) grammar.getRules().get(1);
		RuleCall ruleCall = (RuleCall) datatypeRule.getAlternatives();
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(ruleCall, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testRuleCallAllowed_09_335692() throws Exception {
		String grammarAsText =
			"grammar test with org.eclipse.xtext.common.Terminals\n" +
			"generate test 'http://test'\n" +
			"Model : value=ID FRAGMENT;\n"+
			"SubModel : FRAGMENT;\n"+
			"terminal fragment FRAGMENT: ';';";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ParserRule rule = (ParserRule) grammar.getRules().get(0);
		RuleCall ruleCall = (RuleCall) ((Group) rule.getAlternatives()).getElements().get(1);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(ruleCall, true, false);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnassignedRuleCallAllowed(ruleCall);
		validator.checkTerminalFragmentCalledFromTerminalRule(ruleCall);
		messageAcceptor.validate();
	}
	
	public void testPredicatedUnorderedGroup_01() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"Model: =>(name=ID & value=STRING);\n";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getAlternatives(),
				((CompoundElement) grammar.getRules().get(0).getAlternatives()).getElements().get(0)
		);
		PredicateUsesUnorderedGroupInspector inspector = new PredicateUsesUnorderedGroupInspector(messageAcceptor);
		inspector.inspect(grammar);
		messageAcceptor.validate();
	}
	
	public void testPredicatedUnorderedGroup_02() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"A: =>C;\n" +
				"B: =>C;\n" +
				"C: name=ID & value=STRING;";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getAlternatives(),
				grammar.getRules().get(1).getAlternatives(),
				grammar.getRules().get(2).getAlternatives()
		);
		PredicateUsesUnorderedGroupInspector inspector = new PredicateUsesUnorderedGroupInspector(messageAcceptor);
		inspector.inspect(grammar);
		messageAcceptor.validate();
	}
	
	public void testPredicatedUnorderedGroup_03() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"A: =>B;\n" +
				"B: =>C;\n" +
				"C: name=ID & value=STRING;";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getAlternatives(),
				grammar.getRules().get(1).getAlternatives(),
				grammar.getRules().get(2).getAlternatives()
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnorderedGroupIsNotPredicated(grammar);
		messageAcceptor.validate();
	}
	
	public void testPredicatedUnorderedGroup_04() throws Exception {
		String grammarAsText =
				"grammar test with org.eclipse.xtext.common.Terminals\n" +
				"generate test 'http://test'\n" +
				"A: =>(name=ID value=B);\n" +
				"B: name=ID & value=STRING;";
		
		Grammar grammar = (Grammar) getModel(grammarAsText);
		XtextValidator validator = get(XtextValidator.class);
		ValidatingMessageAcceptor messageAcceptor = new ValidatingMessageAcceptor(null, true, false);
		Group predicatedGroup = (Group) grammar.getRules().get(0).getAlternatives();
		Group groupContent = (Group) predicatedGroup.getElements().get(0);
		Assignment valueAssignment = (Assignment) groupContent.getElements().get(1);
		messageAcceptor.expectedContext(
				grammar.getRules().get(0).getAlternatives(),
				valueAssignment.getTerminal(),
				grammar.getRules().get(1).getAlternatives()
		);
		validator.setMessageAcceptor(messageAcceptor);
		validator.checkUnorderedGroupIsNotPredicated(grammar);
		messageAcceptor.validate();
	}
	
	public class ValidatingMessageAcceptor extends AbstractValidationMessageAcceptor {

		private final Set<EObject> contexts;
		private boolean error;
		private boolean warning;
		private boolean info;

		public ValidatingMessageAcceptor(EObject context, boolean error, boolean warning) {
			this.contexts = Sets.newHashSet();
			if (context != null)
				contexts.add(context);
			this.error = error;
			this.warning = warning;
		}
		
		public void expectedContext(EObject... contexts) {
			this.contexts.addAll(Arrays.asList(contexts));
		}
		
		public void validate() {
			assertTrue(contexts.toString(), contexts.isEmpty());
			assertFalse(info);
			assertFalse(warning);
			assertFalse(error);
		}

		@Override
		public void acceptError(String message, EObject object, EStructuralFeature feature, int index, String code, String... issueData) {
			assertTrue(error);
			assertTrue(String.valueOf(object) + " but expected: " + contexts.toString(), contexts.remove(object));
			error = contexts.size() > 0;
		}

		@Override
		public void acceptWarning(String message, EObject object, EStructuralFeature feature, int index, String code, String... issueData) {
			assertTrue(warning);
			assertTrue(object.toString(), contexts.remove(object));
			warning = contexts.size() > 0;
		}
		
		@Override
		public void acceptInfo(String message, EObject object, EStructuralFeature feature, int index, String code, String... issueData) {
			assertTrue(warning);
			assertTrue(object.toString(), contexts.remove(object));
			info = contexts.size() > 0;
		}
		
	}
	
	@Override
	public void acceptError(String message, EObject object, EStructuralFeature feature, int index, String code, String... issueData) {
		assertNull(lastMessage);
		lastMessage = message;
	}

}

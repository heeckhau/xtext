/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xtext.ui;

import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.AbstractMetamodelDeclaration;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.XtextPackage;
import org.eclipse.xtext.XtextStandaloneSetup;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.util.ITextRegion;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class XtextLocationInFileProviderTest extends AbstractXtextTests {

	private static String grammarText = "grammar org.xtext.Sample\n" +
			"generate first 'http://my.uri'\n" +
			"generate second 'http://my.uri' as alias\n" +
			"Root returns Root: 'keyword'*;";
	
	private Grammar grammar;

	private ILocationInFileProvider locationInFileProvider;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(XtextStandaloneSetup.class);
		grammar = (Grammar) getModel(grammarText);
		locationInFileProvider = get(ILocationInFileProvider.class);
	}
	
	@Override
	protected void tearDown() throws Exception {
		grammar = null;
		super.tearDown();
	}
	
	public void testAliasLocation() {
		AbstractMetamodelDeclaration declaration = grammar.getMetamodelDeclarations().get(1);
		ITextRegion region = locationInFileProvider.getSignificantTextRegion(declaration);
		String alias = grammarText.substring(region.getOffset(), region.getOffset() + region.getLength());
		assertEquals("alias", alias);
	}
	
	public void testMMNameLocation() {
		AbstractMetamodelDeclaration declaration = grammar.getMetamodelDeclarations().get(0);
		ITextRegion region = locationInFileProvider.getSignificantTextRegion(declaration);
		String first = grammarText.substring(region.getOffset(), region.getOffset() + region.getLength());
		assertEquals("first", first);
	}
	
	public void testCardinalityLocation() {
		AbstractElement body = grammar.getRules().get(0).getAlternatives();
		ITextRegion region = locationInFileProvider.getSignificantTextRegion(body, XtextPackage.Literals.ABSTRACT_ELEMENT__CARDINALITY, 0);
		String cardinality = grammarText.substring(region.getOffset(), region.getOffset() + region.getLength());
		assertEquals("*", cardinality);
	}
	
	public void testKeywordLocation() {
		Keyword keyword = (Keyword) grammar.getRules().get(0).getAlternatives();
		ITextRegion region = locationInFileProvider.getFullTextRegion(keyword);
		String fullRegion = grammarText.substring(region.getOffset(), region.getOffset() + region.getLength());
		assertEquals("'keyword'*", fullRegion);
		region = locationInFileProvider.getSignificantTextRegion(keyword);
		String significantRegion = grammarText.substring(region.getOffset(), region.getOffset() + region.getLength());
		assertEquals("'keyword'*", significantRegion);
	}
}

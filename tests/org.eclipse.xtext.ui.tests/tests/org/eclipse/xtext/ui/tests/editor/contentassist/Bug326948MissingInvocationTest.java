/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import junit.framework.Assert;
import junit.framework.Test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.testlanguages.backtracking.services.BeeLangTestLanguageGrammarAccess;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.junit.editor.contentassist.AbstractContentAssistProcessorTest;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug326948MissingInvocationTest extends AbstractBug326948Test {

	private boolean featureNameCalled;
	private boolean nameCalled;
	private boolean keywordCalled;
	
	@Override
	public void completeInfixExpression_FeatureName(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		featureNameCalled = true;
		assertEquals("", context.getPrefix());
	}
	
	@Override
	public void completeInfixExpression_Name(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		nameCalled = true;
		assertEquals("", context.getPrefix());
	}
	
	@Override
	public void completeKeyword(Keyword keyword, ContentAssistContext contentAssistContext,
			ICompletionProposalAcceptor acceptor) {
		if (keyword == ((BeeLangTestLanguageGrammarAccess) getGrammarAccess()).getInfixExpressionAccess().getLeftSquareBracketKeyword_1_1_1()) {
			Assert.assertEquals(".", contentAssistContext.getPrefix());
			keywordCalled = true;
		}
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		featureNameCalled = false;
		nameCalled = false;
		keywordCalled = false;
	}
	
	@Override
	protected void tearDown() throws Exception {
		keywordCalled = false;
		featureNameCalled = false;
		nameCalled = false;
		super.tearDown();
	}
	
	public void testAllAlternativesProposed() throws Exception {
		newBuilder()
			.append("function foo() { \"\".")
			.computeCompletionProposals();
		assertTrue(featureNameCalled);
		assertTrue(nameCalled);
		assertTrue(keywordCalled);
	}

	public static Test suite() {
		return AbstractContentAssistProcessorTest.suite(Bug326948MissingInvocationTest.class);
	}

}

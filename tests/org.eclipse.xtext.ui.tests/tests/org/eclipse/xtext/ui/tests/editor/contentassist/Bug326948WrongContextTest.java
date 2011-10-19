/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import junit.framework.Test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.testlanguages.backtracking.beeLangTestLanguage.ChainedExpression;
import org.eclipse.xtext.testlanguages.backtracking.beeLangTestLanguage.ValueLiteral;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.junit.editor.contentassist.AbstractContentAssistProcessorTest;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug326948WrongContextTest extends AbstractBug326948Test {

	@Override
	public void completeInfixExpression_FeatureName(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		assertContextAsExpected(context);
	}

	protected void assertContextAsExpected(ContentAssistContext context) {
		assertTrue(String.valueOf(context.getPreviousModel()), context.getPreviousModel() instanceof ValueLiteral);
		assertTrue(String.valueOf(context.getPreviousModel()), context.getCurrentModel() instanceof ChainedExpression);
	}
	
	@Override
	public void completeKeyword(Keyword keyword, ContentAssistContext contentAssistContext,
			ICompletionProposalAcceptor acceptor) {
		assertContextAsExpected(contentAssistContext);
	}
	
	@Override
	public void completeInfixExpression_Name(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		assertContextAsExpected(context);
	}
	
	public void testCurrentModel_01() throws Exception {
		newBuilder()
			.append("function foo() { \"\".")
			.computeCompletionProposals();
	}
	
	public void testCurrentModel_02() throws Exception {
		newBuilder()
			.append("function foo() { \"\". ;")
			.computeCompletionProposals(" ;");
	}
	
	public void testCurrentModel_03() throws Exception {
		newBuilder()
			.append("function foo() { \"\". ; }")
			.computeCompletionProposals(" ;");
	}

	public static Test suite() {
		return AbstractContentAssistProcessorTest.suite(Bug326948WrongContextTest.class);
	}
}

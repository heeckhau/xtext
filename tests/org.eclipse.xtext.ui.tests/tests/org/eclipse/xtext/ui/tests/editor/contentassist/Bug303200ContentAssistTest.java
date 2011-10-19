/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import junit.framework.Test;

import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.ui.junit.editor.contentassist.AbstractContentAssistProcessorTest;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.eclipse.xtext.ui.tests.Activator;
import org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug303200TestLanguageUiModule;
import org.eclipse.xtext.util.Modules2;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug303200ContentAssistTest extends AbstractContentAssistProcessorTest {

	@Override
	public ISetup doGetSetup() {
		return new Bug303200TestLanguageStandaloneSetup() {
			@Override
			public Injector createInjector() {
				return Guice.createInjector(Modules2.mixin(new Bug303200TestLanguageRuntimeModule(),
						new Bug303200TestLanguageUiModule(Activator.getInstance()),
						new SharedStateModule()));
			}
		};
	}
	
	public void testEmptyModel() throws Exception {
		newBuilder().assertText(
				"function", // FunctionDefinition
				"Ident", // Attribute
				"[", // Attribute
				"{", // Block
				"Name" // PrimaryExpression
				);
	}
	
	public void testAfterFunction_01() throws Exception {
		newBuilder().append("function f() {}").assertText(
				"function", // FunctionDefinition
				"Ident", // Attribute
				"[", // Attribute
				"{", // Block
				"Name", // PrimaryExpression
				"}" // closing brace
				);
	}
	
	public void testAfterFunction_02() throws Exception {
		newBuilder().appendNl("function f() {}").assertText(
				"function", // FunctionDefinition
				"Ident", // Attribute
				"[", // Attribute
				"{", // Block
				"Name" // PrimaryExpression
				);
	}

	public static Test suite() {
		return AbstractContentAssistProcessorTest.suite(Bug303200ContentAssistTest.class);
	}
	
}


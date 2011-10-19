/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
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
import org.eclipse.xtext.ui.tests.editor.contentassist.ui.LookAheadContentAssistTestLanguageUiModule;
import org.eclipse.xtext.util.Modules2;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug282031Test extends AbstractContentAssistProcessorTest {

	@Override
	public ISetup doGetSetup() {
		return new LookAheadContentAssistTestLanguageStandaloneSetup() {
			@Override
			public Injector createInjector() {
				return Guice.createInjector(Modules2.mixin(new LookAheadContentAssistTestLanguageRuntimeModule(),new LookAheadContentAssistTestLanguageUiModule(Activator.getInstance()),
						new SharedStateModule()));
			}
		};
	}
	
	public void testBug282031_01() throws Exception {
    	newBuilder()
    		.append("( values1 ")
    		.assertText("=", ")", "Value", "Name");
    }
    
    public void testBug282031_02() throws Exception {
    	newBuilder()
    		.append("(")
    		.assertText("(", ")", "Value", "Name");
    }
    
    public void testBug282031_03() throws Exception {
    	newBuilder()
    		.append("( ")
    		.assertText(")", "Value", "Name");
    }
    
    public void testBug282031_04() throws Exception {
    	newBuilder()
    		.append("( values1")
    		.assertText("=", ")");
    }
    
    public void testBug282031_05() throws Exception {
    	newBuilder()
    		.append("[ values1 ")
    		.assertText("]", "Value", "Name");
    }
    
    public void testBug282031_06() throws Exception {
    	newBuilder()
    		.append("[")
    		.assertText("[", "Value");
    }
    
    public void testBug282031_07() throws Exception {
    	newBuilder()
    		.append("[ ")
    		.assertText("Value");
    }
    
    public void testBug282031_08() throws Exception {
    	newBuilder()
    		.append("[ values1")
    		.assertText("]");
    }
    
    public void testBug282031_09() throws Exception {
    	newBuilder()
    		.append("< values1 ")
    		.assertText("=", "Value", "Name");
    }
    
    public void testBug282031_10() throws Exception {
    	newBuilder()
    		.append("<")
    		.assertText("<", "Value", "Name");
    }
    
    public void testBug282031_11() throws Exception {
    	newBuilder()
    		.append("< ")
    		.assertText("Value", "Name");
    }
    
    public void testBug282031_12() throws Exception {
    	newBuilder()
    		.append("< values1")
    		.assertText("=");
    }
    
    public void testBug282031_13() throws Exception {
    	newBuilder()
    		.append("{ values1 ")
    		.assertText("Value", "Name");
    }
    
    public void testBug282031_14() throws Exception {
    	newBuilder()
    		.append("{")
    		.assertText("{", "Value");
    }
    
    public void testBug282031_15() throws Exception {
    	newBuilder()
    		.append("{ ")
    		.assertText("Value");
    }
    
    public void testBug282031_16() throws Exception {
    	newBuilder()
    		.append("{ values1")
    		.assertText();
    }

    public static Test suite() {
		return AbstractContentAssistProcessorTest.suite(Bug282031Test.class);
	}
    
	
}

/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import org.eclipse.xtext.ui.junit.editor.contentassist.AbstractContentAssistProcessorTest;

import junit.framework.Test;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug348427Scenario2_3Test extends AbstractBug348427Test {

	@Override
	protected String getScenario() {
		return "2.3";
	}

	public static Test suite() {
		return AbstractContentAssistProcessorTest.suite(Bug348427Scenario2_3Test.class);
	}

}

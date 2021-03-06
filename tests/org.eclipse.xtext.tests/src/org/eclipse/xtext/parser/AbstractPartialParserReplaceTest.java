/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.parser;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.IReferableElementsUnloader;
import org.eclipse.xtext.parser.impl.PartialParsingPointers;
import org.eclipse.xtext.util.ReplaceRegion;
import org.eclipse.xtext.util.Wrapper;

/**
 * @author Jan K�hnlein - Initial contribution and API
 * @author Sebastian Zarnekow
 */
public abstract class AbstractPartialParserReplaceTest extends AbstractPartialParserTest {

	protected void replaceAndReparse(String model, int offset, int length, String change, String expectedReparseRegion)
			throws Exception {
		IParseResult parseResult = getParseResult(model);
		PartialParsingPointers parsingPointers = partialParser.calculatePartialParsingPointers(parseResult, offset,
				length);
		String reparseRegion = partialParser.insertChangeIntoReplaceRegion(parsingPointers
				.getDefaultReplaceRootNode(), new ReplaceRegion(offset, length, change));
		assertEquals(expectedReparseRegion, reparseRegion);
		final Wrapper<Boolean> unloaded = Wrapper.wrap(Boolean.FALSE);
		partialParser.setUnloader(new IReferableElementsUnloader() {
			public void unloadRoot(EObject root) {
				unloaded.set(Boolean.TRUE);
			}
		});
		IParseResult partiallyReparse = reparse(parseResult, offset, length, change);
		assertTrue("unloaded", unloaded.get());
		assertFalse(partiallyReparse.hasSyntaxErrors());
		String expectedReparseModel = model.substring(0, offset) + change + model.substring(offset + length);
		assertEquals(expectedReparseModel, partiallyReparse.getRootNode().getText());
	}
	
}

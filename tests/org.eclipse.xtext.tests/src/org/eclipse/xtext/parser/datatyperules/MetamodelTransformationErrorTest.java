/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.parser.datatyperules;

import static org.easymock.EasyMock.*;

import java.util.List;

import org.easymock.EasyMock;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.XtextStandaloneSetup;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xtext.ecoreInference.ErrorAcceptor;
import org.eclipse.xtext.xtext.ecoreInference.TransformationErrorCode;
import org.eclipse.xtext.xtext.ecoreInference.Xtext2EcoreTransformer;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class MetamodelTransformationErrorTest extends AbstractXtextTests {

	private String model;
	private XtextResource resource;
	private Xtext2EcoreTransformer transformer;
	private ErrorAcceptor errorAcceptor;
	private Grammar grammar;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(XtextStandaloneSetup.class);
		model = "grammar datatypetests with org.eclipse.xtext.common.Terminals\n" +
				"import 'http://www.eclipse.org/emf/2002/Ecore' as ecore\n" +
				"generate metamodel 'http://fooo'\n" +
				"Start:\n" +
				"  id=ValidId id2=ValidId2 failure1=FailureId failure2=Failure2;\n" +
				"ValidId returns ecore::EString: ID '.' ID;\n" +
				"ValidId2 returns ecore::EString: ID '.' ValidId;\n" +
				"FailureId returns ecore::EString: name=ID;\n" +
				"Failure2 returns ecore::EString: name=Start;";
		resource = getResourceFromStringAndExpect(model, 2);
		grammar = (Grammar) resource.getContents().get(0);
		transformer = new Xtext2EcoreTransformer(grammar);
		errorAcceptor = EasyMock.createMock(ErrorAcceptor.class);
		transformer.setErrorAcceptor(errorAcceptor);
	}

	public void testSetUp() {
		assertNotNull(resource);
		assertNotNull(transformer);
		assertNotNull(errorAcceptor);
		assertEquals(2, resource.getErrors().size());
	}

	public void testErrorMessages() throws Exception {
		errorAcceptor.acceptError(same(TransformationErrorCode.InvalidDatatypeRule), (String) anyObject(), same(grammar.getRules().get(3)));
		errorAcceptor.acceptError(same(TransformationErrorCode.InvalidDatatypeRule), (String) anyObject(), same(grammar.getRules().get(4)));
		transform();
	}

	private EPackage transform() throws Exception {
		replay(errorAcceptor);
		transformer.removeGeneratedPackages();
		transformer.transform();
		List<EPackage> metamodels = transformer.getGeneratedPackages();
		verify(errorAcceptor);
		assertNotNull(metamodels);
		assertEquals(1, metamodels.size());
		return metamodels.get(0);
	}

}

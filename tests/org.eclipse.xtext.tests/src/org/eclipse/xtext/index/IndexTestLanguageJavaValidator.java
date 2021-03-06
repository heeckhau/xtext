/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.index;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.index.indexTestLanguage.Entity;
import org.eclipse.xtext.index.indexTestLanguage.IndexTestLanguageFactory;
import org.eclipse.xtext.index.indexTestLanguage.IndexTestLanguagePackage;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;

/**
 * @author Michael Clay - Initial contribution and API
 */
public class IndexTestLanguageJavaValidator extends AbstractDeclarativeValidator {

	@Override
	protected List<EPackage> getEPackages() {
		List<EPackage> result = new ArrayList<EPackage>();
		result.add(IndexTestLanguagePackage.eINSTANCE);
		return result;
	}

	@Check
	public void createErrorContextObjectWithoutNodeModel(Entity entity) {
		Entity contextObjectWithoutLocation = IndexTestLanguageFactory.eINSTANCE.createEntity();
		error("test for bugfix https://bugs.eclipse.org/bugs/show_bug.cgi?id=315249", 
				contextObjectWithoutLocation, 
				IndexTestLanguagePackage.Literals.TYPE__NAME,
				ValidationMessageAcceptor.INSIGNIFICANT_INDEX,
				null);
	}
	
	@Check
	public void createWarningContextObjectWithoutNodeModel(Entity entity) {
		Entity contextObjectWithoutLocation = IndexTestLanguageFactory.eINSTANCE.createEntity();
		warning("test for bugfix https://bugs.eclipse.org/bugs/show_bug.cgi?id=315249", 
				contextObjectWithoutLocation, 
				IndexTestLanguagePackage.Literals.TYPE__NAME,
				ValidationMessageAcceptor.INSIGNIFICANT_INDEX,
				null);
	}
}

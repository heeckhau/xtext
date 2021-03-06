/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.model.edit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
public class SemanticModificationWrapper implements IModification {

	private URI uriToProblem;

	private ISemanticModification semanticModification;
	
	
	public SemanticModificationWrapper(URI uriToProblem, ISemanticModification semanticModification) {
		this.semanticModification = semanticModification;
		this.uriToProblem = uriToProblem;
	}

	public void apply(final IModificationContext context) {
		context.getXtextDocument().modify(new IUnitOfWork.Void<XtextResource>() {
			@Override
			public void process(XtextResource state) throws Exception {
				EObject eObject = state.getEObject(uriToProblem.fragment());
				semanticModification.apply(eObject, context);
			}
		});
	}

}

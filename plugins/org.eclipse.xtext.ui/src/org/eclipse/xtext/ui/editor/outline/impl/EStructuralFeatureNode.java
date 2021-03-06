/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.outline.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;

/**
 * A node representing the value(s) of one of its owner's {@link EStructuralFeature}s.
 * 
 * @author Jan Koehnlein - Initial contribution and API
 */
public class EStructuralFeatureNode extends AbstractOutlineNode {

	private URI ownerURI;

	private EStructuralFeature feature;

	public EStructuralFeatureNode(EObject owner, EStructuralFeature feature, IOutlineNode parent, Image image, Object text,
			boolean isLeaf) {
		super(parent, image, text, isLeaf);
		this.ownerURI = EcoreUtil.getURI(owner);
		this.feature = feature;
	}

	@Override
	public URI getEObjectURI() {
		return ownerURI;
	}

	public EStructuralFeature getEStructuralFeature() {
		return feature;
	}

}

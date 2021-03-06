/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.common.types.impl;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.ITypeReferenceVisitor;
import org.eclipse.xtext.common.types.util.ITypeReferenceVisitorWithParameter;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class JvmSpecializedTypeReferenceImplCustom extends JvmSpecializedTypeReferenceImpl {

	@Override
	public JvmType getType() {
		JvmTypeReference resolvedEquivalent = getEquivalent();
		if (resolvedEquivalent != null)
			return resolvedEquivalent.getType();
		return null;
	}
	
	@Override
	public <Result> Result accept(ITypeReferenceVisitor<Result> visitor) {
		Result result = visitor.doVisitSpecializedTypeReference(this);
		return result;
	}
	
	@Override
	public <Parameter, Result> Result accept(ITypeReferenceVisitorWithParameter<Parameter,Result> visitor, Parameter parameter) {
		Result result = visitor.doVisitSpecializedTypeReference(this, parameter);
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(eClass().getName());
		result.append(": ");
		if (equivalent == null) {
			result.append(" equivalent is not yet computed");
		} else if (equivalent.eIsProxy()) {
			result.append(" (equivalent uri: ");
			result.append(((InternalEObject) equivalent).eProxyURI());
			result.append(')');
		} else {
			result.append(equivalent.toString());
		}
		return result.toString();
	}
}

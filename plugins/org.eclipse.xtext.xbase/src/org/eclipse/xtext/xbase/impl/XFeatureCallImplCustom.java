/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.impl;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.XExpression;


/**
 * @author Sven Efftinge - Initial contribution and API
 */
public class XFeatureCallImplCustom extends XFeatureCallImpl {
	
	@Override
	public String toString() {
		return getConcreteSyntaxFeatureName()+getExpressionsAsString(getFeatureCallArguments(),isExplicitOperationCall());
	}
	
	@Override
	public EList<XExpression> getExplicitArguments() {
		BasicEList<XExpression> result = new BasicEList<XExpression>();
		result.addAll(getFeatureCallArguments());
		return result;
	}
	
	@Override
	public boolean isExplicitOperationCallOrBuilderSyntax() {
		return super.isExplicitOperationCall() || !getFeatureCallArguments().isEmpty();
	}
	
}

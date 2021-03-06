/*******************************************************************************
 * Copyright (c) 2010 Christoph Kulla
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Christoph Kulla - Initial API and implementation
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.hover;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.ITextHover;

import com.google.inject.Inject;

/**
 * The default implementation of a composite hover. Configured with the 
 * AnnotationWithQuickFixesHover and XtextHtmlHover.
 * 
 * @author Christoph Kulla - Initial contribution and API
 */
public class DefaultCompositeHover extends AbstractCompositeHover  {

	@Inject 
	protected AnnotationWithQuickFixesHover annotationHover;
	
	@Inject
	protected DispatchingEObjectTextHover htmlHover;
	
	@Override
	protected List<ITextHover> createHovers() {
		List<ITextHover> list = new ArrayList<ITextHover>();
		list.add (annotationHover);
		list.add (htmlHover);
		return list;
	}

}

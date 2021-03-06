/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.outline;

import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
public class FilterContributionTest extends AbstractOutlineWorkbenchTest {

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		setFiltering(false);
	}
	
	public void testFilterOutline() {
		treeViewer.expandAll();
		setFiltering(false);
		assertEquals(1, treeViewer.getTree().getItemCount());
		assertSame(modelNode, treeViewer.getTree().getItem(0));
		assertEquals(2, treeViewer.getTree().getItem(0).getItemCount());
		assertSame(oneNode, treeViewer.getTree().getItem(0).getItem(0));
		assertSame(fourNode, treeViewer.getTree().getItem(0).getItem(1));
		assertEquals(2, treeViewer.getTree().getItem(0).getItem(0).getItemCount());
		assertSame(twoNode, treeViewer.getTree().getItem(0).getItem(0).getItem(0));
		assertSame(threeNode, treeViewer.getTree().getItem(0).getItem(0).getItem(1));

		setFiltering(true);
		treeViewer.expandAll();
		assertEquals(1, treeViewer.getTree().getItemCount());
		assertSame(modelNode, treeViewer.getTree().getItem(0));
		assertEquals(1, treeViewer.getTree().getItem(0).getItemCount());
		assertSame(oneNode, treeViewer.getTree().getItem(0).getItem(0));
		assertEquals(1, treeViewer.getTree().getItem(0).getItem(0).getItemCount());
		assertSame(twoNode, treeViewer.getTree().getItem(0).getItem(0).getItem(0));
	}
	
	public void testFilteringPreservesExpansionAndSelection() throws Exception {
		treeViewer.setExpandedElements(new Object[] {modelNode});
		treeViewer.setSelection(new StructuredSelection(oneNode));
		setFiltering(true);
		assertExpanded(treeViewer, modelNode);
		assertSelected(treeViewer, oneNode);
	}
	
	protected void setFiltering(boolean isFiltering) {
		preferenceStore.setValue(SampleFilterContribution.PREFERENCE_KEY, isFiltering);
	}
}

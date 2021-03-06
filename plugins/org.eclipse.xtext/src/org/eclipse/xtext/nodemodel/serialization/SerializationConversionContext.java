/*******************************************************************************
 * Copyright (c) 2011 Sigasi N.V. (http://www.sigasi.com) and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.xtext.nodemodel.serialization;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.nodemodel.impl.RootNode;
import org.eclipse.xtext.resource.XtextResource;

/**
 * @author Mark Christiaens - Initial contribution
 * 
 * @since 2.1
 */

public class SerializationConversionContext {
	final private Map<EObject, Integer> grammarElementToIdMap;
	final private ArrayList<String> grammarIdToURIMap;
	final private Map<EObject, Integer> eObjectToIdMap;

	public SerializationConversionContext(XtextResource resource) {
		grammarElementToIdMap = new IdentityHashMap<EObject, Integer>();
		grammarIdToURIMap = new ArrayList<String>();
		eObjectToIdMap = new IdentityHashMap<EObject, Integer>();

		fillEObjectToIdMap(resource);
		fillGrammarElementToIdMap(resource);
	}

	public Integer getGrammarElementId(EObject grammarElement) {
		final Integer id = grammarElementToIdMap.get(grammarElement);

		if (id == null) {
			throw new IllegalArgumentException(
					"Trying to fetch a grammar element that does not (no longer) exists with id: " + id);
		}

		return id;
	}

	protected void fillEObjectToIdMap(Resource resource) {
		ArrayList<EObject> idToEObjectMap = new ArrayList<EObject>();

		SerializationUtil.fillIdToEObjectMap(resource, idToEObjectMap);

		for (int id = 0; id < idToEObjectMap.size(); ++id) {
			eObjectToIdMap.put(idToEObjectMap.get(id), id);
		}
	}

	protected void fillGrammarElementToIdMap(XtextResource r) {
		RootNode it = (RootNode) r.getParseResult().getRootNode();

		it.fillGrammarElementToIdMap(0, grammarElementToIdMap, grammarIdToURIMap);
	}

	public Integer getEObjectId(EObject eObject) {
		Integer id = eObjectToIdMap.get(eObject);

		if (id == null) {
			throw new IllegalArgumentException("Tryin to fetch an EMF object that does not exist (no longer) with id: "
					+ id);
		}

		return id;
	}

	public String[] getGrammarIdToURIMap() {
		String[] map = grammarIdToURIMap.toArray(new String[0]);

		return map;
	}
}

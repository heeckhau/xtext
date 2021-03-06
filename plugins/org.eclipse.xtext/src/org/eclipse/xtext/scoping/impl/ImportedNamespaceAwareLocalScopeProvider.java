/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.scoping.impl;

import static java.util.Collections.*;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.ISelectable;
import org.eclipse.xtext.scoping.ICaseInsensitivityHelper;
import org.eclipse.xtext.scoping.IGlobalScopeProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.util.IResourceScopeCache;
import org.eclipse.xtext.util.SimpleAttributeResolver;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.util.Tuples;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * A local scope provider that understands namespace imports.
 * 
 * It scans model elements for an EAttribute <code>importedNamespace</code>. The value of this attribute is interpreted
 * as qualified name to be imported. Wildcards are supported (see {@link #getWildCard()} for details).
 * 
 * Imports are valid for all elements in the same container and their children.
 * 
 * See http://www.eclipse.org/Xtext/documentation/latest/xtext.html#scoping for details.
 * 
 * @author Sven Efftinge - Initial contribution and API
 * @author Jan Koehnlein
 * @author Sebastian Zarnekow
 */
public class ImportedNamespaceAwareLocalScopeProvider extends AbstractGlobalScopeDelegatingScopeProvider {

	@Inject
	private IResourceScopeCache cache = IResourceScopeCache.NullImpl.INSTANCE;

	@Inject
	private IQualifiedNameProvider qualifiedNameProvider;

	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;
	
	@Inject
	public ImportedNamespaceAwareLocalScopeProvider() {
	}

	public ImportedNamespaceAwareLocalScopeProvider(IGlobalScopeProvider globalScopeProvider,
			IQualifiedNameProvider qualifiedNameProvider, IQualifiedNameConverter qualifiedNameConverter,
			ICaseInsensitivityHelper caseInsensitivityHelper) {
		super(globalScopeProvider, caseInsensitivityHelper);
		this.qualifiedNameProvider = qualifiedNameProvider;
		this.qualifiedNameConverter = qualifiedNameConverter;
	}

	public IQualifiedNameProvider getQualifiedNameProvider() {
		return qualifiedNameProvider;
	}

	public IScope getScope(EObject context, EReference reference) {
		if (context == null)
			throw new NullPointerException("context");
		IScope result = null;
		if (context.eContainer() != null) {
			result = getScope(context.eContainer(), reference);
		} else {
			result = getResourceScope(context.eResource(), reference);
		}
		return getLocalElementsScope(result, context, reference);
	}
	
	protected IScope getResourceScope(Resource res, EReference reference) {
		EObject context = res.getContents().get(0);
		IScope globalScope = getGlobalScope(res, reference);
		List<ImportNormalizer> normalizers = getImplicitImports(isIgnoreCase(reference));
		if (!normalizers.isEmpty()) {
			globalScope = createImportScope(globalScope, normalizers, null, reference.getEReferenceType(), isIgnoreCase(reference));
		}
		return getResourceScope(globalScope, context, reference);
	}

	protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
		return emptyList();
	}

	protected IScope getResourceScope(final IScope parent, final EObject context, final EReference reference) {
		// TODO: SZ - context may not be a proxy, may it?
		if (context.eResource() == null)
			return parent;
		ISelectable allDescriptions = getAllDescriptions(context.eResource());
		return SelectableBasedScope.createScope(parent, allDescriptions, reference.getEReferenceType(), isIgnoreCase(reference));
	}

	protected Object getKey(Notifier context, EReference reference) {
		return Tuples.create(ImportedNamespaceAwareLocalScopeProvider.class, context, reference);
	}

	protected List<ImportNormalizer> getImportedNamespaceResolvers(final EObject context, final boolean ignoreCase) {
		return cache.get(Tuples.create(context, ignoreCase, "imports"), context.eResource(), new Provider<List<ImportNormalizer>>() {
			public List<ImportNormalizer> get() {
				return internalGetImportedNamespaceResolvers(context, ignoreCase);
			}
		});
	}

	protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(final EObject context, boolean ignoreCase) {
		List<ImportNormalizer> importedNamespaceResolvers = Lists.newArrayList();
		SimpleAttributeResolver<EObject, String> importResolver = SimpleAttributeResolver.newResolver(String.class,
				"importedNamespace");
		EList<EObject> eContents = context.eContents();
		for (EObject child : eContents) {
			String value = importResolver.getValue(child);
			ImportNormalizer resolver = createImportedNamespaceResolver(value, ignoreCase);
			if (resolver != null)
				importedNamespaceResolvers.add(resolver);
		}
		return importedNamespaceResolvers;
	}

	/**
	 * Create a new {@link ImportNormalizer} for the given namespace.
	 * @param namespace the namespace.
	 * @param ignoreCase <code>true</code> if the resolver should be case insensitive.
	 * @return a new {@link ImportNormalizer} or <code>null</code> if the namespace cannot be converted to a valid
	 * qualified name.
	 */
	protected ImportNormalizer createImportedNamespaceResolver(final String namespace, boolean ignoreCase) {
		if (Strings.isEmpty(namespace))
			return null;
		QualifiedName importedNamespace = qualifiedNameConverter.toQualifiedName(namespace);
		if (importedNamespace == null || importedNamespace.getSegmentCount() < 1) {
			return null;
		}
		boolean hasWildCard = ignoreCase ? 
				importedNamespace.getLastSegment().equalsIgnoreCase(getWildCard()) :
				importedNamespace.getLastSegment().equals(getWildCard());
		if (hasWildCard) {
			if (importedNamespace.getSegmentCount() <= 1)
				return null;
			return new ImportNormalizer(importedNamespace.skipLast(1), true, ignoreCase);
		} else {
			return new ImportNormalizer(importedNamespace, false, ignoreCase);
		}
	}

	protected IScope getLocalElementsScope(IScope parent, final EObject context,
			final EReference reference) {
		IScope result = parent;
		ISelectable allDescriptions = getAllDescriptions(context.eResource());
		QualifiedName name = getQualifiedNameOfLocalElement(context);
		boolean ignoreCase = isIgnoreCase(reference);
		final List<ImportNormalizer> namespaceResolvers = getImportedNamespaceResolvers(context, ignoreCase);
		if (!namespaceResolvers.isEmpty()) {
			if (isRelativeImport() && name!=null) {
				ImportNormalizer localNormalizer = new ImportNormalizer(name, true, ignoreCase); 
				result = createImportScope(result, singletonList(localNormalizer), allDescriptions, reference.getEReferenceType(), isIgnoreCase(reference));
			}
			result = createImportScope(result, namespaceResolvers, null, reference.getEReferenceType(), isIgnoreCase(reference));
		}
		if (name!=null) {
			ImportNormalizer localNormalizer = new ImportNormalizer(name, true, ignoreCase); 
			result = createImportScope(result, singletonList(localNormalizer), allDescriptions, reference.getEReferenceType(), isIgnoreCase(reference));
		}
		return result;
	}

	protected ImportScope createImportScope(IScope parent, List<ImportNormalizer> namespaceResolvers, ISelectable importFrom, EClass type, boolean ignoreCase) {
		return new ImportScope(namespaceResolvers, parent, importFrom, type, ignoreCase);
	}

	protected QualifiedName getQualifiedNameOfLocalElement(final EObject context) {
		return qualifiedNameProvider.getFullyQualifiedName(context);
	}

	protected boolean isRelativeImport() {
		return true;
	}

	protected ISelectable getAllDescriptions(final Resource resource) {
		return cache.get("internalGetAllDescriptions", resource, new Provider<ISelectable>() {
			public ISelectable get() {
				return internalGetAllDescriptions(resource);
			}
		});
	}
	
	protected ISelectable internalGetAllDescriptions(final Resource resource) {
		Iterable<EObject> allContents = new Iterable<EObject>(){
			public Iterator<EObject> iterator() {
				return EcoreUtil.getAllContents(resource, false);
			}
		}; 
		Iterable<IEObjectDescription> allDescriptions = Scopes.scopedElementsFor(allContents, qualifiedNameProvider);
		return new MultimapBasedSelectable(allDescriptions);
	}

	public String getWildCard() {
		return "*";
	}
}

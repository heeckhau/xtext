/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xtend2.ui.tests.linking;

import static org.eclipse.xtext.ui.junit.util.IResourcesSetupUtil.*;
import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmArrayType;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.ui.resource.XtextResourceSetProvider;
import org.eclipse.xtext.xtend2.ui.tests.AbstractXtend2UITestCase;
import org.eclipse.xtext.xtend2.ui.tests.WorkbenchTestHelper;

import com.google.inject.Inject;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class JavaLinkingTest extends AbstractXtend2UITestCase {

	@Inject
	private WorkbenchTestHelper testHelper;
	
	@Inject
	private XtextResourceSetProvider resourceSetProvider;
	
	@Inject
	private IJvmTypeProvider.Factory typeProviderFactory;
	
	public static Test suite() {
		return WorkbenchTestHelper.suite(JavaLinkingTest.class);
	}
	
	@Override
	protected void tearDown() throws Exception {
		testHelper.tearDown();
		super.tearDown();
	}
	
	public void testNavigateToXtendClass() throws Exception {
		IFile xtendFile = testHelper.createFile(
				"test/XtendClass", 
				"package test\nclass XtendClass {}");
		IFile javaFile = testHelper.createFileImpl(
				WorkbenchTestHelper.TESTPROJECT_NAME + "/src/test/JavaClass.java", 
				"package test;\npublic class JavaClass extends XtendClass {}");
		waitForAutoBuild();

		assertNumberOfMarkers(xtendFile, 0);
		assertNumberOfMarkers(javaFile, 0);
		
		ResourceSet resourceSet = resourceSetProvider.get(testHelper.getProject());
		IJvmTypeProvider jvmTypeProvider = typeProviderFactory.findOrCreateTypeProvider(resourceSet);
		JvmType javaType = jvmTypeProvider.findTypeByName("test.JavaClass");
		assertNotNull(javaType);
		assertTrue(javaType instanceof JvmDeclaredType);
		JvmDeclaredType declaredJavaType = (JvmDeclaredType) javaType;
		JvmTypeReference superType = declaredJavaType.getSuperTypes().get(0);
		JvmType xtendType = superType.getType();
		assertNotNull(xtendType);
		assertEquals("test.XtendClass", xtendType.getQualifiedName());
		Resource xtendResource = xtendType.eResource();
		assertEquals("platform:/resource" + xtendFile.getFullPath(), xtendResource.getURI().toString());
	}
	
	public void testNavigateToXtendClassArray() throws Exception {
		IFile xtendFile = testHelper.createFile(
				"test/XtendClass", 
				"package test\nclass XtendClass {}");
		IFile javaFile = testHelper.createFileImpl(
				WorkbenchTestHelper.TESTPROJECT_NAME + "/src/test/JavaClass.java", 
				"package test;\npublic class JavaClass extends java.util.ArrayList<XtendClass[]> {}");
		waitForAutoBuild();

		assertNumberOfMarkers(xtendFile, 0);
		assertNumberOfMarkers(javaFile, 0);
		
		ResourceSet resourceSet = resourceSetProvider.get(testHelper.getProject());
		IJvmTypeProvider jvmTypeProvider = typeProviderFactory.findOrCreateTypeProvider(resourceSet);
		JvmType javaType = jvmTypeProvider.findTypeByName("test.JavaClass");
		assertNotNull(javaType);
		assertTrue(javaType instanceof JvmDeclaredType);
		JvmDeclaredType declaredJavaType = (JvmDeclaredType) javaType;
		JvmParameterizedTypeReference arrayList = (JvmParameterizedTypeReference) declaredJavaType.getSuperTypes().get(0);
		JvmTypeReference arrayReference = arrayList.getArguments().get(0);
		assertTrue(arrayReference instanceof JvmGenericArrayTypeReference);
		JvmType arrayType = arrayReference.getType();
		assertTrue(arrayType instanceof JvmArrayType);
		JvmType xtendType = ((JvmArrayType) arrayType).getComponentType();
		assertNotNull(xtendType);
		assertEquals("test.XtendClass", xtendType.getQualifiedName());
		Resource xtendResource = xtendType.eResource();
		assertEquals("platform:/resource" + xtendFile.getFullPath(), xtendResource.getURI().toString());
	}
	
	public void testGetXtendClassDirectly() throws Exception {
		IFile xtendFile = testHelper.createFile(
				"test/XtendClass", 
				"package test\nclass XtendClass {}");
		waitForAutoBuild();

		assertNumberOfMarkers(xtendFile, 0);
		
		ResourceSet resourceSet = resourceSetProvider.get(testHelper.getProject());
		IJvmTypeProvider jvmTypeProvider = typeProviderFactory.findOrCreateTypeProvider(resourceSet);
		JvmType xtendType = jvmTypeProvider.findTypeByName("test.XtendClass");
		assertNotNull(xtendType);
		assertEquals("test.XtendClass", xtendType.getQualifiedName());
		Resource xtendResource = xtendType.eResource();
		assertEquals("platform:/resource" + xtendFile.getFullPath(), xtendResource.getURI().toString());
	}
	
	public void testGetXtendClassAsArray() throws Exception {
		IFile xtendFile = testHelper.createFile(
				"test/XtendClass", 
				"package test\nclass XtendClass {}");
		waitForAutoBuild();

		assertNumberOfMarkers(xtendFile, 0);
		
		ResourceSet resourceSet = resourceSetProvider.get(testHelper.getProject());
		IJvmTypeProvider jvmTypeProvider = typeProviderFactory.findOrCreateTypeProvider(resourceSet);
		JvmType xtendArrayType = jvmTypeProvider.findTypeByName("test.XtendClass[][]");
		assertNotNull(xtendArrayType);
		assertTrue(xtendArrayType instanceof JvmArrayType);
		assertEquals("test.XtendClass[][]", xtendArrayType.getQualifiedName());
		Resource xtendResource = xtendArrayType.eResource();
		assertEquals("platform:/resource" + xtendFile.getFullPath(), xtendResource.getURI().toString());
	}
	
	protected void assertNumberOfMarkers(IFile file, int numberOfMarkers) throws CoreException {
		IMarker[] markers = file.findMarkers(EValidator.MARKER, true, 1);
		assertEquals(printMarker(markers),numberOfMarkers,markers.length);
	}
}

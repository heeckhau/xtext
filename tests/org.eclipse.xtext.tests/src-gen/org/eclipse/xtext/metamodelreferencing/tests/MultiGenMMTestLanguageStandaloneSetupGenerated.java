
package org.eclipse.xtext.metamodelreferencing.tests;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.ISetup;
import org.eclipse.emf.ecore.resource.Resource;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Generated from StandaloneSetup.xpt!
 */
@SuppressWarnings("all")
public class MultiGenMMTestLanguageStandaloneSetupGenerated implements ISetup {

	public Injector createInjectorAndDoEMFRegistration() {
		org.eclipse.xtext.common.TerminalsStandaloneSetup.doSetup();

		Injector injector = createInjector();
		register(injector);
		return injector;
	}
	
	public Injector createInjector() {
		return Guice.createInjector(new org.eclipse.xtext.metamodelreferencing.tests.MultiGenMMTestLanguageRuntimeModule());
	}
	
	public void register(Injector injector) {

		org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
		org.eclipse.xtext.resource.IResourceServiceProvider serviceProvider = injector.getInstance(org.eclipse.xtext.resource.IResourceServiceProvider.class);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("multigenmmtestlanguage", resourceFactory);
		org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("multigenmmtestlanguage", serviceProvider);
		
	if (!EPackage.Registry.INSTANCE.containsKey("http://eclipse.org/xtext/tests/SimpleTest")) {
		EPackage.Registry.INSTANCE.put("http://eclipse.org/xtext/tests/SimpleTest", org.eclipse.xtext.metamodelreferencing.tests.simpleTest.SimpleTestPackage.eINSTANCE);
	}
	if (!EPackage.Registry.INSTANCE.containsKey("http://eclipse.org/xtext/tests/OtherTest")) {
		EPackage.Registry.INSTANCE.put("http://eclipse.org/xtext/tests/OtherTest", org.eclipse.xtext.metamodelreferencing.tests.otherTest.OtherTestPackage.eINSTANCE);
	}

	}
}

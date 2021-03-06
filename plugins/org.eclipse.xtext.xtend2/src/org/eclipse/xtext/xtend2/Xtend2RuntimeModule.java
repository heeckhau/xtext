/*
 * generated by Xtext
 */
package org.eclipse.xtext.xtend2;

import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.generator.OutputConfigurationProvider;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.featurecalls.IdentifiableSimpleNameProvider;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.JvmModelAssociator;
import org.eclipse.xtext.xbase.typing.ITypeProvider;
import org.eclipse.xtext.xbase.validation.EarlyExitValidator;
import org.eclipse.xtext.xtend2.compiler.Xtend2Compiler;
import org.eclipse.xtext.xtend2.compiler.Xtend2OutputConfigurationProvider;
import org.eclipse.xtext.xtend2.conversion.Xtend2ValueConverterService;
import org.eclipse.xtext.xtend2.featurecalls.Xtend2IdentifiableSimpleNameProvider;
import org.eclipse.xtext.xtend2.jvmmodel.IXtend2JvmAssociations;
import org.eclipse.xtext.xtend2.jvmmodel.Xtend2JvmModelInferrer;
import org.eclipse.xtext.xtend2.naming.Xtend2QualifiedNameProvider;
import org.eclipse.xtext.xtend2.resource.Xtend2Resource;
import org.eclipse.xtext.xtend2.resource.Xtend2ResourceDescriptionStrategy;
import org.eclipse.xtext.xtend2.resource.XtendEObjectAtOffsetHelper;
import org.eclipse.xtext.xtend2.scoping.Xtend2ImportedNamespaceScopeProvider;
import org.eclipse.xtext.xtend2.scoping.Xtend2ScopeProvider;
import org.eclipse.xtext.xtend2.typing.Xtend2TypeProvider;
import org.eclipse.xtext.xtend2.validation.XtendEarlyExitValidator;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class Xtend2RuntimeModule extends org.eclipse.xtext.xtend2.AbstractXtend2RuntimeModule {
	
	@Override
	public Class<? extends ITypeProvider> bindITypeProvider() {
		return Xtend2TypeProvider.class;
	}
	
	@Override
	public Class<? extends XtextResource> bindXtextResource() {
		return Xtend2Resource.class;
	}
	
	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return Xtend2ValueConverterService.class;
	}

	@Override
	public void configureIScopeProviderDelegate(Binder binder) {
		binder.bind(IScopeProvider.class).annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_DELEGATE))
		.to(Xtend2ImportedNamespaceScopeProvider.class);
	}

	@Override
	public Class<? extends IdentifiableSimpleNameProvider> bindIdentifiableSimpleNameProvider() {
		return Xtend2IdentifiableSimpleNameProvider.class;
	}

	public Class<? extends IJvmModelInferrer> bindIJvmModelInferrer() {
		return Xtend2JvmModelInferrer.class;
	}

	@Override
	public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return Xtend2QualifiedNameProvider.class;
	}
	
	public Class <? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
		return Xtend2ResourceDescriptionStrategy.class;
	}

	public Class<? extends JvmModelAssociator> bindJvmModelAssociator() {
		return IXtend2JvmAssociations.Impl.class;
	}

	public Class<? extends EarlyExitValidator> bindEarlyExitValidator() {
		return XtendEarlyExitValidator.class;
	}
	
	@Override
	public Class<? extends EObjectAtOffsetHelper> bindEObjectAtOffsetHelper() {
		return XtendEObjectAtOffsetHelper.class;
	}
	
	public Class<? extends XbaseCompiler> bindXbaseCompiler() {
		return Xtend2Compiler.class; 
	}
	
	public Class<? extends OutputConfigurationProvider> bindOutputConfigurationProvider() {
		return Xtend2OutputConfigurationProvider.class;
	}
	
	@Override
	public java.lang.Class<? extends IScopeProvider> bindIScopeProvider() {
		return Xtend2ScopeProvider.class;
	}

}

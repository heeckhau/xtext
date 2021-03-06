/*
 * generated by Xtext
 */
package org.eclipse.xtext.ui.tests;

import static com.google.inject.util.Modules.override;
import static com.google.inject.Guice.createInjector;

import org.apache.log4j.Logger;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.Map;
import java.util.HashMap;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class Activator extends AbstractUIPlugin {

	private Map<String,Injector> injectors = new HashMap<String,Injector>();
	private static Activator INSTANCE;

	public Injector getInjector(String languageName) {
		return injectors.get(languageName);
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		try {
			registerInjectorFor("org.eclipse.xtext.ui.tests.XtextGrammarUiTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.linking.ImportUriUiTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.TwoContextsTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug286935TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug287941TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug288734TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug288760TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug289187TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug291022TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug303200TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug304681TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug307519TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug309949TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug332217TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug348427TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug348199TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug360834TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.Bug347012TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.BacktrackingContentAssistTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistContextTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistCustomizingTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.CrossReferenceProposalTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.DatatypeRuleTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.LookAheadContentAssistTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.DomainModelTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.contentassist.UnorderedGroupsTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.outline.OutlineTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.parser.keywords.KeywordsUiTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.testlanguages.ContentAssistTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.testlanguages.ReferenceGrammarUiTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.encoding.EncodingUiTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.enumrules.EnumRulesUiTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.TestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.FoldingTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.editor.bracketmatching.BmTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.quickfix.QuickfixCrossrefTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.refactoring.RefactoringTestLanguage");
			registerInjectorFor("org.eclipse.xtext.ui.tests.refactoring.ReferringTestLanguage");
			
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
			throw e;
		}
	}
	
	protected void registerInjectorFor(String language) throws Exception {
		injectors.put(language, createInjector(
		  override(override(getRuntimeModule(language)).with(getSharedStateModule())).with(getUiModule(language))));
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		injectors.clear();
		INSTANCE = null;
		super.stop(context);
	}
	
	public static Activator getInstance() {
		return INSTANCE;
	}
	
	protected Module getRuntimeModule(String grammar) {
		if ("org.eclipse.xtext.ui.tests.XtextGrammarUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.XtextGrammarUiTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.linking.ImportUriUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.linking.ImportUriUiTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.TwoContextsTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.TwoContextsTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug286935TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug286935TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug287941TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug287941TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug288734TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug288734TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug288760TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug288760TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug289187TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug289187TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug291022TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug291022TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug303200TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug303200TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug304681TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug304681TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug307519TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug307519TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug309949TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug309949TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug332217TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug332217TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug348427TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug348427TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug348199TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug348199TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug360834TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug360834TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug347012TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.Bug347012TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.BacktrackingContentAssistTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.BacktrackingContentAssistTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistContextTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistContextTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistCustomizingTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistCustomizingTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.CrossReferenceProposalTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.CrossReferenceProposalTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.DatatypeRuleTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.DatatypeRuleTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.LookAheadContentAssistTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.LookAheadContentAssistTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.DomainModelTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.DomainModelTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.UnorderedGroupsTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.UnorderedGroupsTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.outline.OutlineTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.outline.OutlineTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.parser.keywords.KeywordsUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.parser.keywords.KeywordsUiTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.testlanguages.ContentAssistTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.testlanguages.ContentAssistTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.testlanguages.ReferenceGrammarUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.testlanguages.ReferenceGrammarUiTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.encoding.EncodingUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.encoding.EncodingUiTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.enumrules.EnumRulesUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.enumrules.EnumRulesUiTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.TestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.FoldingTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.FoldingTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.editor.bracketmatching.BmTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.bracketmatching.BmTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.quickfix.QuickfixCrossrefTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.quickfix.QuickfixCrossrefTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.refactoring.RefactoringTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.refactoring.RefactoringTestLanguageRuntimeModule();
		}
		if ("org.eclipse.xtext.ui.tests.refactoring.ReferringTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.refactoring.ReferringTestLanguageRuntimeModule();
		}
		
		throw new IllegalArgumentException(grammar);
	}
	
	protected Module getUiModule(String grammar) {
		if ("org.eclipse.xtext.ui.tests.XtextGrammarUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.ui.XtextGrammarUiTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.linking.ImportUriUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.linking.ui.ImportUriUiTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.TwoContextsTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.TwoContextsTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug286935TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug286935TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug287941TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug287941TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug288734TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug288734TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug288760TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug288760TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug289187TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug289187TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug291022TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug291022TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug303200TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug303200TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug304681TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug304681TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug307519TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug307519TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug309949TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug309949TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug332217TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug332217TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug348427TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug348427TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug348199TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug348199TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug360834TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug360834TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.Bug347012TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.Bug347012TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.BacktrackingContentAssistTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.BacktrackingContentAssistTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistContextTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.ContentAssistContextTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.ContentAssistCustomizingTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.ContentAssistCustomizingTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.CrossReferenceProposalTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.CrossReferenceProposalTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.DatatypeRuleTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.DatatypeRuleTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.LookAheadContentAssistTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.LookAheadContentAssistTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.DomainModelTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.DomainModelTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.contentassist.UnorderedGroupsTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.contentassist.ui.UnorderedGroupsTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.outline.OutlineTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.outline.ui.OutlineTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.parser.keywords.KeywordsUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.parser.keywords.ui.KeywordsUiTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.testlanguages.ContentAssistTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.testlanguages.ui.ContentAssistTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.testlanguages.ReferenceGrammarUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.testlanguages.ui.ReferenceGrammarUiTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.encoding.EncodingUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.encoding.ui.EncodingUiTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.enumrules.EnumRulesUiTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.enumrules.ui.EnumRulesUiTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.TestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.ui.TestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.FoldingTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.ui.FoldingTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.editor.bracketmatching.BmTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.editor.bracketmatching.ui.BmTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.quickfix.QuickfixCrossrefTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.quickfix.ui.QuickfixCrossrefTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.refactoring.RefactoringTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.refactoring.ui.RefactoringTestLanguageUiModule(this);
		}
		if ("org.eclipse.xtext.ui.tests.refactoring.ReferringTestLanguage".equals(grammar)) {
		  return new org.eclipse.xtext.ui.tests.refactoring.ui.ReferringTestLanguageUiModule(this);
		}
		
		throw new IllegalArgumentException(grammar);
	}
	
	protected Module getSharedStateModule() {
		return new org.eclipse.xtext.ui.shared.SharedStateModule();
	}
	
}
